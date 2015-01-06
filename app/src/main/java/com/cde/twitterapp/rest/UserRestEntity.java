package com.cde.twitterapp.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


/**
 * Created by dello on 06/01/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRestEntity implements Serializable {
    long id;
    String name;
    @JsonProperty("screen_name")
    String screenName;
    String url;
    @JsonProperty("profile_image_url")
    String profile_image;

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

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
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

}
