package com.codepath.apps.iTweetClient.actvities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.adapters.TweetsAdapter;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TimelineActivity extends AppCompatActivity {
    private TwitterClient client;

    private ArrayList<Tweet> tweets;

    @Nullable @Bind(R.id.rvTweets) RecyclerView rvTweets;
    private TweetsAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        client = new TwitterClient(getApplicationContext());
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(tweets,getApplicationContext());

        rvTweets.setAdapter(tweetsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rvTweets.setLayoutManager(linearLayoutManager);
        populateTimeline(0);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                populateTimeline(page);
            }
        });
    }

    // Fill in listview with tweet JSON objects
    private void populateTimeline(int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
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
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(APP_TAG,"Failed"+statusCode+" resp "+responseString + "err: "+throwable.getMessage());
            }
        });

    }
}
