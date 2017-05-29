package letshangllc.foodfight.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.DatabaseConstants;
import letshangllc.foodfight.models.layoutbindings.MealCard;
import letshangllc.foodfight.models.UserPost;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /* Views */
    private Toolbar toolbar;
    private SwipePlaceHolderView mSwipeView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        setupToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();

        progressDialog = ProgressDialog.show(this, "Gathering data", "Please wait...", true, false);
        progressDialog.show();
        getData();

    }

    private void getData(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(DatabaseConstants.USER_POSTS);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                ArrayList<UserPost> userPosts = new ArrayList<>();

                for (DataSnapshot userPostSnapshot: dataSnapshot.getChildren()) {
                    for(DataSnapshot postSnapshot: userPostSnapshot.getChildren()) {
                        UserPost post = postSnapshot.getValue(UserPost.class);
                        userPosts.add(post);
                        Log.e("Get Data", post.toString());
                    }
                }

                setupSwipeView(userPosts);
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

    private void setupSwipeView(ArrayList<UserPost> userPosts) {
        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_message_view_accept)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_message_view_reject));


        for (UserPost userPost : userPosts) {
            mSwipeView.addView(new MealCard(this, userPost, mSwipeView));
        }

        findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

        progressDialog.dismiss();
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /*
     * Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_upload_meal:
                startActivity(new Intent(MainActivity.this, CreateUserMealActivity.class));
                return true;
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

}
