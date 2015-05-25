package com.jsafaiyeh.omni;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetui.TweetUi;

import io.fabric.sdk.android.Fabric;


public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.TWITTER_KEY), getString(R.string.TWITTER_SECRET));
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig), new TweetUi());
        setContentView(R.layout.activity_feed);

        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.feed_linear_layout);
        Context mContext = this;

        Intent i = getIntent();
        TwitterAuthToken twitterAuthToken = new TwitterAuthToken(i.getStringExtra("Twitter AuthToken"), i.getStringExtra("Twitter AuthSecret"));
        Long userID = i.getLongExtra("Twitter UserID", 1L);
        String userName = i.getStringExtra("Twitter UserName");

        TwitterSession twitterSession = new TwitterSession(twitterAuthToken, userID, userName);
        Socials.loadTweets(mContext, mLinearLayout, getParent(), twitterSession);
    }

}
