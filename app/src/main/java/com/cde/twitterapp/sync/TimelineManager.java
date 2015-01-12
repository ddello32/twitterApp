package com.cde.twitterapp.sync;

import com.cde.twitterapp.R;
import com.cde.twitterapp.TwitterAppPreferences_;
import com.cde.twitterapp.db.TweetDBManager;
import com.cde.twitterapp.db.TweetDbEntity;
import com.cde.twitterapp.db.UserDbEntity;
import com.cde.twitterapp.rest.ErrorHandler;
import com.cde.twitterapp.rest.TweetRestEntity;
import com.cde.twitterapp.rest.TwitterApiAuth;
import com.cde.twitterapp.rest.TwitterRestClient;
import com.cde.twitterapp.rest.UserRestEntity;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

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
    TweetDBManager tweetDBManager;

    @Bean
    TwitterApiAuth authHandler;
    @RestService
    TwitterRestClient restClient;

    @Pref
    TwitterAppPreferences_ prefs;

    ArrayList<String> users = new ArrayList<String>();

    void configureAuthentication(){
        if(!authHandler.isInitialized()){
            authHandler.initialize(consumerKey, consumerSecret);
            restClient.setAuthentication(authHandler);
            restClient.setRestErrorHandler(new ErrorHandler());
        }
    }

    @AfterInject
    void usersUpdated(){
        users.clear();
        for(String userName : prefs.following().get()){
            users.add(userName);
        }
    }

    private void recoverUserTimeline(long userId, long sinceId){
        List<TweetRestEntity> page = restClient.getUserTimeline(userId, sinceId, REQUISITION_PAGE_SIZE);
        if(page == null) return;
        long max_id = page.get(page.size() - 1).getId() - 1;
        while(page.size() != 0){
            max_id = page.get(page.size() - 1).getId() - 1;
            for(TweetRestEntity tweet : page){
                //TODO: Turn this into a batch operation
                UserRestEntity userRest = tweet.getAuthor();
                UserDbEntity userDb = new UserDbEntity(userRest.getId(), userRest.getName(), userRest.getScreenName(), userRest.getProfile_image());
                tweetDBManager.addTweet(new TweetDbEntity(tweet.getId(), tweet.getText(), userDb, tweet.getDate(), tweet.getLocation()==null?null:tweet.getLocation().getCoordinates()));
            }
            page = restClient.getUserTimeline(userId, sinceId, REQUISITION_PAGE_SIZE, max_id);
            if(page == null) return;
        }
    }

    private void recoverUserTimeline(String userName, long sinceId){
        List<TweetRestEntity> page = restClient.getUserTimeline(userName, sinceId, REQUISITION_PAGE_SIZE);
        if(page == null) return;
        long max_id;
        while(page.size() != 0){
            max_id = page.get(page.size() - 1).getId() - 1;
            List<TweetDbEntity> newTweets = new ArrayList<TweetDbEntity>();
            for(TweetRestEntity tweet : page){
                //TODO: Turn this into a batch operation
                UserRestEntity userRest = tweet.getAuthor();
                UserDbEntity userDb = new UserDbEntity(userRest.getId(), userRest.getName(), userRest.getScreenName(), userRest.getProfile_image());
                newTweets.add(new TweetDbEntity(tweet.getId(), tweet.getText(), userDb, tweet.getDate(), tweet.getLocation()==null?null:tweet.getLocation().getCoordinates()));
            }
            tweetDBManager.addTweets(newTweets);
            page = restClient.getUserTimeline(userName, sinceId, REQUISITION_PAGE_SIZE, max_id);
            if(page == null) return;
        }
    }

    public void updateTimeline(long userId){
        Collection<TweetDbEntity> timeline = tweetDBManager.getTweetsFromAuthor(userId);
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
        Collection<TweetDbEntity> timeline = tweetDBManager.getTweetsFromAuthor(userName);
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
        UserDbEntity user = tweetDBManager.getUser(userId);
        if(user == null){
            UserRestEntity restUser = restClient.getUser(userId);
            if(restUser != null)
                tweetDBManager.addAuthor(new UserDbEntity(restUser.getId(), restUser.getName(), restUser.getScreenName(), restUser.getProfile_image()));
        }
    }

    public void updateUser(String screenName){
        UserDbEntity user = tweetDBManager.getUser(screenName);
        if(user == null){
            UserRestEntity restUser = restClient.getUser(screenName);
            if(restUser != null)
                tweetDBManager.addAuthor(new UserDbEntity(restUser.getId(), restUser.getName(), restUser.getScreenName(), restUser.getProfile_image()));
            else tweetDBManager.triggerObservers(screenName);
        }
    }

    public void updateDB(){
        users.clear();
        for(String userName : prefs.following().get()){
            users.add(userName);
        }
        configureAuthentication();
        for(String user : users){
            updateUser(user);
        }
        for(String user : users){
            updateTimeline(user);
        }
    }
}
