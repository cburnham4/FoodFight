package letshangllc.foodfight.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.FirebaseHelper;
import letshangllc.foodfight.models.Meal;
import letshangllc.foodfight.models.MealCard;
import letshangllc.foodfight.models.Utils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /* Views */
    private Toolbar toolbar;
    private SwipePlaceHolderView mSwipeView;

    /* Firebase */
    private FirebaseAuth firebaseAuth;
    private StorageReference localStorageReference;

    /* Permissions */
    boolean hasFilePermissions = false, hasCameraPermission = false;

    /* Requests */
    private static final int SELECT_PICTURE = 12, FILE_PERMISSION_REQUEST = 99,
            CAMERA_REQUEST_PERMISSION = 13, REQUEST_IMAGE_CAPTURE = 11;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupFirebase();
        setupSwipeView();
        checkPermissions();
    }

    private void setupFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        localStorageReference = FirebaseStorage.getInstance().getReference();
    }

    private void setupSwipeView() {
        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_message_view_accept)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_message_view_reject));


        for (Meal meal : Utils.loadMeals(this)) {
            mSwipeView.addView(new MealCard(this, meal, mSwipeView));
        }

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /*
     * Permissions
     */

    private void checkPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionCheck += ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if(permissionCheck != 0){
                askForPermissions();
            }
        }
    }

    private void askForPermissions(){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                FILE_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FILE_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    hasFilePermissions = true;
                }
                return;
            }
        }
    }



    /*
     * Menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        PackageManager packageManager = this.getPackageManager();
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) return true;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_take_photo:
                getPhotoFromGallery();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Capture a photo to upload
     */

    private void capturePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Log.i(TAG, "Got photo");
                    break;
                case SELECT_PICTURE:
                    try {
                        Uri selectedImageUri = data.getData();
                        FirebaseHelper.uploadImage(selectedImageUri, firebaseAuth, localStorageReference);

                        /* Get bitmap */
                        InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Something went wrong. Please Try Again",
                                Toast.LENGTH_LONG).show();
                    }

                    break;
            }

        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.i(TAG, "Got photo");
            //mImageView.setImageBitmap(imageBitmap);
        }
    }

    /*
     * Grab photo from gallery
     */
    private void getPhotoFromGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }


}
