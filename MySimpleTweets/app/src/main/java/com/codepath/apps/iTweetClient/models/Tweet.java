package com.codepath.apps.iTweetClient.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.codepath.apps.iTweetClient.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

import static com.codepath.apps.iTweetClient.utils.Constants.*;

@Parcel(analyze={Tweet.class})
@Table(name = "Tweets")
public class Tweet extends Model {
    // Define database columns and associated fields
    @Column(name = "userName")
    String userName;
    @Column(name = "userId")
    Long userId;
    @Column(name = "screen_name")
    String screen_name;
    @Column(name = "profileImage")
    String profileImage;
    @Column(name = "body")
    String body;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    Long uid;
    @Column(name = "fav")
    Boolean fav;

    @Column(name = "createdAt")
    String createdAt;

    public Boolean getFav() {
        return fav;
    }

    // Make sure to always define this constructor with no arguments
    public Tweet() {
        super();
    }

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

    public String getBody() {
        return body;
    }

    public Long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // Add a constructor that creates an object from the JSON response
    public Tweet(JSONObject object){
        super();

        try {
            Log.d(APP_TAG,"Tag: "+object.toString());
            this.body = object.getString("text");
            this.uid = object.getLong("id");
            this.createdAt = object.getString("created_at");

            JSONObject user = object.getJSONObject("user");

            this.userName = user.getString("name");
            this.userId = user.getLong("id");
            this.screen_name = user.getString("screen_name");
            this.profileImage = user.getString("profile_image_url");

            this.fav = object.getBoolean("favorited");

            //this.userHandle = object.getString("user_username");
            //this.timestamp = object.getString("timestamp");

        } catch (JSONException e) {
            Log.e(APP_TAG,"Failed in creating Tweet Object" + e.getMessage());
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue; // WHaaaaa?!
            }

            Tweet tweet = new Tweet(tweetJson);
            tweet.save();
            Log.d(APP_TAG,"Added tweet to DB: "+tweet.userId);
            tweets.add(tweet);
        }
        return tweets;
    }

    public static Tweet fromJSONObject(JSONObject response) {
        Tweet tweet = new Tweet(response);
        tweet.save();
        return tweet;
    }
}