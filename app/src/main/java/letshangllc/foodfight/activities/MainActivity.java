package letshangllc.foodfight.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

import letshangllc.foodfight.R;
import letshangllc.foodfight.fragments.LikedMealsFragment;
import letshangllc.foodfight.fragments.MealSwipeFragment;
import letshangllc.foodfight.fragments.PopularMealsFragment;
import letshangllc.foodfight.fragments.ProfileFragment;
import letshangllc.foodfight.models.DatabaseConstants;
import letshangllc.foodfight.models.layoutbindings.MealCard;
import letshangllc.foodfight.models.UserPost;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    /* Views */
    private SwipePlaceHolderView mSwipeView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setFirstTab();
        setupBottomBar();
    }

    private void setFirstTab(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        /* Fragments */
        final MealSwipeFragment mealSwipeFragment = new MealSwipeFragment();

        fragmentTransaction.add(R.id.linFragmentViewer, mealSwipeFragment);
        fragmentTransaction.commit();
    }

    /* Setup the bottom bar to handle fragment transactions */
    private void setupBottomBar(){
        final FragmentManager fragmentManager = getSupportFragmentManager();

        /* Fragments */
        final MealSwipeFragment mealSwipeFragment = new MealSwipeFragment();
        final PopularMealsFragment popularMealsFragment = new PopularMealsFragment();
        final LikedMealsFragment likedMealsFragment = new LikedMealsFragment();
        final ProfileFragment profileFragment = new ProfileFragment();



        /* Setup tab bar */
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (tabId == R.id.tab_main) {
                    fragmentTransaction.replace(R.id.linFragmentViewer, mealSwipeFragment);
                }else if (tabId == R.id.tab_profile) {
                    fragmentTransaction.replace(R.id.linFragmentViewer, profileFragment);
                }else if (tabId == R.id.tab_liked){
                    fragmentTransaction.replace(R.id.linFragmentViewer, likedMealsFragment);
                }else if(tabId == R.id.tab_popular){
                    fragmentTransaction.replace(R.id.linFragmentViewer, popularMealsFragment);
                }

                /* Commit the fragment transaction */
                fragmentTransaction.addToBackStack(getString(R.string.profile_activity));
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    /* Override backpress to go back in the fragment stack */
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSwipeView.removeAllViews();
    }
}
