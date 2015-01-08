package com.cde.twitterapp;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.cde.twitterapp.db.TweetDbEntity;
import com.cde.twitterapp.db.UserDbEntity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collection;

import static android.widget.Toast.makeText;

/**
 * Created by dello on 07/01/15.
 */
@EFragment(R.layout.activity_twitter)
public class TimelineTabFragment extends Fragment {
    @ViewById
    ListView tweetList;

    @Bean
    TweetListAdapter adapter;

    UserDbEntity user;

    @AfterViews
    void bindAdapter(){
        if(user != null) {
            Log.e(this.getClass().getName(), "bindAdapter" +  user.getTweets().size());
            //adapter.setTweets(user.getTweets());
            tweetList.setAdapter(adapter);
        }
    }

    @ItemClick
    void tweetListItemClicked(TweetDbEntity tweet) {
        //TODO
    }

    void setUser(UserDbEntity user){
        this.user = user;
        if(adapter != null) bindAdapter();
    }

}
