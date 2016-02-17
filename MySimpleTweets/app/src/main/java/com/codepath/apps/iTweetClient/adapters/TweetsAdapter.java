package com.codepath.apps.iTweetClient.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.utils.Constants;
import com.codepath.apps.iTweetClient.utils.ParseRelativeDate;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvUserName;
        public TextView tvScreenName;
        public TextView tvBody;
        public ImageView ivProfileImage;
        public TextView tvTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvName);
            tvTimestamp = (TextView) itemView.findViewById(R.id.tvTimestamp);

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
    public void onBindViewHolder(TweetsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Tweet tweet = tweets.get(position);

        // Set item views based on the data model
        TextView tvUserName = viewHolder.tvUserName;
        tvUserName.setText(tweet.getUserName());

        TextView tvScreenName = viewHolder.tvScreenName;
        tvScreenName.setText(tweet.getScreen_name());

        TextView tvBody = viewHolder.tvBody;
        Log.d(Constants.APP_TAG,"setting tvBody to: "+tweet.getBody());
        tvBody.setText(tweet.getBody());

        TextView tvTimestamp = viewHolder.tvTimestamp;
        tvTimestamp.setText(ParseRelativeDate.getRelativeTimeAgo(tweet.getCreatedAt()));

        ImageView ivProfileImg = viewHolder.ivProfileImage;
        ivProfileImg.setImageResource(0);

        Picasso.with(mContext).load(tweet.getProfileImage()).fit().into(ivProfileImg);
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
