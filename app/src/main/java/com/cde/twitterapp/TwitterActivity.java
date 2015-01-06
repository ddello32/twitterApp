package com.cde.twitterapp;

import android.support.annotation.StringRes;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.cde.twitterapp.db.TweetBO;
import com.cde.twitterapp.db.TweetDbEntity;
import com.cde.twitterapp.rest.TweetRestEntity;
import com.cde.twitterapp.rest.TwitterApiAuth;
import com.cde.twitterapp.rest.TwitterRestClient;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.widget.Toast.makeText;

@EActivity(R.layout.activity_twitter)
@OptionsMenu(R.menu.menu_twitter)
public class TwitterActivity extends ActionBarActivity {
    //@StringRes
    static final String consumerKey = "0PHGAdzLcPRIODJWGb9qBZqP0";

    //@StringRes(R.string.consumerSecret)
    static final String consumerSecret = "XuW9tzhXqW7AXEfP0YMbYyO1lnTHA6eRfjnFAbwH0qX0pQlLBx";

    @ViewById
    ListView tweetList;

    @Bean
    TweetListAdapter adapter;

    @OptionsMenuItem
    MenuItem actionSearch;

    @Bean
    TweetBO TweetBO;

    @Bean
    TwitterApiAuth authHandler;

    @RestService
    TwitterRestClient restClient;

    SearchView searchView = null;
    ActionBar actionBar;

    @AfterInject
    void finalInjections(){
        actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    @AfterInject
    @Background
    void setNetwork(){
        Log.d("SetNetwork", consumerKey);
        authHandler.initialize(consumerKey, consumerSecret);
        restClient.setAuthentication(authHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        updateSearchView();
        return true;
    }

    @AfterViews
    void bindAdapter(){
        tweetList.setAdapter(adapter);
    }

    @ItemClick
    void tweetListItemClicked(TweetDbEntity tweet) {
    List<TweetRestEntity> tweets = new ArrayList<TweetRestEntity>();
    getTweetsFromUser(tweets, 310072711, 5);
    makeText(this, tweet.getAuthorEntity().getName() + " said: " + tweet.getText(), Toast.LENGTH_SHORT).show();
    }

    //TODO: ERASE THIS! JUST FOR TESTING!
    @Background
    void getTweetsFromUser(List<TweetRestEntity> returnList, long userid, int count){
        returnList.addAll(restClient.getUserTimeline(userid, count));
        Log.e("TEST", returnList.get(0).getText());
    }


    private void updateSearchView(){
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
        Collection<TweetDbEntity> results = TweetBO.searchForContent(content);
        updateListView(results);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void updateListView(Collection<TweetDbEntity> tweets){
        //For some reason this doesn't work. Will look up into it.
        //adapter.setTweets(tweets);
        adapter.tweets.clear();
        adapter.tweets.addAll(tweets);
        adapter.notifyDataSetChanged();
    }

}
