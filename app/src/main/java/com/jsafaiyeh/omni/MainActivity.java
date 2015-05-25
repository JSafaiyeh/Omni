package com.jsafaiyeh.omni;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.github.mrengineer13.snackbar.SnackBar;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetui.TweetUi;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.TWITTER_KEY), getString(R.string.TWITTER_SECRET));
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig), new TweetUi());
        setContentView(R.layout.activity_main);

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Intent i = new Intent(getBaseContext(), FeedActivity.class);
                i.putExtra("Twitter AuthToken", result.data.getAuthToken().token);
                i.putExtra("Twitter AuthSecret", result.data.getAuthToken().secret);
                i.putExtra("Twitter UserName", result.data.getUserName());
                i.putExtra("Twitter UserID", result.data.getUserId());
                startActivity(i);
            }

            @Override
            public void failure(TwitterException exception) {
                new SnackBar.Builder(getParent())
                        .withMessage("Twitter Login Failed.")
                        .withBackgroundColorId(R.color.tw__blue_pressed)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


}
