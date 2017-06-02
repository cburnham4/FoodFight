package letshangllc.foodfight.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.DatabaseConstants;
import letshangllc.foodfight.models.UserPost;
import letshangllc.foodfight.models.Utils;
import letshangllc.foodfight.models.layoutbindings.GalleryItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPostsFragment extends Fragment {


    public UserPostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_posts, container, false);

        Bundle arguments = getArguments();
        ArrayList<UserPost> userPosts = arguments.getParcelableArrayList(DatabaseConstants.USER_POSTS);
        setupGallery(view, userPosts);

        return view;
    }



    private void setupGallery(View view, ArrayList<UserPost> userPosts) {
        PlaceHolderView mGalleryView = (PlaceHolderView) view.findViewById(R.id.galleryView);
        mGalleryView.getBuilder().setLayoutManager(new GridLayoutManager(this.getContext(), 3));

        for (UserPost userPost : userPosts) {
            mGalleryView.addView(new GalleryItem(this.getContext(), userPost));
        }
    }

}
