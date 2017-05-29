package letshangllc.foodfight.models.layoutbindings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.UserPost;

/**
 * Created by carlburnham on 5/29/17.
 */

@Animate(Animation.CARD_LEFT_IN_DESC)
@NonReusable
@Layout(R.layout.gallery_item)
public class GalleryItem {

    @View(R.id.imgMeal)
    private ImageView imgMeal;

    private Drawable mDrawable;

    private UserPost userPost;
    private Context context;

    public GalleryItem(Context context, UserPost userPost) {
        this.userPost = userPost;
        this.context = context;
    }

    @Resolve
    private void onResolved() {
        Glide.with(context).load(userPost.downloadUrl).into(imgMeal);
    }
}