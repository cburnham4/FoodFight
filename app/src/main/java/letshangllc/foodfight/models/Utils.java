package letshangllc.foodfight.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import letshangllc.foodfight.R;

/**
 * Created by carlburnham on 5/28/17.
 */

public class Utils {

    private static final String TAG = "Utils";

    public static List<Meal> loadMeals(Context context){
        try{
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONArray array = new JSONArray(loadJSONFromAsset(context));
            List<Meal> mealList = new ArrayList<>();
            for(int i=0;i<array.length();i++){
                Meal meal = gson.fromJson(array.getString(i), Meal.class);
                mealList.add(meal);
            }
            return mealList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String loadJSONFromAsset(Context context) {
        String json = null;
        InputStream is=null;
        try {
            AssetManager manager = context.getAssets();

            is = context.getResources().openRawResource(R.raw.profiles);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}