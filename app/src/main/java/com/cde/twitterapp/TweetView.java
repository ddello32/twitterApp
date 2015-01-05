package com.cde.twitterapp;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

/**
 * Created by dello on 01/01/15.
 * Simple model, should upgrade soon
 */
@EViewGroup(R.layout.tweet_layout)
public class TweetView extends LinearLayout {

    @ViewById
    TextView authorName;

    @ViewById
    TextView tweetContent;

    public TweetView(Context context) {
        super(context);
    }

    public void bind(Tweet_Entity tweet){
        authorName.setText(tweet.authorEntity.userName + " said:");
        tweetContent.setText(tweet.content);
    }
}
