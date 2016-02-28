package com.codepath.apps.iTweetClient.actvities;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.fragments.UserTimelineFragment;
import com.codepath.apps.iTweetClient.models.User;
import com.codepath.apps.iTweetClient.utils.Constants;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;

public class ProfileActivity extends AppCompatActivity {
    @Bind(R.id.tvFollowers) TextView tvFollowers;
    @Bind(R.id.tvFollowing) TextView tvFollowing;
    @Bind(R.id.ivProfileImage) ImageView ivProfileImage;
    @Bind(R.id.tvName) TextView tvName;
    @Bind(R.id.tvTagline) TextView tvTagline;

    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);



        // Get the screenName
        String screenName = getIntent().getStringExtra("screenName");

        client = new TwitterClient(this);
        if(Constants.isNetworkAvailable(this)) {
            client.getUser(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(APP_TAG, "User credentials are here: " + response.toString());
                    User currUser = User.fromJSON(response);
                    getSupportActionBar().setTitle("@ " + currUser.getScreen_name());
                    populateUserInfo(currUser);
                }


                @Override
                public void onFailure(int status, Header[] headers, Throwable t, JSONObject obj) {
                    Log.d(APP_TAG, "Failed to get user credentials" + t.getMessage());
                }

            });
        }

        // Create new UserTimelineFragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
        if(savedInstanceState == null) {
            //Display the fragment
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment, "UserTimelineFragment");
            ft.commit();
        }
    }

    private void populateUserInfo(User user) {
        Picasso.with(getApplicationContext()).load(user.getProfileImage()).fit().into(ivProfileImage);
        tvName.setText(user.getUserName());
        tvFollowers.setText(String.valueOf(user.getFollowers()) + " Followers");
        tvFollowing.setText(String.valueOf(user.getFollowing()) + " Following");
        tvTagline.setText(user.getTagLine());
    }
}
