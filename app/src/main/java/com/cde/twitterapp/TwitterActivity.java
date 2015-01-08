package com.cde.twitterapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cde.twitterapp.db.TweetBO;
import com.cde.twitterapp.db.UserDbEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EActivity(R.layout.activity_twitter)
@OptionsMenu(R.menu.menu_twitter)
public class TwitterActivity extends ActionBarActivity {
    //Update Constants
    public static final String AUTHORITY = "com.cde.twitterapp.provider";
    public static final String ACCOUNT = "default_account";
    public static final String ACCOUNT_TYPE = "com.cde.test";
    public static final long SYNC_INTERVAL = 20L*60L; //Every 20 minutes;

    @OptionsMenuItem
    MenuItem actionSearch;
    SearchView searchView = null;

    @OptionsMenuItem
    MenuItem actionAddUser;
    SearchView actionAddUserView = null;

    @Bean
    TweetBO tweetBO;

    @SystemService
    AccountManager accountManager;
    FragmentManager fm;
    ActionBar actionBar;
    List<String> following = new ArrayList<String>();
    ContentResolver mResolver;
    Account mAccount;

    @Pref
    TwitterAppPreferences_ prefs;

    @AfterInject
    void finalInjections(){
        fm = getSupportFragmentManager();
        if(prefs.following().get() != null) {
            for (String userName : prefs.following().get()) {
                following.add(userName);
            }
        }
    }

    @AfterInject
    void setAutoUpdate(){
        mResolver = getContentResolver();
        mAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(mAccount, null, null);
        mResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

    @AfterViews
    void configureActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for(String userName : following) {
            UserDbEntity user = tweetBO.getUser(userName);
            if(user == null){
                manualUpdate();
                user = tweetBO.getUser(userName);
            }
            ActionBar.Tab tab = actionBar.newTab().setText(user.getName()).setTabListener(new TabListener<TimelineTabFragment_>(this, TimelineTabFragment_.class, user));
            actionBar.addTab(tab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        setSearchView();
        setAddUserView();
        return true;
    }


    private void setSearchView(){
        searchView = (SearchView) MenuItemCompat.getActionView(actionSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                findContent("%" + s + "%");
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void setAddUserView(){
        actionAddUserView = (SearchView) MenuItemCompat.getActionView(actionAddUser);
        actionAddUserView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                addUser(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Background
    public void findContent(String content){
        TimelineTabFragment tabFragment = (TimelineTabFragment) fm.findFragmentByTag(following.get(actionBar.getSelectedTab().getPosition()));
        tabFragment.searchContent(content);
    }

    @Background
    public void addUser(String content){
        if(following.contains(content)) return;
        Set<String> set = prefs.following().get();
        Set<String> newset = new HashSet<String>();
        if(set != null) {
            for(String user: set) {
                newset.add(user);
            }
        }
        newset.add(content);
        prefs.edit().following().put(newset).apply();
    }

    //TODO think this over
    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void notifyTimelineChange(long userId){
        //TODO Choose correct tab
        //TimelineTabFragment tabFragment = (TimelineTabFragment) fm.findFragmentByTag();
    }

    @OptionsItem(R.id.action_update)
    void manualUpdate(){
        Log.e("Activity", "manual update");
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }
}
