package com.codepath.apps.iTweetClient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.codepath.apps.iTweetClient.R;

/**
 * Created by ishanpande on 2/16/16.
 */
public class Constants {
    public static final String APP_TAG = "iTweetClient";
    public final static int TWEET_LENGTH = 140;
    public static long MAXID = 1;
    public static final int FIRST_PAGE = 1;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isNWAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if (!isNWAvailable)
            Toast.makeText(context, "No Network available", Toast.LENGTH_SHORT).show();
        return isNWAvailable;
    }
}
