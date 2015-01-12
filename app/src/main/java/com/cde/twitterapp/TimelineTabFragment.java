package com.cde.twitterapp;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ListView;

import com.cde.twitterapp.db.TweetDBManager;
import com.cde.twitterapp.db.TweetDbEntity;
import com.cde.twitterapp.db.UserDbEntity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by dello on 07/01/15.
 */
@EFragment(R.layout.activity_twitter)
public class TimelineTabFragment extends Fragment implements Observer{
    @ViewById
    ListView tweetList;

    @Bean
    TweetListAdapter adapter;

    @Bean
    TweetDBManager tweetDBManager;

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
        adapter.setTweets(tweetDBManager.searchForContentFromUser(user, content));
    }

    void finishSearch(){
        adapter.setTweets(user.getTweets());
        showingSearch = false;
    }

    ArrayList<MarkerOptions> getMarkers(){
        ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        for(TweetDbEntity tweet : adapter.getTweets()){
            if(tweet.isChecked()){
                markers.add(
                        new MarkerOptions().position(
                                new LatLng(
                                        tweet.getLocation().get(1),tweet.getLocation().get(0)
                                )
                        ).title(
                                tweet.getAuthorEntity().getName()
                        ).snippet(tweet.getText())
                );
            }
        }
        return markers;
    }

    @Override
    @UiThread
    public void update(Observable observable, Object o) {
        Log.e(user.getUserName(), "Update");
        if(!showingSearch) adapter.setTweets(user.getTweets());
    }

    public void persistChecked(){
        tweetDBManager.updateTweets(adapter.getTweets());
    }
}
