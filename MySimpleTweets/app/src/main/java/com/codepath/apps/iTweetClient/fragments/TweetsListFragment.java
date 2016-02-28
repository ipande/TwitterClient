package com.codepath.apps.iTweetClient.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.actvities.TweetDetailActivity;
import com.codepath.apps.iTweetClient.adapters.TweetsAdapter;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.models.User;
import com.codepath.apps.iTweetClient.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.iTweetClient.utils.ItemClickSupport;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TweetsListFragment extends Fragment implements TweetFragment.TweetFragmentDialogListener {
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Nullable
    @Bind(R.id.rvTweets) RecyclerView rvTweets;
    private TweetsAdapter tweetsAdapter;
    ArrayList<Tweet> tweets;
    private ItemClickSupport itemClick;
    protected LinearLayoutManager linearLayoutManager;

    @Override
    public void onFinishTweetingDialog(Tweet newTweet) {
        if(newTweet!=null){
            tweets.add(0,newTweet);
            tweetsAdapter.notifyItemChanged(0);
            swipeContainer.setRefreshing(true);
            linearLayoutManager.scrollToPosition(0);
            swipeContainer.setRefreshing(false);
        }
        else{
            Log.d(APP_TAG,"There was an error posting your tweet");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        ButterKnife.bind(this, v);
        rvTweets.setAdapter(tweetsAdapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);

        itemClick = ItemClickSupport.addTo(rvTweets);

        itemClick.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.d(APP_TAG,"View clicked: "+v.getId()  );
                if(v.getId() != R.id.ivProfileImage) {
                    Tweet tweet = tweets.get(position);
                    // create an intent to display the article
                    Intent tweetDetailIntent = new Intent(getActivity(), TweetDetailActivity.class);
                    tweetDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // pass in that article into the intent
                    tweetDetailIntent.putExtra("tweet", Parcels.wrap(tweet));
                    getActivity().startActivity(tweetDetailIntent);
                }
                else{
                    Log.d(APP_TAG,"Profile pic clicked!");
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
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
        return v;
    }


    private void showComposeTweetFragment() {
        getUserCredentials();
    }

    private void getUserCredentials() {
        TwitterClient client = new TwitterClient(getActivity());
        client.getUserCredentials(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(APP_TAG,"User credentials are here: "+response.toString());
                User currUser = User.fromJSON(response);
                FragmentManager fm = getFragmentManager();
                TweetFragment tweetFragment = TweetFragment.newInstance("Compose Tweet", currUser);
                tweetFragment.setTargetFragment(TweetsListFragment.this,300);
                tweetFragment.show(fm, "Compose Tweet");
            }

            @Override
            public void onFailure(int status, Header[] headers, Throwable t, JSONObject obj){
                Log.d(APP_TAG,"Failed to get user credentials"+t.getMessage());
            }
        });
    }

    @Override
    // Create lifecycle event
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(tweets, getActivity());


    }

    public void addAll(ArrayList<Tweet> newTweets) {
        tweets.addAll(newTweets);
        tweetsAdapter.notifyDataSetChanged();
    }

    public void clearData(){
        tweetsAdapter.clearData();
        tweetsAdapter.notifyDataSetChanged();
    }

    protected void retrieveDBTweets(List<Tweet> savedTweets) {
        Toast.makeText(getActivity(), "Retrieving tweets from DB", Toast.LENGTH_LONG).show();
        tweetsAdapter.clearData();
        tweets.addAll(savedTweets);
        tweetsAdapter.notifyDataSetChanged();
//        swipeContainer.setRefreshing(false);
    }

}
