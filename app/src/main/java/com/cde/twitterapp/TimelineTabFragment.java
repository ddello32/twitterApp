package com.cde.twitterapp;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.cde.twitterapp.db.TweetBO;
import com.cde.twitterapp.db.UserDbEntity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by dello on 07/01/15.
 */
@EFragment(R.layout.activity_twitter)
public class TimelineTabFragment extends Fragment {
    @ViewById
    ListView tweetList;

    @Bean
    TweetListAdapter adapter;

    @Bean
    TweetBO tweetBO;

    UserDbEntity user;

    boolean showingSearch = false;

    @AfterViews
    void bindAdapter(){
        if(user != null) {
            adapter.setTweets(user.getTweets());
            tweetList.setAdapter(adapter);
        }
    }

    void setUser(UserDbEntity user){
        this.user = user;
        if(adapter != null) bindAdapter();
    }

    void searchContent(String content){
        adapter.setTweets(tweetBO.searchForContentFromUser(user, content));
    }

    void notifyDBUpdate(){
        if(!showingSearch) adapter.setTweets(user.getTweets());
    }

    void finishSearch(){
        adapter.setTweets(user.getTweets());
        showingSearch = false;
    }

}
