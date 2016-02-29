package com.codepath.apps.iTweetClient.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.actvities.ProfileActivity;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.utils.Constants;
import com.codepath.apps.iTweetClient.utils.ParseRelativeDate;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public interface OnReplyTweetListener {
        void onReplyTweet(Tweet t);
    }

    public interface OnFavoriteListener {
        void onFavTweet(Tweet t);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tvUserName) TextView tvUserName;
        @Bind(R.id.tvName) TextView tvScreenName;
        @Bind(R.id.tvBody) TextView tvBody;
        @Bind(R.id.tvTimestamp) TextView tvTimestamp;
        @Bind(R.id.ivProfileImage) ImageView ivProfileImage;
        @Bind(R.id.ivReply) ImageView ivReply;
        @Bind(R.id.ivFav) ImageView ivFav;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    } // End of ViewHolder class


    private List<Tweet> tweets;
    private Context mContext;

    public TweetsAdapter(List<Tweet> tweetsList,Context context){
        this.tweets = tweetsList;
        this.mContext = context;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TweetsAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Tweet tweet = tweets.get(position);

        // Set item views based on the data model
        TextView tvUserName = viewHolder.tvUserName;
        tvUserName.setText(tweet.getUserName());

        TextView tvScreenName = viewHolder.tvScreenName;
        tvScreenName.setText(tweet.getScreen_name());

        TextView tvBody = viewHolder.tvBody;
//        Log.d(Constants.APP_TAG,"setting tvBody to: "+tweet.getBody());
        tvBody.setText(tweet.getBody());

        TextView tvTimestamp = viewHolder.tvTimestamp;
        tvTimestamp.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));

        ImageView ivProfileImg = viewHolder.ivProfileImage;
        ivProfileImg.setImageResource(0);



        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constants.APP_TAG,"Image clicked!");
                Tweet clicked = tweets.get(position);
                Log.d(Constants.APP_TAG,"profile: "+clicked.getScreen_name());
                Intent viewProfileIntent = new Intent(mContext,ProfileActivity.class);
                viewProfileIntent.putExtra("screenName",clicked.getScreen_name());
                viewProfileIntent.putExtra("tweet",clicked.getUserName());
                mContext.startActivity(viewProfileIntent);

            }
        });

        Picasso.with(mContext).load(tweet.getProfileImage()).fit().into(ivProfileImg);

        final ImageView ivFavo = viewHolder.ivFav;
        if(tweet.getFav())
            ivFavo.setImageResource(R.drawable.ic_fav_on);
        else
            ivFavo.setImageResource(R.drawable.ic_fav);

        ivFavo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tweet clicked = tweets.get(position);
                if(clicked.getFav())
                    ivFavo.setImageResource(R.drawable.ic_fav);
                else
                    ivFavo.setImageResource(R.drawable.ic_fav_on);

                OnFavoriteListener listener = (OnFavoriteListener) mContext;
                listener.onFavTweet(clicked);
            }
        });

        ImageView ivReply = viewHolder.ivReply;
        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constants.APP_TAG,"Reply clicked!");
                Tweet clicked = tweets.get(position);
                Log.d(Constants.APP_TAG,"profile: "+clicked.getScreen_name());
                OnReplyTweetListener listener = (OnReplyTweetListener) mContext;
                listener.onReplyTweet(clicked);
            }
        });



    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clearData() {
        if(tweets!=null) {
            tweets.clear();
            this.notifyDataSetChanged();
        }
    }
}
