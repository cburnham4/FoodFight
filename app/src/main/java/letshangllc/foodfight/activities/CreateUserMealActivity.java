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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.FirebaseHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_meal);

        setupFirebase();
    }

    public void uploadPhotoOnClick(View view){
        getPhotoFromGallery();
    }

    private void setupFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        localStorageReference = FirebaseStorage.getInstance().getReference();
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
                    try {
                        Uri selectedImageUri = data.getData();
                        FirebaseHelper.uploadImage(selectedImageUri, firebaseAuth, localStorageReference);

                        /* Get bitmap */
                        InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(CreateUserMealActivity.this, "Something went wrong. Please Try Again",
                                Toast.LENGTH_LONG).show();
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
}
