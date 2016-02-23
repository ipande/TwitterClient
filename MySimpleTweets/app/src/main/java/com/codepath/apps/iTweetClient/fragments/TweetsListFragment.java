package com.codepath.apps.iTweetClient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.adapters.TweetsAdapter;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class TweetsListFragment extends Fragment {
//    @Bind(R.id.swipeContainer)

    @Nullable @Bind(R.id.rvTweets)
    RecyclerView rvTweets;
    private TweetsAdapter tweetsAdapter;
    ArrayList<Tweet> tweets;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list,parent,false);
        ButterKnife.bind(this,v);
        rvTweets.setAdapter(tweetsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);
        return v;
    }

    @Override
    // Create lifecycle event
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(tweets,getActivity());

    }

    public void addAll(ArrayList<Tweet> newTweets){
        tweets.addAll(newTweets);
        tweetsAdapter.notifyDataSetChanged();
    }
}
