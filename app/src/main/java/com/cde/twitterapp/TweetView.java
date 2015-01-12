package com.cde.twitterapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cde.twitterapp.db.TweetDbEntity;

import org.androidannotations.annotations.CheckedChange;
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

    @ViewById
    TextView tweetDate;

    @ViewById
    ImageView profileImage;

    @ViewById
    CheckBox checkBox;

    TweetDbEntity tweet;

    public TweetView(Context context) {
        super(context);
    }

    public void bind(TweetDbEntity tweet){
        this.tweet = tweet;
        authorName.setText(tweet.getAuthorEntity().getUserName());
        tweetContent.setText(tweet.getText());
        tweetDate.setText(tweet.getDate());
        profileImage.setImageURI(Uri.parse(tweet.getAuthorEntity().getProfile_image_uri()));
        checkBox.setChecked(tweet.isChecked());
       if(tweet.getLocation() != null)  checkBox.setVisibility(View.VISIBLE);
       else checkBox.setVisibility(View.INVISIBLE);
    }

    @CheckedChange(R.id.checkBox)
    public void checkedChange(CompoundButton checkBox, boolean isChecked){
        tweet.setChecked(isChecked);
        if(isChecked) Log.wtf("Checked Change", "OLHA");
    }
}
