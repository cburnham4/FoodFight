package letshangllc.foodfight.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import letshangllc.foodfight.models.Utils;
import letshangllc.foodfight.models.layoutbindings.GalleryItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikedMealsFragment extends Fragment {
    private static final String TAG = LikedMealsFragment.class.getSimpleName();

    /* Views */
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liked_meals, container, false);

        progressDialog = ProgressDialog.show(getContext(), "Gathering data", "Please wait...", true, false);
        progressDialog.show();

        getUserLiked();

        return view;
    }

    private void getUserLiked(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference(DatabaseConstants.USER_LIKED).child(userId);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UserPost> usersLikedPosts = new ArrayList<>();

                /* Loop through user liked photos and add them to the list */
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserPost post = postSnapshot.getValue(UserPost.class);
                    usersLikedPosts.add(post);
                }

                progressDialog.dismiss();

                setupGallery(usersLikedPosts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                progressDialog.dismiss();
            }
        };
        databaseReference.addListenerForSingleValueEvent(postListener);
    }


    /* Setup the gallery of liked meals */
    private void setupGallery(ArrayList<UserPost> userPosts) {
        PlaceHolderView mGalleryView = (PlaceHolderView) getView().findViewById(R.id.galleryView);
        mGalleryView.getBuilder().setLayoutManager(new GridLayoutManager(this.getContext(), 3));

        for (UserPost userPost : userPosts) {
            mGalleryView.addView(new GalleryItem(this.getContext(), userPost));
        }
    }
}
