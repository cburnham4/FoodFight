package letshangllc.foodfight.models;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by carlburnham on 5/28/17.
 */

public class FirebaseHelper {
    private static final String TAG = FirebaseHelper.class.getSimpleName();
    private static FirebaseDatabase firebaseDatabase;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference localStorageReference;

    /* Callback to tell the user when the post has been completed */
    public interface FirebaseListener{
        void firebaseSucceeded(boolean success);
    }



    /*
     * Upload Image to storage
     */
    @SuppressWarnings("VisibleForTests")
    public static void uploadImage(Uri uri, final String mealName, final FirebaseListener firebaseListener){
        Log.i(TAG, "Upload image");

        firebaseAuth = FirebaseAuth.getInstance();
        localStorageReference = FirebaseStorage.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String userId = user.getUid();

        /* Create filepath for image */
        String fileName = mealName + ".jpg";
        String filePath = "images/users/"+userId+"/"+fileName;

        StorageReference storageReference = localStorageReference.child(filePath);

        /* Post the image to the database storage */
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "upload success: ");
                Uri uri = taskSnapshot.getDownloadUrl();
                uploadDownloadURL(uri, userId, mealName, firebaseListener);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseListener.firebaseSucceeded(false);
            }
        });
    }

    /*
     * Upload photo downloadUrl to database
     */
    private static void uploadDownloadURL(Uri downloadURL, String userId, String mealName, FirebaseListener firebaseListener){
        Log.i(TAG, "Save download url" + downloadURL.toString());
        firebaseDatabase = FirebaseDatabase.getInstance();

        UserPost userPost = new UserPost(userId, mealName, null, null, downloadURL.toString(), 1);

        /* Point the database to the user's posts */
        DatabaseReference databaseReference = firebaseDatabase.getReference(DatabaseConstants.USER_POSTS).child(userId);

        /* Create an id for the new post */
        String postId = databaseReference.push().getKey();
        databaseReference.child(postId).setValue(userPost);

        firebaseListener.firebaseSucceeded(true);
    }

    /*
     * Upload Liked Photo to user saved
     */
    public static void uploadLikedMeal(UserPost userPost){
        firebaseDatabase = FirebaseDatabase.getInstance();

        /* Point the database to the user's posts */
        DatabaseReference databaseReference = firebaseDatabase.getReference(DatabaseConstants.USER_LIKED).child(userPost.uid);

        /* Create an id for the new post */
        String postId = databaseReference.push().getKey();
        databaseReference.child(postId).setValue(userPost);
    }
}
