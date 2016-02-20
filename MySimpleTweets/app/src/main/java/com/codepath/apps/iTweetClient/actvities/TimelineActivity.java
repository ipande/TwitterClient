package com.codepath.apps.iTweetClient.actvities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.adapters.TweetsAdapter;
import com.codepath.apps.iTweetClient.fragments.TweetFragment;
import com.codepath.apps.iTweetClient.fragments.TweetFragment.TweetFragmentDialogListener;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.*;
import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TimelineActivity extends AppCompatActivity implements TweetFragmentDialogListener {
    private TwitterClient client;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private ArrayList<Tweet> tweets;

    @Nullable @Bind(R.id.rvTweets) RecyclerView rvTweets;
    private TweetsAdapter tweetsAdapter;
    private static long MAX_ID = 1;

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
                Log.d(APP_TAG,"Loading tweets from page: "+page);
                populateTimeline(MAX_ID);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeTweetFragment();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeLine();
            }
        });

    }

    private void refreshTimeLine() {
        tweetsAdapter.clearData();
        populateTimeline(FIRST_PAGE);

    }

    private void showComposeTweetFragment() {
        FragmentManager fm = getSupportFragmentManager();
        TweetFragment tweetFragment = TweetFragment.newInstance("Compose Tweet");
        tweetFragment.show(fm, "Compose Tweet");
    }


    // Fill in listview with tweet JSON objects
    private void populateTimeline(long page) {
//        if(page == 0)
//            tweetsAdapter.clearData();

        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // Response is automatically parsed into a JSONArray
                //json.getJSONObject(0).getLong("id");
                swipeContainer.setRefreshing(false);
                ArrayList<Tweet> newTweets = Tweet.fromJson(json);
                Log.d(APP_TAG,"Tweets: "+tweets.toString());
                if(newTweets!=null && newTweets.size() > 0) {
                    MAX_ID = newTweets.get(newTweets.size() - 1).getUid();
                    Log.d(APP_TAG,"Max ID: "+MAX_ID);
                }
                tweets.addAll(newTweets);
                tweetsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(APP_TAG,"Failed"+statusCode+" resp "+responseString + "err: "+throwable.getMessage());
            }
        });

    }

    @Override
    public void onFinishTweetingDialog(Tweet newTweet) {
        if(newTweet!=null){
            tweets.add(0,newTweet);
            tweetsAdapter.notifyItemChanged(0);
            populateTimeline(0);
        }
        else{
            Log.d(APP_TAG,"There was an error posting your tweet");
        }
    }
}
