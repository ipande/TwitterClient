package com.codepath.apps.iTweetClient.actvities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = new TwitterClient(getApplicationContext());

        populateTimeline();

    }

    // Fill in listview with tweet JSON objects
    private void populateTimeline() {
        client.getHomeTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // Response is automatically parsed into a JSONArray
                //json.getJSONObject(0).getLong("id");
                Log.d(APP_TAG,"Here");
                Log.d("TwitterClient","Are you serious?"+json.toString());
                ArrayList<Tweet> tweets = Tweet.fromJson(json);
                Log.d(APP_TAG,"Tweets: "+tweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(APP_TAG,"Failed"+statusCode+" resp "+responseString + "err: "+throwable.getMessage());
            }
        });

    }
}
