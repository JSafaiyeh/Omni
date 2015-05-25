package com.jsafaiyeh.omni;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.github.mrengineer13.snackbar.SnackBar;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FeedActivity extends AppCompatActivity {

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mLinearLayout = (LinearLayout) findViewById(R.id.feed_linear_layout);

        Intent i = getIntent();
        TwitterAuthToken twitterAuthToken = new TwitterAuthToken(i.getStringExtra("Twitter AuthToken"), i.getStringExtra("Twitter AuthSecret"));
        Long userID = i.getLongExtra("Twitter UserID", 1L);
        String userName = i.getStringExtra("Twitter UserName");

        TwitterSession twitterSession = new TwitterSession(twitterAuthToken, userID, userName);
        Socials.loadTweets(getBaseContext(), mLinearLayout, getParent(), twitterSession);
    }
}
