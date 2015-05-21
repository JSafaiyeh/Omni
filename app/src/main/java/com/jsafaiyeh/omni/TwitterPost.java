package com.jsafaiyeh.omni;

import com.twitter.sdk.android.core.models.Tweet;

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
}
