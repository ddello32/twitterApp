package com.cde.twitterapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
public class TweetListAdapter extends BaseAdapter {

    @Bean
    TweetBO tweetBo;

    @RootContext
    Context context;

    ArrayList<TweetDbEntity> tweets;

    @AfterInject
    void initAdapter() {
        tweets = new ArrayList<TweetDbEntity>(tweetBo.getAllTweets());
    }

    public void setTweets(Collection<TweetDbEntity> tweets){
        tweets.clear();
        tweets.addAll(tweets);
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
