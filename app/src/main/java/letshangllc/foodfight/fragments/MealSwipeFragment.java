package letshangllc.foodfight.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;
import java.util.StringTokenizer;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.DatabaseConstants;
import letshangllc.foodfight.models.UserPost;
import letshangllc.foodfight.models.layoutbindings.MealCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealSwipeFragment extends Fragment {
    private static final String TAG = MealSwipeFragment.class.getSimpleName();

    /* Views */
    private ProgressDialog progressDialog;
    private SwipePlaceHolderView swipeView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_swipe, container, false);

        swipeView = (SwipePlaceHolderView) view.findViewById(R.id.swipeView);

        /* Start progress dialog and get the data */
        progressDialog = ProgressDialog.show(getContext(), "Gathering data", "Please wait...", true, false);
        progressDialog.show();
        getData(view);

        return view;
    }


    private void getData(final View view){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(DatabaseConstants.USER_POSTS);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ArrayList<UserPost> userPosts = new ArrayList<>();

                for (DataSnapshot userPostSnapshot: dataSnapshot.getChildren()) {
                    for(DataSnapshot postSnapshot: userPostSnapshot.getChildren()) {

                        UserPost post = postSnapshot.getValue(UserPost.class);
                        post.key = postSnapshot.getKey();
                        userPosts.add(post);
                        Log.e("Get Data", post.toString());
                    }
                }

                setupSwipeView(view, userPosts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

                progressDialog.dismiss();
            }


        };
        databaseReference.addListenerForSingleValueEvent(postListener);
    }

    private void setupSwipeView(View view, ArrayList<UserPost> userPosts) {
        swipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_message_view_accept)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_message_view_reject));


        for (UserPost userPost : userPosts) {
            swipeView.addView(new MealCard(getContext(), userPost, swipeView));
        }

        view.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeView.doSwipe(false);
            }
        });

        view.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipeView.doSwipe(true);

            }
        });

        progressDialog.dismiss();
    }

}
