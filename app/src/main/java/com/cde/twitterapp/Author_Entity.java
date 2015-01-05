package com.cde.twitterapp;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.Date;

/**
 * Created by dello on 31/12/14.
 * Just a test model. Should change once using twitter api
 */
public class Author_Entity {
    static final String ID_COLLUMN_NAME = "id";
    static final String NAME_COLLUMN_NAME = "name";
    static final String USERNAME_COLLUMN_NAME = "username";

    @DatabaseField(generatedId = true, columnName = ID_COLLUMN_NAME)
    int id;

    @DatabaseField(columnName = NAME_COLLUMN_NAME)
    String name;

    @DatabaseField(columnName = USERNAME_COLLUMN_NAME)
    String userName;

    @ForeignCollectionField
    ForeignCollection<Tweet_Entity> tweets;

    public Author_Entity(){
        //Empty constructor needed by ORMLITE
    }

    public Author_Entity(String name, String userName){
        this.name = name;
        this.userName = userName;
    }

}
