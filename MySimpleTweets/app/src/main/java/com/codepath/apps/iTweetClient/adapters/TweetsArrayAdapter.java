package com.codepath.apps.iTweetClient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ishanpande on 2/17/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0,tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. get view
        Tweet tweet = getItem(position);
        // 2. find andinflate the template
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);

        tvUserName.setText(tweet.getUserName());
        tvBody.setText(tweet.getBody());
        tvName.setText(tweet.getScreen_name());

        ivProfileImage.setImageResource(android.R.color.transparent); // clear out an old image for recycled view
        Picasso.with(getContext()).load(tweet.getProfileImage()).fit().into(ivProfileImage);
        return convertView;

     }
}
