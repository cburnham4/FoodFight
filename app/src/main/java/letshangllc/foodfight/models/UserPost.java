package letshangllc.foodfight.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by carlburnham on 5/28/17.
 */

@IgnoreExtraProperties
public class UserPost implements Parcelable{

    public String uid, mealName, downloadUrl, key;
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

    protected UserPost(Parcel in) {
        uid = in.readString();
        mealName = in.readString();
        downloadUrl = in.readString();
        key = in.readString();
        ingredients = in.createStringArrayList();
        instructions = in.createStringArrayList();
        score = in.readInt();
    }

    public static final Creator<UserPost> CREATOR = new Creator<UserPost>() {
        @Override
        public UserPost createFromParcel(Parcel in) {
            return new UserPost(in);
        }

        @Override
        public UserPost[] newArray(int size) {
            return new UserPost[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put(DatabaseConstants.USER_ID, uid);
        result.put(DatabaseConstants.MEAL_NAME, mealName);
        result.put("ingredients", ingredients);
        result.put("instructions", instructions);
        result.put(DatabaseConstants.DOWNLOAD_URL, downloadUrl);
        result.put(DatabaseConstants.SCORE, score);

        return result;
    }

    public void setKey(String key){
        this.key = key;
    }

    @Override
    public String toString() {
        return mealName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(mealName);
        dest.writeString(downloadUrl);
        dest.writeString(key);
        dest.writeStringList(ingredients);
        dest.writeStringList(instructions);
        dest.writeInt(score);
    }
}