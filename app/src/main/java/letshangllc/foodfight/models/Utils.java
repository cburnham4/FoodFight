package letshangllc.foodfight.models;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import letshangllc.foodfight.R;

/**
 * Created by carlburnham on 5/28/17.
 */

public class Utils {

    private static final String TAG = "Utils";


    public static void makeToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static String intToString(int value){
        return String.format(Locale.getDefault(), "%d", value);
    }
}