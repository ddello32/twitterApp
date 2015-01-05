package com.cde.twitterapp;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by dello on 31/12/14.
 * Just a test model. Should change once using twitter api
 **/

@DatabaseTable
public class Tweet_Entity {
    static final String ID_COLLUMN_NAME = "id";
    static final String CONTENT_COLLUMN_NAME = "content";
    static final String AUTHOR_COLLUMN_NAME = "author";
    static final String DATE_COLLUMN_NAME = "date";

    @DatabaseField(generatedId = true, columnName = ID_COLLUMN_NAME)
    int id;

    @DatabaseField(columnName = CONTENT_COLLUMN_NAME)
    String content;

    @DatabaseField(columnName = AUTHOR_COLLUMN_NAME, foreign = true, foreignAutoRefresh = true)
    Author_Entity authorEntity;

    @DatabaseField(columnName = DATE_COLLUMN_NAME)
    Date date;

    public Tweet_Entity(){
        //Empty constructor needed by ORMLITE
    }

    public Tweet_Entity(String content, Author_Entity authorEntity, Date date){
        this.content = content;
        this.authorEntity = authorEntity;
        this.date = date;
    }

}
