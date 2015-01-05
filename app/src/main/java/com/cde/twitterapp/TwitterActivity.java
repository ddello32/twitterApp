package com.cde.twitterapp;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.Toast;

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

import java.util.Collection;

import static android.widget.Toast.makeText;

@EActivity(R.layout.activity_twitter)
@OptionsMenu(R.menu.menu_twitter)
public class TwitterActivity extends ActionBarActivity {
    @ViewById
    ListView tweetList;

    @Bean
    TweetListAdapter adapter;

    @OptionsMenuItem
    MenuItem actionSearch;

    @Bean
    TweetBO TweetBO;

    SearchView searchView = null;
    ActionBar actionBar;

    @AfterInject
    void finalInjections(){
        actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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
    void tweetListItemClicked(Tweet_Entity tweet) {
        makeText(this, tweet.authorEntity.name + " said: " + tweet.content, Toast.LENGTH_SHORT).show();
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
        Collection<Tweet_Entity> results = TweetBO.searchForContent(content);
        updateListView(results);
    }

    @UiThread(propagation = UiThread.Propagation.REUSE)
    void updateListView(Collection<Tweet_Entity> tweets){
        //For some reason this doesn't work. Will look up into it.
        //adapter.setTweets(tweets);
        adapter.tweets.clear();
        adapter.tweets.addAll(tweets);
        adapter.notifyDataSetChanged();
    }

}
