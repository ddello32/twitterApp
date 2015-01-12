package com.cde.twitterapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cde.twitterapp.db.TweetDbEntity;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dello on 01/01/15.
 */
@EBean
public class TweetListAdapter extends BaseAdapter{
    private ArrayList<TweetDbEntity> tweets = new ArrayList<TweetDbEntity>();;

    @RootContext
    Context context;

    @UiThread
    public void setTweets(Collection<TweetDbEntity> tweets){
        this.tweets = new ArrayList<TweetDbEntity>(tweets);
        this.notifyDataSetChanged();
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

    public ArrayList<TweetDbEntity> getTweets(){ return tweets; }
}
