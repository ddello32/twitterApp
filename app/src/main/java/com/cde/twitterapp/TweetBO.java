package com.cde.twitterapp;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by dello on 01/01/15.
 */
@EBean
public class TweetBO {
    @OrmLiteDao(helper = DatabaseHelper.class)
    RuntimeExceptionDao<Author_Entity, Integer> authorDao;

    @OrmLiteDao(helper = DatabaseHelper.class)
    RuntimeExceptionDao<Tweet_Entity, Integer> tweetDao;

    @Bean
    DatabaseHelper databaseHelper;

    Collection<Tweet_Entity> getTweetsFromAuthor(Author_Entity author){
        return  authorDao.queryForSameId(author).tweets;
    }

    Collection<Tweet_Entity> getTweetsFromAuthor(Integer id){
        return  authorDao.queryForId(id).tweets;
    }

    Collection<Tweet_Entity> searchForContent(String content){
        //TODO: make this safe for user input.
        Collection<Tweet_Entity> ret;
        try{
           ret = tweetDao.queryBuilder().where().like(Tweet_Entity.CONTENT_COLLUMN_NAME, content).query();
        } catch(Exception e){
           ret = null;
        }
        return ret;
    }

    Collection<Tweet_Entity> getAllTweets(){
        Collection<Tweet_Entity> ret;
        try{
            ret = tweetDao.queryForAll();
        } catch(Exception e){
            ret = null;
        }
        return ret;
    }
}
