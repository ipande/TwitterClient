package com.codepath.apps.iTweetClient.fragments;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.iTweetClient.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TweetFragment extends DialogFragment {
    @Bind(R.id.etTweet) EditText mTweetText;
    @Bind(R.id.tvChars) TextView tvCharsLeft;
    @Bind(R.id.btComposeTweet) Button btTweet;
    final static int TWEET_LENGTH = 140;
    int textColor;


    public TweetFragment() {
        // Required empty public constructor
    }

    public static TweetFragment newInstance(String title) {
        TweetFragment frag = new TweetFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        ButterKnife.bind(this,view);
        return view;
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // Butter Knife has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        mTweetText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        textColor = tvCharsLeft.getCurrentTextColor();
        mTweetText.addTextChangedListener(textWatcher);

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
//                btnTweet.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            } else {
                btTweet.setEnabled(false);
//                btnTweet.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if (charsRemaining < 0)
                    tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }
    };
}
