package com.jsafaiyeh.omni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TwitterPost extends Post {

    private Tweet tweet;
    private String timeStamp;

    public TwitterPost(Tweet tweet) {
        this.tweet = tweet;
        timeStamp = tweet.createdAt;
    }

    public Tweet getTweet() {
        return tweet;
    }

    @Override
    public Date getTimeStamp() throws ParseException {
        final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        return sf.parse(timeStamp);
    }

    @Override
    public void addToFeed(Context mContext, LinearLayout mLinearLayout) {
        View view = new TweetView(mContext, tweet);
        view.setPadding(0, 50, 0, 0);
        mLinearLayout.addView(view);
    }

    @Override
    public int compareTo(@NonNull Object another) {
        try {
            return ((TwitterPost) another).getTimeStamp().compareTo(getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
