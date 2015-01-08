package com.cde.twitterapp.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

/**
 * Created by dello on 31/12/14.
 * Just a test model. Should change once using twitter api
 */
public class UserDbEntity{
    static final String ID_COLUMN_NAME = "id";
    static final String NAME_COLUMN_NAME = "name";
    static final String USERNAME_COLUMN_NAME = "username";
    static final String URL_COLUMN_NAME = "url";
    static final String IMAGE_URL_COLUMN_NAME = "profile_image_url";
    static final String IMAGE_URI_COLUMN_NAME = "profile_image_uri";


    @DatabaseField(id = true, columnName = ID_COLUMN_NAME)
    private long id;

    @DatabaseField(columnName = NAME_COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = USERNAME_COLUMN_NAME)
    private String userName;

    @DatabaseField(columnName = URL_COLUMN_NAME)
    private String url;

    @DatabaseField(columnName = IMAGE_URL_COLUMN_NAME)
    private String profile_image_url;

    @DatabaseField(columnName = IMAGE_URI_COLUMN_NAME)
    private String profile_image_uri;

    @ForeignCollectionField(orderColumnName = TweetDbEntity.ID_COLUMN_NAME, orderAscending = false, eager = true)
    private ForeignCollection<TweetDbEntity> tweets;

    public UserDbEntity(){
        //Empty constructor needed by ORMLITE
    }

    public UserDbEntity(long id, String name, String userName, String profile_image_url){
        this.setId(id);
        this.setName(name);
        this.setUserName(userName);
        this.setProfile_image_url(profile_image_url);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_uri() {
        return profile_image_uri;
    }

    public void setProfile_image_uri(String profile_image_uri) {
        this.profile_image_uri = profile_image_uri;
    }

    public ForeignCollection<TweetDbEntity> getTweets() {
        return tweets;
    }
}
