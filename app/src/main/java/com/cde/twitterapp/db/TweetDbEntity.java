package com.cde.twitterapp.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dello on 31/12/14.
 * Just a test model. Should change once using twitter api
 **/

@DatabaseTable
public class TweetDbEntity{
    static final String ID_COLUMN_NAME = "id";
    static final String TEXT_COLUMN_NAME = "text";
    static final String AUTHOR_COLUMN_NAME = "author";
    static final String DATE_COLUMN_NAME = "date";

    @DatabaseField(id = true, columnName = ID_COLUMN_NAME)
    private long id;

    @DatabaseField(columnName = TEXT_COLUMN_NAME)
    private String text;

    @DatabaseField(columnName = AUTHOR_COLUMN_NAME, foreign = true, foreignAutoRefresh = true)
    private UserDbEntity authorEntity;

    @DatabaseField(columnName = DATE_COLUMN_NAME)
    private String date;

    public TweetDbEntity(){
        //Empty constructor needed by ORMLITE
    }

    /**
     *
     * @param id
     * @param text
     * @param authorEntity
     * @param date
     */
    public TweetDbEntity(long id, String text, UserDbEntity authorEntity, String date){
        this.setId(id);
        this.setText(text);
        this.setAuthorEntity(authorEntity);
        this.setDate(date);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserDbEntity getAuthorEntity() {
        return authorEntity;
    }

    public void setAuthorEntity(UserDbEntity authorEntity) {
        this.authorEntity = authorEntity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
