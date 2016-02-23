package com.codepath.apps.iTweetClient.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.utils.Constants;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private static long MAX_ID = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new TwitterClient(getActivity());
        populateTimeline(0);
    }

    // Fill in listview with tweet JSON objects
    private void populateTimeline(long page) {

        // TODO: add logic for checking internet
        if (Constants.isNetworkAvailable(getActivity())) {
            client.getMentionsTimeline(page, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    // Response is automatically parsed into a JSONArray
                    //json.getJSONObject(0).getLong("id");
//                    swipeContainer.setRefreshing(false);
                    ArrayList<Tweet> newTweets = Tweet.fromJson(json);
//                    Log.d(APP_TAG, "Tweets: " + tweets.toString());
                    if (newTweets != null && newTweets.size() > 0) {
                        MAX_ID = newTweets.get(newTweets.size() - 1).getUid();
                        Log.d(APP_TAG, "Max ID: " + MAX_ID);
                    }
                    addAll(newTweets);
//                    tweetsAdapter.notifyDataSetChanged();
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
//            retrieveDBTweets();
        }
    }

}
