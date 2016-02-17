package com.codepath.apps.iTweetClient.actvities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.adapters.TweetsAdapter;
import com.codepath.apps.iTweetClient.adapters.TweetsArrayAdapter;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
//    private ListView lvTweets;
    private RecyclerView rvTweets;
    private TweetsAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = new TwitterClient(getApplicationContext());

//        lvTweets = (ListView) findViewById(R.id.lvTweets);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
//        aTweets = new TweetsArrayAdapter(this,tweets);
        tweetsAdapter = new TweetsAdapter(tweets,getApplicationContext());

        rvTweets.setAdapter(tweetsAdapter);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

//        lvTweets.setAdapter(aTweets);


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
                ArrayList<Tweet> newTweets = Tweet.fromJson(json);
                Log.d(APP_TAG,"Tweets: "+tweets.toString());

                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();
//                aTweets.addAll(tweets);
//                aTweets.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(APP_TAG,"Failed"+statusCode+" resp "+responseString + "err: "+throwable.getMessage());
            }
        });

    }
}
