package com.cde.twitterapp;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cde.twitterapp.db.TweetDbEntity;

import org.androidannotations.annotations.EViewGroup;
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

    public void bind(TweetDbEntity tweet){
        authorName.setText(tweet.getAuthorEntity().getUserName() + " said:");
        tweetContent.setText(tweet.getText());
    }
}
