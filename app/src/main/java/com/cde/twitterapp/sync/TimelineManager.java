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

    ArrayList<Long> users = new ArrayList<Long>();

    @AfterInject
    @Background(serial = "REST")
    void configureAuthentication(){
        Log.d("Config Authentication", consumerKey);
        if(!authHandler.isInitialized()){
            authHandler.initialize(consumerKey, consumerSecret);
            restClient.setAuthentication(authHandler);
        }
        //TODO Erase this and make something that actually works
        users.add(new Long(37565276));
    }

    private List<TweetRestEntity> recoverUserTimeline(long userId, long sinceId){
        //TODO: Paginate through requests instead of loading it all.
        return restClient.getUserTimeline(userId, sinceId);
    }

    @Background(serial = "REST")
    public void updateTimeline(long userId){
        Collection<TweetDbEntity> timeline = tweetBO.getTweetsFromAuthor(userId);
        long lastId;
        if(timeline != null && timeline.size() != 0){
            Iterator<TweetDbEntity> iterator = timeline.iterator();
            lastId = iterator.next().getId();
        }
        else lastId = 1;
        List<TweetRestEntity> newTweets = recoverUserTimeline(userId, lastId);
        //TODO: Turn this into a batch operation
        for(TweetRestEntity tweet : newTweets){
            UserRestEntity userRest = tweet.getAuthor();
            UserDbEntity userDb = new UserDbEntity(userRest.getId(), userRest.getName(), userRest.getScreenName(), userRest.getProfile_image());
            tweetBO.addTweet(new TweetDbEntity(tweet.getId(), tweet.getText(), userDb, tweet.getDate()));
        }
        //TODO Notify lists of database update
    }

    @Background(serial = "REST")
    public void updateDB(){
        Log.e(this.getClass().getName(), "UpdateDB " + users.size() + authHandler);
        for(Long user : users){
            updateTimeline(user);
        }
    }
}
