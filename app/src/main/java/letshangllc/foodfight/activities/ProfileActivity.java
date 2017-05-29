package letshangllc.foodfight.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.mindorks.placeholderview.PlaceHolderView;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.layoutbindings.GalleryItem;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        PlaceHolderView mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);

        mGalleryView.getBuilder().setLayoutManager(new GridLayoutManager(this.getApplicationContext(), 3));
        mGalleryView
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.profile_pic)))
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.profile_pic)))
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.food1_example)))
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.food1_example)))
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.profile_pic)))
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.food1_example)))
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.food1_example)))
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.profile_pic)))
                .addView(new GalleryItem(getResources().getDrawable(R.drawable.profile_pic)));
    }
}
