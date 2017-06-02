package letshangllc.foodfight.activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import java.util.List;

import letshangllc.foodfight.R;
import letshangllc.foodfight.fragments.UserPostsFragment;
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

    /* Data checkers - Make sure you have both things of data */
    private boolean hasPosts = false, hasSaved = false;

    /* Data */
    private ArrayList<UserPost> userPosts, usersLikedPosts;

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
        getUserLiked();

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
                userPosts = new ArrayList<>();

                int postCount = 0;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserPost post = postSnapshot.getValue(UserPost.class);
                    userPosts.add(post);
                    Log.e("Get Data", post.toString());
                    postCount++;
                }

                tvPostCount.setText(Utils.intToString(postCount));
                progressDialog.dismiss();

                hasPosts = true;

                if(hasSaved){
                    setupTabs();
                }
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

    private void getUserLiked(){
        String userId = firebaseUser.getUid();

        final DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference(DatabaseConstants.USER_LIKED).child(userId);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // Get Post object and use the values to update the UI
                usersLikedPosts = new ArrayList<>();

                int postCount = 0;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserPost post = postSnapshot.getValue(UserPost.class);
                    usersLikedPosts.add(post);
                    Log.e("Get Data", post.toString());
                    postCount++;
                }

                tvPostCount.setText(Utils.intToString(postCount));
                progressDialog.dismiss();

                hasSaved = true;

                if(hasPosts){
                    setupTabs();
                }
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

    private void setupTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpProfile);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        UserPostsFragment userPostsFragment = new UserPostsFragment();
        Bundle postsBundle = new Bundle();
        postsBundle.putParcelableArrayList(DatabaseConstants.USER_POSTS, userPosts);
        userPostsFragment.setArguments(postsBundle);


        UserPostsFragment userPostsFragment2 = new UserPostsFragment();
        Bundle postsBundle2 = new Bundle();
        postsBundle2.putParcelableArrayList(DatabaseConstants.USER_POSTS, usersLikedPosts);
        userPostsFragment2.setArguments(postsBundle2);


        adapter.addFragment(userPostsFragment, "Posts");
        adapter.addFragment(userPostsFragment2, "Liked");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
