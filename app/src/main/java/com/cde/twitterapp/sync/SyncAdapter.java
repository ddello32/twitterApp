package com.cde.twitterapp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.cde.twitterapp.db.UserDbEntity;

import org.androidannotations.handler.BackgroundHandler;

/**
 * Created by dello on 07/01/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    TimelineManager_ timelineManager;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        timelineManager = TimelineManager_.getInstance_(context);
    }

    /*
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }*/

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.e("SyncAdapter", "onPerformSync");
        timelineManager.updateDB();
    }
}
