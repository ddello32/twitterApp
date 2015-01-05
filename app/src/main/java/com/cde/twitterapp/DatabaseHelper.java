package com.cde.twitterapp;

import android.content.Context;
import java.sql.SQLException;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

/**
 * Created by dello on 31/12/14.
 */
@EBean
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "twitter_app.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    RuntimeExceptionDao<Tweet_Entity, Integer> tweetDao;
    RuntimeExceptionDao<Author_Entity, Integer> authorDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        authorDao = getRuntimeExceptionDao(Author_Entity.class);
        tweetDao = getRuntimeExceptionDao(Tweet_Entity.class);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Tweet_Entity.class);
            TableUtils.createTable(connectionSource, Author_Entity.class);

            //Add a couple of rows for testing
            Author_Entity Daniel = new Author_Entity("Daniel", "ddello32");
            Author_Entity Renato = new Author_Entity("Renato", "rdrusso");
            authorDao.create(Daniel);
            authorDao.create(Renato);
            tweetDao.create(new Tweet_Entity("Teste", Daniel, new Date(2012, 1, 4)));
            tweetDao.create(new Tweet_Entity("Teste2", Daniel, new Date(2012, 1, 5)));
            tweetDao.create(new Tweet_Entity("Teste re", Renato, new Date(2012, 1, 6)));
            tweetDao.create(new Tweet_Entity("Teste2 re", Renato, new Date(2012, 1, 7)));
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
            TableUtils.dropTable(connectionSource, Tweet_Entity.class, true);
            TableUtils.dropTable(connectionSource, Author_Entity.class, true);
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

