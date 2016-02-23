package com.codepath.apps.iTweetClient.actvities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.adapters.TweetsAdapter;
import com.codepath.apps.iTweetClient.fragments.HomeTimelineFragment;
import com.codepath.apps.iTweetClient.fragments.MentionsTimelineFragment;
import com.codepath.apps.iTweetClient.fragments.PageFragment;
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



    private User currUser;
//    SwipeRefreshLayout swipeContainer;

    TweetsListFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "Home Timeline", "Mentions Timeline" };

        public SampleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(APP_TAG,"position: "+position);
            switch(position){
                case 0: {
                    return new HomeTimelineFragment();
                }
                case 1: {
                    return new MentionsTimelineFragment();
                }
                default: return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
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
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showComposeTweetFragment();
//            }
//        });

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

//    }

//    private void refreshTimeLine() {
//        tweetsAdapter.clearData();
//        populateTimeline(FIRST_PAGE);
//
//    }

//    private void showComposeTweetFragment() {
//        getUserCredentials();
//    }

//    private void getUserCredentials() {
//        client.getUserCredentials(new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//               Log.d(APP_TAG,"User credentials are here: "+response.toString());
//                currUser = User.fromJSON(response);
//                FragmentManager fm = getSupportFragmentManager();
//                TweetFragment tweetFragment = TweetFragment.newInstance("Compose Tweet", currUser);
//                tweetFragment.show(fm, "Compose Tweet");
//            }
//
//
//            @Override
//            public void onFailure(int status, Header[] headers, Throwable t, JSONObject obj){
//                Log.d(APP_TAG,"Failed to get user credentials"+t.getMessage());
//            }
//        });
//    }






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
