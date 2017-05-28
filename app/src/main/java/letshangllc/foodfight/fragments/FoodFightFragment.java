package letshangllc.foodfight.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import letshangllc.foodfight.R;
import letshangllc.foodfight.models.Meal;
import letshangllc.foodfight.models.MealCard;
import letshangllc.foodfight.models.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFightFragment extends Fragment {
    private SwipePlaceHolderView mSwipeView;

    public FoodFightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_fight, container, false);

        setupSwipeView(view);

        return view;
    }

    private void setupSwipeView(View view) {
        mSwipeView = (SwipePlaceHolderView) view.findViewById(R.id.swipeView);

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.swipe_message_view_accept)
                        .setSwipeOutMsgLayoutId(R.layout.swipe_message_view_reject));


        for (Meal meal : Utils.loadMeals(this.getContext())) {
            mSwipeView.addView(new MealCard(getContext(), meal, mSwipeView));
        }

        view.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        view.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });
    }

}
