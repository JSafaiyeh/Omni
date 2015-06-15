package com.jsafaiyeh.omni;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.LinearLayout;

import com.github.mrengineer13.snackbar.SnackBar;
import com.restfb.Connection;
import com.restfb.FacebookClient;
import com.restfb.types.Post;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Socials {

    public static void loadTweets(final Context mContext, final LinearLayout mLinearLayout, final Activity mActivity, final ArrayList<SocialCustomPost> socialCustomPosts, TwitterSession result) {

        TwitterApiClient twitterApiClient = new TwitterApiClient(result);
        twitterApiClient.getStatusesService().homeTimeline(45, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                final List<Long> tweetIds = new ArrayList<>();
                for (Tweet t : result.data) {
                    tweetIds.add(t.getId());
                }


                TweetUtils.loadTweets(tweetIds, new LoadCallback<List<Tweet>>() {

                    @Override
                    public void success(List<Tweet> tweets) {

                        mLinearLayout.removeAllViews();

                        for (Tweet tweet : tweets) {
                            socialCustomPosts.add(new TwitterSocialCustomPost(tweet));
                        }

                        Collections.sort(socialCustomPosts);

                        for (SocialCustomPost socialCustomPost : socialCustomPosts) {
                            socialCustomPost.addToFeed(mContext, mLinearLayout);
                        }


                    }

                    @Override
                    public void failure(TwitterException exception) {
                        new SnackBar.Builder(mActivity)
                                .withBackgroundColorId(R.color.tw__blue_pressed)
                                .withMessage("Error Loading Tweets")
                                .show();
                    }
                });
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
                new SnackBar.Builder(mActivity)
                        .withBackgroundColorId(R.color.tw__blue_pressed)
                        .withMessage("Error Loading Tweets")
                        .show();
            }
        });
    }

    public static void loadFacebookPosts(FacebookClient facebookClient, ArrayList<SocialCustomPost> socialCustomPosts) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection<Post> myFeed = facebookClient.fetchConnection("me/home", Post.class);
        List<Post> data = myFeed.getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getStatusType() != null) {
                if (data.get(i).getMessage() != null) {
                    if (data.get(i).getMessage().trim().length() > 0) {
                        socialCustomPosts.add(new FacebookSocialCustomPost(myFeed.getData().get(i)));
                    }
                }
            }
        }
    }

}
