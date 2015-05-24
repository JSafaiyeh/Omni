package com.jsafaiyeh.omni;

import android.content.Context;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

abstract class Post implements Comparable {

    public abstract Date getTimeStamp() throws ParseException;

    public abstract void addToFeed(Context mContext, LinearLayout mLinearLayout);

}
