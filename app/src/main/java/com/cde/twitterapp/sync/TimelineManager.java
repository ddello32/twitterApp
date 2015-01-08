package com.cde.twitterapp.sync;

import android.content.Context;
import android.util.Log;

import com.cde.twitterapp.R;
import com.cde.twitterapp.db.TweetBO;
import com.cde.twitterapp.db.TweetDbEntity;
import com.cde.twitterapp.db.UserDbEntity;
import com.cde.twitterapp.rest.TweetRestEntity;
import com.cde.twitterapp.rest.TwitterApiAuth;
import com.cde.twitterapp.rest.TwitterRestClient;
import com.cde.twitterapp.rest.UserRestEntity;
import com.cde.twitterapp.TwitterAppPreferences_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SupposeBackground;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.api.BackgroundExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dello on 06/01/15.
 */
//TODO: Turn this into a content provider
@EBean(scope = EBean.Scope.Singleton)
public class TimelineManager {
    static final int REQUISITION_PAGE_SIZE = 500;
    @StringRes(R.string.consumerKey)
    static String consumerKey;
    @StringRes(R.string.consumerSecret)
    static String consumerSecret;
    @Bean
    TweetBO tweetBO;
    @Bean
    TwitterApiAuth authHandler;
    @RestService
    TwitterRestClient restClient;

    @Pref
    TwitterAppPreferences_ prefs;

    ArrayList<String> users = new ArrayList<String>();

    @AfterInject
    @Background(serial = "REST")
    void configureAuthentication(){
        Log.d("Config Authentication", consumerKey);
        if(!authHandler.isInitialized()){
            authHandler.initialize(consumerKey, consumerSecret);
            restClient.setAuthentication(authHandler);
        }
    }

    @AfterInject
    void notifyUpddateUser(){
        users.clear();
        for(String userName : prefs.following().get()){
            users.add(userName);
        }
    }

    private void recoverUserTimeline(long userId, long sinceId){
        List<TweetRestEntity> page = restClient.getUserTimeline(userId, sinceId, REQUISITION_PAGE_SIZE);
        long max_id = page.get(page.size() - 1).getId() - 1;
        while(page.size() != 0){
            for(TweetRestEntity tweet : page){
                //TODO: Turn this into a batch operation
                UserRestEntity userRest = tweet.getAuthor();
                UserDbEntity userDb = new UserDbEntity(userRest.getId(), userRest.getName(), userRest.getScreenName(), userRest.getProfile_image());
                tweetBO.addTweet(new TweetDbEntity(tweet.getId(), tweet.getText(), userDb, tweet.getDate()));
            }
            restClient.getUserTimeline(userId, sinceId, REQUISITION_PAGE_SIZE, max_id);
            max_id = page.get(page.size() - 1).getId() - 1;
        }
    }

    private void recoverUserTimeline(String userName, long sinceId){
        List<TweetRestEntity> page = restClient.getUserTimeline(userName, sinceId, REQUISITION_PAGE_SIZE);
        long max_id;
        while(page.size() != 0){
            max_id = page.get(page.size() - 1).getId() - 1;
            for(TweetRestEntity tweet : page){
                //TODO: Turn this into a batch operation
                UserRestEntity userRest = tweet.getAuthor();
                UserDbEntity userDb = new UserDbEntity(userRest.getId(), userRest.getName(), userRest.getScreenName(), userRest.getProfile_image());
                tweetBO.addTweet(new TweetDbEntity(tweet.getId(), tweet.getText(), userDb, tweet.getDate()));
            }
            restClient.getUserTimeline(userName, sinceId, REQUISITION_PAGE_SIZE, max_id);
        }
    }

    public void updateTimeline(long userId){
        Collection<TweetDbEntity> timeline = tweetBO.getTweetsFromAuthor(userId);
        long lastId;
        if(timeline != null && timeline.size() != 0){
            Iterator<TweetDbEntity> iterator = timeline.iterator();
            lastId = iterator.next().getId();
        }
        else lastId = 1;
        recoverUserTimeline(userId, lastId);
        //TODO Notify lists of database update
    }

    public void updateTimeline(String userName){
        Collection<TweetDbEntity> timeline = tweetBO.getTweetsFromAuthor(userName);
        long lastId;
        if(timeline != null && timeline.size() != 0){
            Iterator<TweetDbEntity> iterator = timeline.iterator();
            lastId = iterator.next().getId();
        }
        else lastId = 1;
        recoverUserTimeline(userName, lastId);
        //TODO Notify lists of database update
    }

    public void updateUser(long userId){
        UserDbEntity user = tweetBO.getUser(userId);
        if(user == null){
            UserRestEntity restUser = restClient.getUser(userId);
            tweetBO.addAuthor(new UserDbEntity(restUser.getId(), restUser.getName(), restUser.getScreenName(), restUser.getProfile_image()));
        }
    }

    public void updateUser(String screenName){
        UserDbEntity user = tweetBO.getUser(screenName);
        if(user == null){
            UserRestEntity restUser = restClient.getUser(screenName);
            tweetBO.addAuthor(new UserDbEntity(restUser.getId(), restUser.getName(), restUser.getScreenName(), restUser.getProfile_image()));
        }
    }

    @Background(serial = "REST")
    public void updateDB(){
        users.clear();
        for(String userName : prefs.following().get()){
            Log.e("updateDB", userName);
            users.add(userName);
        }
        for(String user : users){
            updateUser(user);
        }
        for(String user : users){
            updateTimeline(user);
        }
    }
}
