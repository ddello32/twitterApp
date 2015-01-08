package com.cde.twitterapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.cde.twitterapp.db.TweetBO;
import com.cde.twitterapp.db.TweetDbEntity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dello on 01/01/15.
 */
@EBean
public class TweetListAdapter extends BaseAdapter{
    ArrayList<TweetDbEntity> tweets;

    @RootContext
    Context context;

    @Bean
    TweetBO tweetBO;

    @AfterInject
    public void initAdapter(){
        tweets = new ArrayList<TweetDbEntity>(tweetBO.getAllTweets());
    }

    public void setTweets(Collection<TweetDbEntity> tweets){
        //tweets.clear();
        //tweets.addAll(tweets);
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public TweetDbEntity getItem(int i) {
        return tweets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        TweetView tweetItemView ;
        if (convertView == null) {
            tweetItemView = TweetView_.build(context);
        } else {
            tweetItemView = (TweetView) convertView;
        }

        tweetItemView.bind(getItem(i));

        return tweetItemView;
    }
}
