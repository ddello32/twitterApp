package com.cde.twitterapp.db;

import android.content.Context;
import java.sql.SQLException;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.EBean;

/**
 * Created by dello on 31/12/14.
 */
@EBean
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "twitter_app.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 2;

    RuntimeExceptionDao<TweetDbEntity, Integer> tweetDao;
    RuntimeExceptionDao<UserDbEntity, Integer> authorDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        authorDao = getRuntimeExceptionDao(UserDbEntity.class);
        tweetDao = getRuntimeExceptionDao(TweetDbEntity.class);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TweetDbEntity.class);
            TableUtils.createTable(connectionSource, UserDbEntity.class);

            //Add a couple of rows for testing
            UserDbEntity Daniel = new UserDbEntity(1,"Daniel", "ddello32", null);
            UserDbEntity Renato = new UserDbEntity(2,"Renato", "rdrusso", null);
            authorDao.create(Daniel);
            authorDao.create(Renato);
            tweetDao.create(new TweetDbEntity(1,"Teste", Daniel, new Date(2012, 1, 4)));
            tweetDao.create(new TweetDbEntity(2,"Teste2", Daniel, new Date(2012, 1, 5)));
            tweetDao.create(new TweetDbEntity(3,"Teste re", Renato, new Date(2012, 1, 6)));
            tweetDao.create(new TweetDbEntity(4,"Teste2 re", Renato, new Date(2012, 1, 7)));
            Log.e(DatabaseHelper.class.getName(), "Populated database");
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    /**
     * Update database, for now just delete and start over.
     */
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, TweetDbEntity.class, true);
            TableUtils.dropTable(connectionSource, UserDbEntity.class, true);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't delete database", e);
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
        tweetDao = null;
        authorDao = null;
    }
}

