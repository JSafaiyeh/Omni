package com.jsafaiyeh.omni;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "WVjkb954bIEDdxCzDgO6lUJGo";
    private static final String TWITTER_SECRET = "69Pkdo6QuML5EuekI9orwotv2U50c8oq8rGv1jqXuAw4RbEUbH";
    private TwitterLoginButton loginButton;
    private LinearLayout mLinearLayout;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig), new TweetUi());
        setContentView(R.layout.activity_main);
        mContext = this;

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls

                mLinearLayout = (LinearLayout) findViewById(R.id.main_linear_view);
                mLinearLayout.removeView(loginButton);

                TwitterApiClient twitterApiClient = new TwitterApiClient(result.data);
                twitterApiClient.getStatusesService().homeTimeline(35, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        final List<Long> tweetIds = new ArrayList<>();
                        for (Tweet t : result.data) {
                            tweetIds.add(t.getId());
                        }

                        TweetUtils.loadTweets(tweetIds, new LoadCallback<List<Tweet>>() {
                            int i = 0;

                            @Override
                            public void success(List<Tweet> tweets) {
                                for (Tweet tweet : tweets) {
                                    View view = new TweetView(mContext, tweet);
                                    if (i != 0)
                                        view.setPadding(0, 50, 0, 0);
                                    mLinearLayout.addView(view);
                                    i++;
                                }
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                // Toast.makeText(...).show();
                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException e) {

                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


}
