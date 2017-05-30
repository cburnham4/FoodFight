package letshangllc.foodfight.activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.TextureView;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.DatabaseConstants;
import letshangllc.foodfight.models.UserPost;
import letshangllc.foodfight.models.Utils;
import letshangllc.foodfight.models.layoutbindings.GalleryItem;
import letshangllc.foodfight.models.layoutbindings.MealCard;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();

    /* Views */
    ProgressDialog progressDialog;
    private TextView tvFirstName, tvLastName, tvSavedCount, tvPostCount;

    /* Firebase */
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setupToolbar();
        findViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog = ProgressDialog.show(this, "Gathering data", "Please wait...", true, false);
        progressDialog.show();
        getUserPosts();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void findViews(){
        tvFirstName = (TextView) findViewById(R.id.tvFirstName);
        //tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvSavedCount = (TextView) findViewById(R.id.tvSavedCount);
        tvPostCount = (TextView) findViewById(R.id.tvPostCount);
    }


    private void getUserPosts(){
        String userId = firebaseUser.getUid();

//        String displayName = "";
//        for (UserInfo profile : firebaseUser.getProviderData()) {
//            // Id of the provider (ex: google.com)
//            String providerId = profile.getProviderId();
//
//            // UID specific to the provider
//            String uid = profile.getUid();
//
//            // Name, email address, and profile photo Url
//            displayName = profile.getDisplayName();
//            String email = profile.getEmail();
//            Uri photoUrl = profile.getPhotoUrl();
//        };

        String displayName = firebaseUser.getDisplayName();
        if(displayName!=null && !displayName.isEmpty())tvFirstName.setText(firebaseUser.getDisplayName());
        else tvFirstName.setText(firebaseUser.getEmail());



        final DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference(DatabaseConstants.USER_POSTS).child(userId);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // Get Post object and use the values to update the UI
                ArrayList<UserPost> userPosts = new ArrayList<>();

                int postCount = 0;

                Log.i(TAG, "Posts#: ");

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserPost post = postSnapshot.getValue(UserPost.class);
                    userPosts.add(post);
                    Log.e("Get Data", post.toString());
                    postCount++;
                }

                tvPostCount.setText(Utils.intToString(postCount));
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
        }

        progressDialog.dismiss();
    }


}
