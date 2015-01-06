package com.cde.twitterapp.db;

import android.util.Log;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

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

    public Collection<TweetDbEntity> getTweetsFromAuthor(Integer id){
        return authorDao.queryForId(id).getTweets();
    }

    public Collection<TweetDbEntity> searchForContent(String content){
        //TODO: make this safe for user input.
        Collection<TweetDbEntity> ret;
        try{
           ret = tweetDao.queryBuilder().where().like(TweetDbEntity.TEXT_COLUMN_NAME, content).query();
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
}
