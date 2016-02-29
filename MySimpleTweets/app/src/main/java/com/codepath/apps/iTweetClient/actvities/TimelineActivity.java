package com.codepath.apps.iTweetClient.actvities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.adapters.TweetsAdapter;
import com.codepath.apps.iTweetClient.fragments.HomeTimelineFragment;
import com.codepath.apps.iTweetClient.fragments.MentionsTimelineFragment;
import com.codepath.apps.iTweetClient.fragments.TweetFragment;
import com.codepath.apps.iTweetClient.fragments.TweetsListFragment;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.models.User;
import com.codepath.apps.iTweetClient.utils.Constants;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TimelineActivity extends AppCompatActivity implements TweetsAdapter.OnReplyTweetListener,TweetsAdapter.OnFavoriteListener {


    TweetsListFragment fragmentTweetsList;
    MenuItem miActionProgressItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TwitterFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55acee")));
    }


    private void showComposeTweetFragment(Tweet t) {
        getUserCredentials(t);
    }

    private void getUserCredentials(final Tweet t) {
        TwitterClient client = new TwitterClient(this);
        client.getUserCredentials(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(APP_TAG,"User credentials are here: "+response.toString());
                User currUser = User.fromJSON(response);
                FragmentManager fm = getSupportFragmentManager();
                TweetFragment tweetFragment = TweetFragment.newInstance("Compose Tweet", currUser,t.getScreen_name());
                tweetFragment.setTargetFragment(new HomeTimelineFragment(),300);
                tweetFragment.show(fm, "Compose Tweet");
            }

            @Override
            public void onFailure(int status, Header[] headers, Throwable t, JSONObject obj){
                Log.d(APP_TAG,"Failed to get user credentials"+t.getMessage());
            }
        });
    }


    @Override
    public void onReplyTweet(Tweet t) {
        Log.d(APP_TAG,"Here!");
        showComposeTweetFragment(t);
    }

    @Override
    public void onFavTweet(Tweet t) {
        TwitterClient client = new TwitterClient(this);
        client.favTweet(Long.toString(t.getUid()),!t.getFav(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject response) {
                Tweet newTweet = Tweet.fromJSONObject(response);
                newTweet.save();
                String favTed;
                if(newTweet.getFav())
                    favTed = "Favorited!";
                else
                    favTed = "Un Favorited!";
                Toast.makeText(getApplicationContext(),favTed,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(Constants.APP_TAG, errorResponse.toString());
            }
        });
    }


    public class TwitterFragmentPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "Home Timeline", "Mentions Timeline" };

        public TwitterFragmentPagerAdapter(FragmentManager fm) {
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


    public void onProfileView(MenuItem mi){
        Intent viewProfileIntent = new Intent(this,ProfileActivity.class);
        viewProfileIntent.putExtra("screenName","ipande");
        startActivity(viewProfileIntent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline,menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}
