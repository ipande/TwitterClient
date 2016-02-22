package com.codepath.apps.iTweetClient.actvities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.utils.Constants;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TweetDetailActivity extends AppCompatActivity {
    @Bind(R.id.tvUserName) TextView tvUserName;
    @Bind(R.id.tvScreenName) TextView tvScreenName;
    @Bind(R.id.ivProfilePic) ImageView ivProfilePic;
    @Bind(R.id.ibCancel) ImageButton btCancel;
    @Bind(R.id.tvTweetBody) TextView tvTweetBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        ButterKnife.bind(this);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        Log.d(Constants.APP_TAG,"Tweet: "+tweet.toString());

        Picasso.with(this).load(tweet.getProfileImage()).fit().into(ivProfilePic);

        tvUserName.setText(tweet.getUserName());
        tvScreenName.setText(tweet.getScreen_name());
        tvTweetBody.setText(tweet.getBody());

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
