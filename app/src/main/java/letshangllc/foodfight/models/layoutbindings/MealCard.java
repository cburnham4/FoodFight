package letshangllc.foodfight.models.layoutbindings;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

import java.util.Locale;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.FirebaseHelper;
import letshangllc.foodfight.models.UserPost;

import static android.content.ContentValues.TAG;

/**
 * Created by carlburnham on 5/28/17.
 */

@Layout(R.layout.cardview_meal)
public class MealCard {
    @View(R.id.imgMeal)
    private ImageView imgMeal;

    @View(R.id.tvMealName)
    private TextView nameAgeTxt;

    @View(R.id.tvRestaurant)
    private TextView locationNameTxt;

    private UserPost userPost;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public MealCard(Context context, UserPost userPost, SwipePlaceHolderView swipeView) {
        mContext = context;
        this.userPost = userPost;
        mSwipeView = swipeView;
    }

    @Resolve
    private void onResolved(){
        Glide.with(mContext).load(userPost.downloadUrl).into(imgMeal);
        nameAgeTxt.setText(userPost.mealName);
        locationNameTxt.setText(String.format(Locale.getDefault()," %d",userPost.score));
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        //mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        Log.d(TAG, "User swiped: " + userPost.mealName);
        FirebaseHelper.uploadLikedMeal(userPost);
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }
}
