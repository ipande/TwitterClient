package com.codepath.apps.iTweetClient.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.iTweetClient.R;
import com.codepath.apps.iTweetClient.TwitterClient;
import com.codepath.apps.iTweetClient.models.Tweet;
import com.codepath.apps.iTweetClient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.codepath.apps.iTweetClient.utils.Constants.APP_TAG;
import static com.codepath.apps.iTweetClient.utils.Constants.TWEET_LENGTH;

/**
 * A simple {@link Fragment} subclass.
 */
public class TweetFragment extends DialogFragment {
    @Bind(R.id.etTweet) EditText mTweetText;
    @Bind(R.id.tvChars) TextView tvCharsLeft;
    @Bind(R.id.btComposeTweet) Button btTweet;
    @Bind(R.id.ibCancel) ImageButton btCancel;
    @Bind(R.id.tvScreenName) TextView tvScreenName;
    @Bind(R.id.tvUserName) TextView tvUserName;
    @Bind(R.id.ivProfilePic) ImageView ivProfilePic;

    int textColor;
    private TwitterClient client;
    private Context mContext;
    private boolean tweetingSuccessfull = false;
    private Tweet newTweet;
    private static User currUser;

    public TweetFragment() {
        // Required empty public constructor
    }

    // Defines the listener interface
    public interface TweetFragmentDialogListener {
        void onFinishTweetingDialog(Tweet newTweet);
    }


    public static TweetFragment newInstance(String title, User user,String mention) {
        TweetFragment frag = new TweetFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("mention",mention);
        frag.setArguments(args);
        currUser = user;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        ButterKnife.bind(this,view);
        client = new TwitterClient(view.getContext());
        return view;
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // Butter Knife has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        tweetingSuccessfull = false;
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        String mention = getArguments().getString("mention", null);
        getDialog().setTitle(title);
        mContext = view.getContext();

        textColor = tvCharsLeft.getCurrentTextColor();
        mTweetText.addTextChangedListener(textWatcher);
        if(mention!=null) {
            getDialog().setTitle("Reply to Tweet");
            mTweetText.setText("@" + mention);
        }

        Picasso.with(getContext()).load(currUser.getProfileImage()).fit().into(ivProfilePic);
        tvScreenName.setText(currUser.getScreen_name());
        tvUserName.setText(currUser.getUserName());

        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTweet(mTweetText.getText().toString());
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void postTweet(String tweetText){
        if(tweetText!=null){
            client.postTweet(tweetText,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(APP_TAG,"JSON Object response");
                    Toast.makeText(mContext,"Tweeted successfully",Toast.LENGTH_SHORT).show();
                    tweetingSuccessfull= true;
                    newTweet = Tweet.fromJSONObject(response);
                    TweetFragmentDialogListener listener = (TweetFragmentDialogListener) getTargetFragment();
                    if(listener!=null)
                        listener.onFinishTweetingDialog(newTweet);
//                    else
//                        listener = getActivity().getSupportFragmentManager().
                    dismiss();
                }


                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d(APP_TAG,"Failed to tweet: "+statusCode + " err: "+throwable.getMessage());
                    Toast.makeText(mContext,"Failed to tweet",Toast.LENGTH_SHORT).show();
                    tweetingSuccessfull = false;
                }
            });
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int charsRemaining = TWEET_LENGTH - s.length();
            tvCharsLeft.setText(Integer.toString(charsRemaining));

            if (charsRemaining >= 0 && charsRemaining < TWEET_LENGTH) {
                btTweet.setEnabled(true);
                tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            } else {
                btTweet.setEnabled(false);
                if (charsRemaining < 0)
                    tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                tweetingSuccessfull = false;
            }
        }
    };
}
