package com.codepath.apps.iTweetClient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.utils.Constants;
import com.codepath.apps.iTweetClient.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class UserTimelineFragment extends TweetsListFragment{
    private TwitterClient client;
    private static long SINCE_ID = 0;
    private String screenName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new TwitterClient(getActivity());
        screenName = getArguments().getString("screenName", null);
        populateTimeline(screenName,0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, parent, savedInstanceState);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                populateTimeline(screenName,totalItemsCount);
            }
        });
        return v;
    }

    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    // Fill in listview with tweet JSON objects
    private void populateTimeline(String screenName, long totalItemsCount) {

        // TODO: add logic for checking internet
        if (Constants.isNetworkAvailable(getActivity())) {
            client.getUserTimeline(screenName, totalItemsCount, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    // Response is automatically parsed into a JSONArray
//                    swipeContainer.setRefreshing(false);
                    ArrayList<Tweet> newTweets = Tweet.fromJson(json);
//                    Log.d(APP_TAG, "Tweets: " + tweets.toString());
                    if (newTweets != null && newTweets.size() > 0) {
                        SINCE_ID = newTweets.size() - 1;
                        Log.d(APP_TAG, "Max ID: " + SINCE_ID);
                    }
                    addAll(newTweets);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(APP_TAG, "Failed" + statusCode + " resp " + responseString + "err: " + throwable.getMessage());
                }
            });
        } else {
            // 1. fetch tweets from DB
            // 2. Populate adapter
            // 3. refresh view
            retrieveTweets(screenName);
        }
    }

    // Search the DB for tweets that have this searchTerm
    private void retrieveTweets(String searchTerm) {
        List<Tweet> savedTweets = new Select().from(Tweet.class)
                .where("screen_name LIKE ?",new String[]{'%' + searchTerm + '%'})
                .orderBy("uid DESC")
                .execute();
        retrieveDBTweets(savedTweets);
    }
}
