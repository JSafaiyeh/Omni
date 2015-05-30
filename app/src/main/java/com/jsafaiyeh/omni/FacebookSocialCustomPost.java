package com.jsafaiyeh.omni;

import android.content.Context;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.util.Date;

public class FacebookSocialCustomPost extends SocialCustomPost {
    @Override
    public Date getTimeStamp() throws ParseException {
        return null;
    }

    @Override
    public void addToFeed(Context mContext, LinearLayout mLinearLayout) {

    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}
