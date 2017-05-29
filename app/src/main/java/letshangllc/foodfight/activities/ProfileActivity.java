package letshangllc.foodfight.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.DatabaseConstants;
import letshangllc.foodfight.models.UserPost;
import letshangllc.foodfight.models.layoutbindings.GalleryItem;
import letshangllc.foodfight.models.layoutbindings.MealCard;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();

    /* Views */
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getUserPosts();


    }

    @Override
    protected void onResume() {
        super.onResume();

        progressDialog = ProgressDialog.show(this, "Gathering data", "Please wait...", true, false);
        progressDialog.show();
        getUserPosts();
    }


    private void getUserPosts(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference(DatabaseConstants.USER_POSTS).child(userId);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // Get Post object and use the values to update the UI
                ArrayList<UserPost> userPosts = new ArrayList<>();

                Log.i(TAG, "Posts#: ");

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserPost post = postSnapshot.getValue(UserPost.class);
                    userPosts.add(post);
                    Log.e("Get Data", post.toString());
                }

                setupGallery(userPosts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                progressDialog.dismiss();
                // ...
            }
        };
        databaseReference.addListenerForSingleValueEvent(postListener);
    }

    private void setupGallery(ArrayList<UserPost> userPosts){
        PlaceHolderView mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        mGalleryView.getBuilder().setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 3));

        for (UserPost userPost : userPosts) {
            mGalleryView.addView(new GalleryItem(this, userPost));
            mGalleryView.addView(new GalleryItem(this, userPost));
            mGalleryView.addView(new GalleryItem(this, userPost));
        }


        progressDialog.dismiss();
    }


}
