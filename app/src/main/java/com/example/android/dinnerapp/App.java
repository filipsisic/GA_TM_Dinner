package com.example.android.dinnerapp;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;

/**
 * Created by Filip on 24.4.2016..
 */
public class App extends Application {

    private Tracker mTracker;
    private TagManager mTagManager;
    private ContainerHolder containerHolder;

    public void startTracking() {
        if (mTracker == null) {
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            mTracker = ga.newTracker(R.xml.tracker_app);
            ga.enableAutoActivityReports(this);
            ga.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
    }

    public Tracker getTracker() {
        startTracking();
        return mTracker;
    }

    public TagManager getTagManager() {
        if (mTagManager == null) {
            mTagManager = TagManager.getInstance(getApplicationContext());
        }
        return mTagManager;
    }

    public void setContainerHolder(ContainerHolder containerHolder) {
        this.containerHolder = containerHolder;
    }

    public ContainerHolder getContainerHolder() {
        return containerHolder;
    }
}
