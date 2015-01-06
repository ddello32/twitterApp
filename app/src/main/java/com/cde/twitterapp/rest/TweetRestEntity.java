package com.cde.twitterapp.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dello on 06/01/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetRestEntity implements Serializable {
    long id;
    String text;
    @JsonProperty("user")
    UserRestEntity author;
    @JsonProperty("created_at")
    String date;

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

    public UserRestEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserRestEntity author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
