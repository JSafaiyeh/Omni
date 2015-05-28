package com.jsafaiyeh.omni;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import com.github.mrengineer13.snackbar.SnackBar;
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

    public static void loadTweets(final Context mContext, final LinearLayout mLinearLayout, final Activity mActivity, final ArrayList<Post> posts, TwitterSession result) {

        TwitterApiClient twitterApiClient = new TwitterApiClient(result);
        twitterApiClient.getStatusesService().homeTimeline(35, null, null, null, null, null, null, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                final List<Long> tweetIds = new ArrayList<>();
                for (Tweet t : result.data) {
                    tweetIds.add(t.getId());
                }


                TweetUtils.loadTweets(tweetIds, new LoadCallback<List<Tweet>>() {

                    @Override
                    public void success(List<Tweet> tweets) {

                        for (Tweet tweet : tweets) {
                            posts.add(new TwitterPost(tweet));
                        }

                        Collections.sort(posts);

                        for (Post p : posts) {
                            p.addToFeed(mContext, mLinearLayout);
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
                new SnackBar.Builder(mActivity)
                        .withBackgroundColorId(R.color.tw__blue_pressed)
                        .withMessage("Error Loading Tweets")
                        .show();
            }
        });
    }
}
