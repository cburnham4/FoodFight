package letshangllc.foodfight.activities;

import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import letshangllc.foodfight.R;

public class IngredientsListActivity extends AppCompatActivity {
    private static final String TAG = IngredientsListActivity.class.getSimpleName();

    /* Views */
    private LinearLayout linIngredientInputs;
    private ArrayList<LinearLayout> ingredientInputs;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    /* Variables */
    private static int INGREDIENT_NUM = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_list);

        findViews();
        createIngredientsList();
    }

    private void findViews(){
        linIngredientInputs = (LinearLayout) findViewById(R.id.linIngredientInputs);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.measurements, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void createIngredientsList(){
        ingredientInputs = new ArrayList<>();



        /* Create the list of ingredient inputs */
        for(int i = 0; i < INGREDIENT_NUM; i++){
            LinearLayout ingredientInput = (LinearLayout) getLayoutInflater().inflate(R.layout.item_input_ingredient, linIngredientInputs);

            Spinner spinMeasurements = (Spinner) ingredientInput.findViewById(R.id.spinMeasurements);
            spinMeasurements.setAdapter(spinnerAdapter);

            ingredientInputs.add(ingredientInput);
        }
    }

    public void addAnotherInputOnClick(View view){
        Log.i(TAG, "Add another clicked");

        /* Add layout input to list */
        LinearLayout ingredientInput = (LinearLayout) getLayoutInflater().inflate(R.layout.item_input_ingredient, linIngredientInputs);

        Spinner spinMeasurements = (Spinner) ingredientInput.findViewById(R.id.spinMeasurements);
        spinMeasurements.setAdapter(spinnerAdapter);
        
        ingredientInputs.add(ingredientInput);
    }
}
