package com.cde.twitterapp.db;

import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.SelectArg;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    public Collection<TweetDbEntity> getTweetsFromAuthor(UserDbEntity author){
        return authorDao.queryForSameId(author).getTweets();
    }

    public Collection<TweetDbEntity> getTweetsFromAuthor(long id){
        //TODO See this long/int problem.
        UserDbEntity author = authorDao.queryForId((int) id);
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
        UserDbEntity author = authorDao.queryForId((int) tweet.getAuthorEntity().getId());
        if(author == null) {
            author = tweet.getAuthorEntity();
            InputStream input = null;
            File storagePath = Environment.getExternalStorageDirectory();
            String uri = storagePath + "/.twitterApp/thumbs/" + author.getUserName() + "tb.png";
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
        tweetDao.createIfNotExists(tweet);
    }

    public UserDbEntity getUser(long userId){
        return authorDao.queryForId((int) userId);
    }
}
