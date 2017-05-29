package letshangllc.foodfight.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.FirebaseHelper;
import letshangllc.foodfight.models.Utils;

import static java.security.AccessController.getContext;

public class CreateUserMealActivity extends AppCompatActivity {
    private static final String TAG = CreateUserMealActivity.class.getSimpleName();

    /* Requests */
    private static final int SELECT_PICTURE = 12, FILE_PERMISSION_REQUEST = 99,
            CAMERA_REQUEST_PERMISSION = 13, REQUEST_IMAGE_CAPTURE = 11;;

    /* Permissions */
    boolean hasFilePermissions = false, hasCameraPermission = false;

    /* Firebase */
    private FirebaseAuth firebaseAuth;
    private StorageReference localStorageReference;

    /* Views */
    private TextView tvUploadImage;
    private EditText etMealName;
    private ImageView imgUploadedImage;

    /* Selected photo */
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_meal);

        setupToolbar();
        findViews();
        checkPermissions();

        cropImage();
    }

    private void cropImage(){
        // start picker to get image for cropping and then use the image in cropping activity


    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void uploadPhotoOnClick(View view){
        getPhotoFromGallery();
    }


    private void findViews(){
        tvUploadImage = (TextView) findViewById(R.id.tvUploadImage);
        etMealName = (EditText) findViewById(R.id.etMealName);
        imgUploadedImage = (ImageView) findViewById(R.id.imgUploadedImage);
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
     * Capture a photo to upload
     */
    private void capturePhoto(){
        PackageManager packageManager = this.getPackageManager();
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) return;

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

                        selectedImageUri = data.getData();

                        CropImage.activity(selectedImageUri)
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setMinCropWindowSize(240,240)
                                .setMinCropResultSize(240,240)
                                .setMaxCropResultSize(720,720)
                                .setAspectRatio(1,1)
                                .start(this);

                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    try {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        Uri resultUri = result.getUri();

                        /* Get bitmap */
                        InputStream imageStream = getContentResolver().openInputStream(resultUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        imgUploadedImage.setImageBitmap(selectedImage);
//                        tvUploadImage.setBackground(new BitmapDrawable(getResources(), selectedImage));

                        Log.i(TAG, "X: " + selectedImage.getWidth() + " Y: " + selectedImage.getHeight());
//
                        tvUploadImage.setVisibility(View.GONE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Utils.makeToast(CreateUserMealActivity.this, "Something went wrong. Please Try Again");
                    }
                    break;
            }

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

    /*
     * Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_meal_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_post:
                Log.i(TAG, "Post image to Firebase");
                uploadImage();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void uploadImage(){
        String mealName = etMealName.getText().toString();

        Log.i(TAG, "Mealname " + mealName);
        Log.i(TAG, "URI: " + selectedImageUri.toString());

        if(mealName.isEmpty() || selectedImageUri == null){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(this, "Uploading Meal", "Please wait...", true, false);
        progressDialog.show();

        /* upload the photo and make toast based on callback */
        FirebaseHelper.uploadImage(selectedImageUri, mealName, new FirebaseHelper.FirebaseListener() {
            @Override
            public void firebaseSucceeded(boolean success) {
                progressDialog.dismiss();
                if(success){
                    Toast.makeText(CreateUserMealActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(CreateUserMealActivity.this, "Upload Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
