package letshangllc.foodfight.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import letshangllc.foodfight.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMealsFragment extends Fragment {


    public PopularMealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_meals, container, false);
    }

}
