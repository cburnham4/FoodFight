package letshangllc.foodfight.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlburnham on 5/28/17.
 */

@IgnoreExtraProperties
public class UserPost {

    public String uid, mealName, downloadUrl;
    public ArrayList<String> ingredients, instructions;
    public int score = 0;


    public UserPost() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public UserPost(String uid, String mealName, ArrayList ingredients, ArrayList instructions, String downloadUrl, int score) {
        this.uid = uid;
        this.mealName = mealName;
        this.downloadUrl = downloadUrl;
        this.score = score;

        /* TODO: Allow for real ingredients */
        this.ingredients = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("mealName", mealName);
        result.put("ingredients", ingredients);
        result.put("instructions", instructions);
        result.put("downloadUrl", downloadUrl);
        result.put("score", score);

        return result;
    }

}