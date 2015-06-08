package com.jsafaiyeh.omni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.crashlytics.android.Crashlytics;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetui.TweetUi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

import io.fabric.sdk.android.Fabric;


public class FeedActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Activity mActivity;
    private LinearLayout mLinearLayout;
    private Context mContext;
    private FacebookClient facebookClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.TWITTER_KEY), getString(R.string.TWITTER_SECRET));
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig), new TweetUi());
        setContentView(R.layout.activity_feed);

        mActivity = this;

        final ObservableScrollView observableScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorNormalResId(R.color.colorPrimary);
        fab.setColorPressedResId(R.color.colorPrimaryDark);
        fab.attachToScrollView(observableScrollView);

        observableScrollView.setOnScrollChangedListener(new ObservableScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView scrollView, int i, int i1, int i2, int i3) {

            }
        });

        mLinearLayout = (LinearLayout) findViewById(R.id.feed_linear_layout);
        mContext = this;
        final ArrayList<SocialCustomPost> socialCustomPosts = new ArrayList<>();

        Intent i = getIntent();
        TwitterAuthToken twitterAuthToken = new TwitterAuthToken(i.getStringExtra("Twitter AuthToken"), i.getStringExtra("Twitter AuthSecret"));
        Long userID = i.getLongExtra("Twitter UserID", 1L);
        String userName = i.getStringExtra("Twitter UserName");
        String facebookAccessToken = i.getStringExtra("Facebook AccessToken");
        facebookClient = new DefaultFacebookClient(facebookAccessToken, Version.VERSION_2_3);


        final TwitterSession twitterSession = new TwitterSession(twitterAuthToken, userID, userName);
        Socials.loadFacebookPosts(facebookClient, socialCustomPosts);
        Socials.loadTweets(mContext, mLinearLayout, mActivity, socialCustomPosts, twitterSession);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<SocialCustomPost> socialCustomPostArrayList = new ArrayList<>();
                Socials.loadFacebookPosts(facebookClient, socialCustomPostArrayList);
                Socials.loadTweets(mContext, mLinearLayout, mActivity, socialCustomPostArrayList, twitterSession);
                observableScrollView.smoothScrollTo(0, 0);
                fab.setVisibility(View.INVISIBLE);
                fab.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab.setVisibility(View.VISIBLE);
                    }
                }, 8000);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

}
