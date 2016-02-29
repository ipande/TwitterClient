package com.codepath.apps.iTweetClient.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class HomeTimelineFragment extends TweetsListFragment{

    private TwitterClient client;
    private static long MAX_ID = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new TwitterClient(getActivity());
        populateTimeline(0);

    }

    public void refreshTimeLine(){
        clearData();
        MAX_ID = 1;
        populateTimeline(MAX_ID);
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
                populateTimeline(MAX_ID);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeLine();
            }
        });
        return v;
    }

    // Fill in listview with tweet JSON objects
    private void populateTimeline(long page) {

        if(Constants.isNetworkAvailable(getActivity())) {
            showProgressBar();
            client.getHomeTimeline(page, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    // Response is automatically parsed into a JSONArray
                    swipeContainer.setRefreshing(false);
                    ArrayList<Tweet> newTweets = Tweet.fromJson(json);
//                    Log.d(APP_TAG, "Tweets: " + tweets.toString());
                    if (newTweets != null && newTweets.size() > 0) {
                        MAX_ID = newTweets.get(newTweets.size() - 1).getUid();
                        Log.d(APP_TAG, "Max ID: " + MAX_ID);
                    }
                    addAll(newTweets);
                    hideProgressBar();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(APP_TAG, "Failed" + statusCode + " resp " + responseString + "err: " + throwable.getMessage());
                    hideProgressBar();
                }
            });
        }
        else{
            // 1. fetch tweets from DB
            // 2. Populate adapter
            // 3. refresh view
            retrieveTweets();
        }
    }

    private void retrieveTweets() {
        List<Tweet> savedTweets = new Select().from(Tweet.class)
                .orderBy("uid DESC")
                .execute();
        retrieveDBTweets(savedTweets);
    }

}
