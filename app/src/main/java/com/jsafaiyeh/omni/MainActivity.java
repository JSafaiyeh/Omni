package com.jsafaiyeh.omni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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


public class MainActivity extends Activity {

    private TwitterLoginButton mTwitterLoginButton;
    private LoginButton mFacebookLoginButton;
    private CallbackManager callbackManager;
    private TextView mTextView;
    private Activity mActivity;
    private boolean signedWithOne = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.TWITTER_KEY), getString(R.string.TWITTER_SECRET));
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig), new TweetUi());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        mActivity = this;
        mTextView = (TextView) findViewById(R.id.title);

        final Intent i = new Intent(getBaseContext(), FeedActivity.class);
        mTwitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                i.putExtra("Twitter AuthToken", result.data.getAuthToken().token);
                i.putExtra("Twitter AuthSecret", result.data.getAuthToken().secret);
                i.putExtra("Twitter UserName", result.data.getUserName());
                i.putExtra("Twitter UserID", result.data.getUserId());
                if (signedWithOne)
                    startActivity(i);
                else {
                    signedWithOne = true;
                    ((ViewManager)mTwitterLoginButton.getParent()).removeView(mTwitterLoginButton);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mFacebookLoginButton.getLayoutParams());
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mFacebookLoginButton.setLayoutParams(layoutParams);

                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(mTextView.getLayoutParams());
                    layoutParams1.addRule(RelativeLayout.ABOVE, R.id.facebook_login_button);
                    layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    mTextView.setLayoutParams(layoutParams1);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                new SnackBar.Builder(mActivity)
                        .withMessage("Twitter Login Failed.")
                        .withBackgroundColorId(R.color.tw__blue_pressed)
                        .show();
                exception.printStackTrace();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        mFacebookLoginButton.setPadding(70, 50, 70, 50);
        mFacebookLoginButton.setReadPermissions("read_stream");
        mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                i.putExtra("Facebook AccessToken", loginResult.getAccessToken().getToken());
                if (signedWithOne)
                    startActivity(i);
                else {
                    signedWithOne = true;
                    ((ViewManager) mFacebookLoginButton.getParent()).removeView(mFacebookLoginButton);
                }
            }

            @Override
            public void onCancel() {
                new SnackBar.Builder(mActivity)
                        .withMessage("Facebook Login Canceled.")
                        .withBackgroundColorId(R.color.com_facebook_button_background_color)
                        .show();
            }

            @Override
            public void onError(FacebookException e) {
                new SnackBar.Builder(mActivity)
                        .withMessage("Facebook Login Error.")
                        .withBackgroundColorId(R.color.com_facebook_button_background_color)
                        .show();
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            i.putExtra("Facebook AccessToken", accessToken.getToken());
            if (signedWithOne)
                startActivity(i);
            else {
                signedWithOne = true;
                ((ViewManager) mFacebookLoginButton.getParent()).removeView(mFacebookLoginButton);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 140) mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 64206) callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
