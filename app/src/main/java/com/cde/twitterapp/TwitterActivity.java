package com.cde.twitterapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cde.twitterapp.db.TweetBO;
import com.cde.twitterapp.db.TweetDbEntity;
import com.cde.twitterapp.db.UserDbEntity;
import com.cde.twitterapp.sync.TimelineManager;

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

import java.util.Collection;

import static android.widget.Toast.makeText;

@EActivity(R.layout.activity_twitter)
@OptionsMenu(R.menu.menu_twitter)
public class TwitterActivity extends ActionBarActivity {
    @OptionsMenuItem
    MenuItem actionSearch;

    @Bean
    TweetBO tweetBO;

    SearchView searchView = null;
    ActionBar actionBar;

    //Update Constants
    public static final String AUTHORITY = "com.cde.twitterapp.provider";
    // Account
    public static final String ACCOUNT = "default_account";
    public static final String ACCOUNT_TYPE = "com.cde.test";
    @SystemService
    AccountManager accountManager;
    public static final long SYNC_INTERVAL = 60L*60L; //Every 1 hour;
    ContentResolver mResolver;
    Account mAccount;


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
        UserDbEntity following = tweetBO.getUser(37565276);
        ActionBar.Tab tab =actionBar.newTab().setText(following.getName()).setTabListener(new TabListener<TimelineTabFragment_>(this, "DDello", TimelineTabFragment_.class, following));
        actionBar.addTab(tab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        setSearchView();
        return true;
    }


    private void setSearchView(){
        searchView = (SearchView) MenuItemCompat.getActionView(actionSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String results = "";
                findContent("%" + s + "%", results);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Background
    public void findContent(String content, String ret){
        Collection<TweetDbEntity> results = tweetBO.searchForContent(content);
        //TODO Create new tab for search
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    public void notifyTimelineChange(long userId){
        //TODO Choose correct tab
        Log.e("NotifyDataSetChanged", "Id " + userId);
        //adapter.notifyDBChanged();
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
