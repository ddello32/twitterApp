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
    static final String IMAGE_URL_COLUMN_NAME = "profile_image";

    @DatabaseField(id = true, columnName = ID_COLUMN_NAME)
    private long id;

    @DatabaseField(columnName = NAME_COLUMN_NAME)
    private String name;

    @DatabaseField(columnName = USERNAME_COLUMN_NAME)
    @JsonProperty("screen_name")
    private String userName;

    @DatabaseField(columnName = URL_COLUMN_NAME)
    private String url;

    @DatabaseField(columnName = IMAGE_URL_COLUMN_NAME)
    @JsonProperty("profile_image_url")
    private String profile_image;

    @ForeignCollectionField
    private ForeignCollection<TweetDbEntity> tweets;

    public UserDbEntity(){
        //Empty constructor needed by ORMLITE
    }

    public UserDbEntity(long id, String name, String userName, String profile_image){
        this.setId(id);
        this.setName(name);
        this.setUserName(userName);
        this.setProfile_image(profile_image);
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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public ForeignCollection<TweetDbEntity> getTweets() {
        return tweets;
    }
}
