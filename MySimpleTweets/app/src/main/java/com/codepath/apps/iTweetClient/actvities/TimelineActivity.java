package com.codepath.apps.iTweetClient.actvities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.adapters.TweetsAdapter;
import com.codepath.apps.iTweetClient.fragments.TweetFragment;
import com.codepath.apps.iTweetClient.fragments.TweetFragment.TweetFragmentDialogListener;
import com.codepath.apps.iTweetClient.fragments.TweetsListFragment;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.models.User;
import com.codepath.apps.iTweetClient.utils.Constants;
import com.codepath.apps.iTweetClient.utils.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.*;
import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TimelineActivity extends AppCompatActivity {//implements TweetFragmentDialogListener {
    private TwitterClient client;
    private static long MAX_ID = 1;

    private User currUser;
//    SwipeRefreshLayout swipeContainer;

    TweetsListFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        client = new TwitterClient(getApplicationContext());
        populateTimeline(0);

        if(savedInstanceState == null) {
            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        }


//        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                Log.d(APP_TAG,"Loading tweets from page: "+page);
//                populateTimeline(MAX_ID);
//            }
//        });
//
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComposeTweetFragment();
            }
        });

//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshTimeLine();
//            }
//        });

    }

//    private void refreshTimeLine() {
//        tweetsAdapter.clearData();
//        populateTimeline(FIRST_PAGE);
//
//    }

    private void showComposeTweetFragment() {
        getUserCredentials();
    }

    private void getUserCredentials() {
        client.getUserCredentials(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               Log.d(APP_TAG,"User credentials are here: "+response.toString());
                currUser = User.fromJSON(response);
                FragmentManager fm = getSupportFragmentManager();
                TweetFragment tweetFragment = TweetFragment.newInstance("Compose Tweet", currUser);
                tweetFragment.show(fm, "Compose Tweet");
            }


            @Override
            public void onFailure(int status, Header[] headers, Throwable t, JSONObject obj){
                Log.d(APP_TAG,"Failed to get user credentials"+t.getMessage());
            }
        });
    }


    // Fill in listview with tweet JSON objects
    private void populateTimeline(long page) {

        // TODO: add logic for checking internet
        if(Constants.isNetworkAvailable(getApplicationContext())) {
            client.getHomeTimeline(page, new JsonHttpResponseHandler() {
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
                    fragmentTweetsList.addAll(newTweets);
//                    tweetsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(APP_TAG, "Failed" + statusCode + " resp " + responseString + "err: " + throwable.getMessage());
                }
            });
        }
        else{
            // 1. fetch tweets from DB
            // 2. Populate adapter
            // 3. refresh view
//            retrieveDBTweets();
        }
    }

//    private void retrieveDBTweets() {
//        Toast.makeText(getApplicationContext(),"Retrieving tweets from DB",Toast.LENGTH_LONG).show();
//        tweetsAdapter.clearData();
//        List<Tweet> savedTweets = new Select().from(Tweet.class)
//                .orderBy("uid DESC")
//                .execute();
//        tweets.addAll(savedTweets);
//        tweetsAdapter.notifyDataSetChanged();
//        swipeContainer.setRefreshing(false);
//    }

//    @Override
//    public void onFinishTweetingDialog(Tweet newTweet) {
//        if(newTweet!=null){
//            tweets.add(0,newTweet);
////            tweetsAdapter.notifyItemChanged(0);
//            tweetsAdapter.notifyDataSetChanged();
////            swipeContainer.setRefreshing(true);
////            refreshTimeLine();
//        }
//        else{
//            Log.d(APP_TAG,"There was an error posting your tweet");
//        }
//    }
}
