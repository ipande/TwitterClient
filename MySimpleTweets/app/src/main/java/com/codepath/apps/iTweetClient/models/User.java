package com.codepath.apps.iTweetClient.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

@Table(name = "User")
public class User extends Model{
    @Column(name = "userName")
    String userName;
    @Column(name = "userId")
    Long userId;
    @Column(name = "screen_name")
    String screen_name;
    @Column(name = "profileImage")
    String profileImage;

    public String getUserName() {
        return userName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public User(){
        super();
    }

    // Add a constructor that creates an object from the JSON response
    public User(JSONObject user){
        super();

        try {
            this.userName = user.getString("name");
            this.userId = user.getLong("id");
            this.screen_name = user.getString("screen_name");
            this.profileImage = user.getString("profile_image_url");

        }catch (JSONException e) {
            Log.e(APP_TAG,"Failed in creating Tweet Object" + e.getMessage());
        }
    }

    public static User fromJSON(JSONObject user){
        User u = new User(user);
        u.save();
        return u;
    }

}
