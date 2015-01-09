package com.cde.twitterapp;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.cde.twitterapp.db.UserDbEntity;

import java.util.Observable;

/**
 * Created by dello on 07/01/15.
 */
public class TabListener<T extends TimelineTabFragment_> implements ActionBar.TabListener {
    private TimelineTabFragment_ mFragment;
    private final Activity mActivity;
    private final Class<T> mClass;
    private final UserDbEntity user;
    private final Observable observable;

    /** Constructor used each time a new tab is created.
     * @param activity  The host Activity, used to instantiate the fragment
     * @param clz  The fragment's Class, used to instantiate the fragment
     */
    public TabListener(Activity activity, Class<T> clz, UserDbEntity user, Observable observable) {
        mActivity = activity;
        mClass = clz;
        this.user = user;
        this.observable = observable;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // Check if the fragment is already initialized
        if (mFragment == null) {
            // If not, instantiate and add it to the activity
            mFragment = (TimelineTabFragment_) Fragment.instantiate(mActivity, mClass.getName());
            mFragment.setUser(user);
            observable.addObserver(mFragment);
            ft.add(android.R.id.content, mFragment, user.getUserName().toLowerCase());
        } else {
            // If it exists, simply attach it in order to show it
            observable.addObserver(mFragment);
            ft.attach(mFragment);
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(mFragment);
            observable.deleteObserver(mFragment);
        }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}