package com.cde.twitterapp.db;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.SelectArg;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.RootContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by dello on 01/01/15.
 */
@EBean
public class TweetBO {
    @OrmLiteDao(helper = DatabaseHelper.class)
    RuntimeExceptionDao<UserDbEntity, Integer> authorDao;

    @OrmLiteDao(helper = DatabaseHelper.class)
    RuntimeExceptionDao<TweetDbEntity, Integer> tweetDao;

    @RootContext
    Context context;

    public Collection<TweetDbEntity> getTweetsFromAuthor(UserDbEntity author){
        return authorDao.queryForSameId(author).getTweets();
    }

    public Collection<TweetDbEntity> getTweetsFromAuthor(long id){
        //TODO See this long/int problem.
        UserDbEntity author = getUser(id);
        if(author == null) return null;
        return author.getTweets();
    }

    public Collection<TweetDbEntity> getTweetsFromAuthor(String screenName){
        //TODO See this long/int problem.
        UserDbEntity author = getUser(screenName);
        if(author == null) return null;
        return author.getTweets();
    }

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

    @Background(serial = "DATABASE")
    public void addTweets(Collection<TweetDbEntity> tweets) {
        //TODO: Make this a batch operation.
        for(TweetDbEntity tweet : tweets){
            addTweet(tweet);
        }
    }

    @Background(serial = "DATABASE")
    public void addTweet(TweetDbEntity tweet){
        addAuthor(tweet.getAuthorEntity());
        tweetDao.createIfNotExists(tweet);
    }

    @Background(serial = "DATABASE")
    public void addAuthor(UserDbEntity author){
        if(authorDao.queryForId((int) author.getId()) != null) return;
        InputStream input = null;
        File storagePath = new File(context.getFilesDir(), author.getName() + "tb.png");
        String uri = storagePath.getPath();
        try {
            URL url = new URL (author.getProfile_image_url());
            input = url.openStream();
            //The sdcard directory e.g. '/sdcard' can be used directly, or
            //more safely abstracted with getExternalStorageDirectory()
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
    }

    public UserDbEntity getUser(long userId){
        return authorDao.queryForId((int) userId);
    }

    public UserDbEntity getUser(String screenName){
        try {
            return authorDao.queryBuilder().where().like(UserDbEntity.USERNAME_COLUMN_NAME, screenName).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
