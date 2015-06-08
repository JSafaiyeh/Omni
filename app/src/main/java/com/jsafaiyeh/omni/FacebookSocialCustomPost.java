package com.jsafaiyeh.omni;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.restfb.types.Post;
import com.restfb.types.StatusMessage;

import java.text.ParseException;
import java.util.Date;

public class FacebookSocialCustomPost extends SocialCustomPost {

    private Post post;
    private String userName;
    private String message;
    private Date timeStamp;

    public FacebookSocialCustomPost(Post post) {
        this.post = post;
        timeStamp = post.getUpdatedTime();
        userName = post.getFrom().getName();
        message = post.getMessage();
    }

    public Post getPost() { return post; }

    public String getSocial() { return"Facebook"; }

    @Override
    public Date getTimeStamp() throws ParseException {
        return timeStamp;
    }

    @Override
    public void addToFeed(Context mContext, LinearLayout mLinearLayout) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View custom  = layoutInflater.inflate(R.layout.facebook_card, null);

        TextView userName = (TextView) custom.findViewById(R.id.fb_user_name);
        userName.setText(this.userName);

        TextView message = (TextView) custom.findViewById(R.id.fb_message);
        message.setText(this.message);

        TextView time = (TextView) custom.findViewById(R.id.post_time);
        time.setText(timeStamp.toString());

        mLinearLayout.addView(custom);
    }

    @Override
    public int compareTo(@NonNull Object another) {
        try {
            return ((SocialCustomPost) another).getTimeStamp().compareTo(getTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
