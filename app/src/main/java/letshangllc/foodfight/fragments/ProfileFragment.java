package letshangllc.foodfight.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import letshangllc.foodfight.models.Utils;
import letshangllc.foodfight.models.layoutbindings.GalleryItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    /* Views */
    ProgressDialog progressDialog;
    private TextView tvFirstName, tvLastName, tvLikedCount, tvPostCount;

    /* Firebase */
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setupFirebase();
        progressDialog = ProgressDialog.show(getContext(), "Gathering data", "Please wait...", true, false);
        progressDialog.show();
        findViews(view);
        getUserPosts();

        return view;
    }

    private void setupFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void findViews(View view){
        tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
        //tvLastName = (TextView) findViewById(R.id.tvLastName);
        tvLikedCount = (TextView) view.findViewById(R.id.tvLikedCount);
        tvPostCount = (TextView) view.findViewById(R.id.tvPostCount);
    }


    private void getUserPosts(){
        String userId = firebaseUser.getUid();

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

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserPost post = postSnapshot.getValue(UserPost.class);
                    userPosts.add(post);
                    Log.e("Get Data", post.toString());
                    postCount++;
                }

                tvPostCount.setText(Utils.intToString(postCount));
                progressDialog.dismiss();

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


    private void setupGallery(ArrayList<UserPost> userPosts) {
        PlaceHolderView mGalleryView = (PlaceHolderView) getView().findViewById(R.id.galleryView);
        mGalleryView.getBuilder().setLayoutManager(new GridLayoutManager(this.getContext(), 3));

        for (UserPost userPost : userPosts) {
            mGalleryView.addView(new GalleryItem(this.getContext(), userPost));
        }
    }



}
