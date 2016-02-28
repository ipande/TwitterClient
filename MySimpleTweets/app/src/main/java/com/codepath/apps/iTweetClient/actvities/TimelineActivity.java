package com.codepath.apps.iTweetClient.actvities;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.fragments.HomeTimelineFragment;
import com.codepath.apps.iTweetClient.fragments.MentionsTimelineFragment;
import com.codepath.apps.iTweetClient.fragments.TweetFragment;
import com.codepath.apps.iTweetClient.fragments.TweetsListFragment;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TimelineActivity extends AppCompatActivity {


    TweetsListFragment fragmentTweetsList;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }




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


}
