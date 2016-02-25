package com.codepath.apps.iTweetClient.actvities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.fragments.UserTimelineFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Get the screenName
        String screenName = getIntent().getStringExtra("screenName");
        // Create new UserTimelineFragment
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);

        //Display the fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flContainer,userTimelineFragment,"UserTimelineFragment");
        ft.commit();
    }
}
