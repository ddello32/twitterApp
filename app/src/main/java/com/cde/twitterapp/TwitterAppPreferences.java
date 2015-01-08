package com.cde.twitterapp;

import com.cde.twitterapp.db.UserDbEntity;

import org.androidannotations.annotations.sharedpreferences.DefaultStringSet;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.util.List;
import java.util.Set;

/**
 * Created by dello on 08/01/15.
 */
@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface TwitterAppPreferences {
    @DefaultStringSet
    Set<String> following();
}
