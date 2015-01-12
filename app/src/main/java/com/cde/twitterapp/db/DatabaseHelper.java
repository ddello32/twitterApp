package com.cde.twitterapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;

/**
 * Database helper class
 * Created by dello on 31/12/14.
 */
@EBean
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "twitter_app.db";
    private static final int DATABASE_VERSION = 7;

    //RuntimeExceptionDao<TweetDbEntity, Integer> tweetDao;
    //RuntimeExceptionDao<UserDbEntity, Integer> authorDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //authorDao = getRuntimeExceptionDao(UserDbEntity.class);
        //tweetDao = getRuntimeExceptionDao(TweetDbEntity.class);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TweetDbEntity.class);
            TableUtils.createTable(connectionSource, UserDbEntity.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    /**
     * Update database, for now just delete and start over.
     */
    //TODO Really update, not drop everything and start over
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, TweetDbEntity.class, true);
            TableUtils.dropTable(connectionSource, UserDbEntity.class, true);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't delete database", e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        onCreate(database, connectionSource);
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        //tweetDao = null;
        //authorDao = null;
    }
}

