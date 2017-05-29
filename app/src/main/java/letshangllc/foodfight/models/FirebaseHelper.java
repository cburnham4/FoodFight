package letshangllc.foodfight.models;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by carlburnham on 5/28/17.
 */

public class FirebaseHelper {
    private static final String TAG = FirebaseHelper.class.getSimpleName();
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();



    /*
     * Upload Image
     */
    @SuppressWarnings("VisibleForTests")
    public static void uploadImage(Uri uri, FirebaseAuth firebaseAuth, StorageReference localStorageReference){
        Log.i(TAG, "Upload image");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userid = user.getUid();

        String fileName = "fileName.jpg";
        String filePath = "images/users/"+userid+"/"+fileName;

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        StorageReference storageReference = localStorageReference.child(filePath);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "upload success: ");
                Uri uri = taskSnapshot.getDownloadUrl();
                uploadDownloadURL(uri);
            }
        });
    }

    /*
     * Upload photo download url
     */
    private static void uploadDownloadURL(Uri downloadURL){
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference("message");
        databaseReference.setValue(downloadURL);
    }
}
