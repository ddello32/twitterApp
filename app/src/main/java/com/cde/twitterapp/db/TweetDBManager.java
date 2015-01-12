package com.cde.twitterapp.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.SelectArg;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Observable;

/**
 * Manages all iteration with local db.
 * Created by dello on 01/01/15.
 */
@EBean(scope = EBean.Scope.Singleton)
public class TweetDBManager extends Observable{
    @OrmLiteDao(helper = DatabaseHelper.class)
    RuntimeExceptionDao<UserDbEntity, Integer> authorDao;

    @OrmLiteDao(helper = DatabaseHelper.class)
    RuntimeExceptionDao<TweetDbEntity, Integer> tweetDao;

    @RootContext
    Context context;

    /**
     * @param author User entity
     * @return Collection with all tweets from this author.
     */
    public Collection<TweetDbEntity> getTweetsFromAuthor(UserDbEntity author){
        return authorDao.queryForSameId(author).getTweets();
    }

    /**
     * @param id author's id
     * @return Collection with all tweets from this author.
     */
    public Collection<TweetDbEntity> getTweetsFromAuthor(long id){
        //TODO See this long/int problem.
        UserDbEntity author = getUser(id);
        if(author == null) return null;
        return author.getTweets();
    }

    /**
     *
     * @param screenName User's screenName
     * @return Collection with all tweets from this author.
     */
    public Collection<TweetDbEntity> getTweetsFromAuthor(String screenName){
        //TODO See this long/int problem.
        UserDbEntity author = getUser(screenName);
        if(author == null) return null;
        return author.getTweets();
    }

    /**
     * Queries for tweets in all of the database
     * @param content Substring to be located
     * @return A Collection of tweets whose text contains the substring content/
     */
    public Collection<TweetDbEntity> searchForContent(String content){
        Collection<TweetDbEntity> ret;
        try{
            SelectArg arg = new SelectArg();
            arg.setValue(content);
           ret = tweetDao.queryBuilder().where().like(TweetDbEntity.TEXT_COLUMN_NAME, arg).query();
        } catch(Exception e){
           ret = null;
        }
        return ret;
    }

    /**
     * Queries for tweets in a user's timeline
     * @param content Substring to be located
     * @return A Collection of tweets from user whose text contains the substring content.
     */
    public Collection<TweetDbEntity> searchForContentFromUser(UserDbEntity user, String content){
        Collection<TweetDbEntity> ret;
        try{
            SelectArg arg = new SelectArg();
            arg.setValue(content);
            ret = tweetDao.queryBuilder().where().eq(TweetDbEntity.AUTHOR_COLUMN_NAME, user.getId()).and().like(TweetDbEntity.TEXT_COLUMN_NAME, arg).query();
        } catch(Exception e){
            ret = null;
        }
        return ret;
    }

    /**
     *
     * @return All tweets stored in the local db.
     */
    public Collection<TweetDbEntity> getAllTweets(){
        Collection<TweetDbEntity> ret;
        try{
            ret = tweetDao.queryForAll();
        } catch(Exception e){
            Log.e("TweetBo", "Exception" + e.toString());
            ret = null;
        }
        return ret;
    }

    /**
     * Adds a collection of tweets to the local db and notifies observers (with null argument).
     * @param tweets Collection of tweets to be added
     */
    @Background(serial = "DATABASE")
    public void addTweets(Collection<TweetDbEntity> tweets) {
        //TODO: Make this a batch operation.
        for(TweetDbEntity tweet : tweets){
            tweetDao.createIfNotExists(tweet);
        }
        triggerObservers(null);
    }

    /**
     * Adds a single tweet to the local db and notifies observers (with null argument).
     * @param tweet Tweets to be added
     */
    @Background(serial = "DATABASE")
    public void addTweet(TweetDbEntity tweet){
        //addAuthor(tweet.getAuthorEntity());
        tweetDao.createIfNotExists(tweet);
        triggerObservers(null);
    }

    /**
     * Add's an user to the local database and notifies observers (with the user's screenName as arg).
     * @param author User to be added
     */
    @Background(serial = "DATABASE")
    public void addAuthor(UserDbEntity author){
        if(authorDao.queryForId((int) author.getId())!= null) return;
        InputStream input = null;
        File storagePath = new File(context.getFilesDir(), author.getName() + "tb.png");
        String uri = storagePath.getPath();
        try {
            URL url = new URL (author.getProfile_image_url());
            input = url.openStream();
            OutputStream output = new FileOutputStream(uri);
            try {
                byte[] buffer = new byte[100000];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(input != null) try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        author.setProfile_image_uri(uri);
        authorDao.create(author);
        triggerObservers(author.getUserName());
    }

    /**
     * Queries for an specific user in the local db
     * @param userId User id in Twitter's db
     * @return User whose id equals userId
     */
    public UserDbEntity getUser(long userId){
        try {
            return authorDao.queryBuilder().where().eq(UserDbEntity.ID_COLUMN_NAME, userId).queryForFirst();
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Queries for an specific user in the local db
     * @param screenName Twitter's screenName for the user
     * @return User whose userName equals screenName
     */
    public UserDbEntity getUser(String screenName){
        try {
            return authorDao.queryBuilder().where().like(UserDbEntity.USERNAME_COLUMN_NAME, screenName).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Notifies observer's of data changed
     * @param userName Argument to be passed to observers so they know which user has been updated
     */
    public void triggerObservers(Object userName){
        setChanged();
        notifyObservers(userName);
    }

    /**
     * Persists the changes done to a Tweet into the local db and notifies observers
     * @param tweet Tweet to be persisted
     */
    public void updateTweet(TweetDbEntity tweet){
        tweetDao.update(tweet);
        triggerObservers(null);
    }

    /**
     * Persists the changes done to a Collection of Tweet into the local db and notifies observers
     * @param tweets Collection of tweets to be be persisted
     */
    public void updateTweets(Collection<TweetDbEntity> tweets){
        for(TweetDbEntity tweet : tweets){
            tweetDao.update(tweet);
        }
        triggerObservers(null);
    }

    /**
     * Persists the changes done to an user entity into the local db
     * @param user User entity to be persisted
     */
    public void updateUser(UserDbEntity user){
        authorDao.update(user);
        triggerObservers(user.getUserName());
    }
}
