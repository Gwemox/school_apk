package com.ynov.tbu.schoolexplorer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by titic on 05/06/2018.
 */

public class SchoolExplorerApplication extends Application {

    private static SchoolExplorerApplication instance;

    private boolean schoolPublicEnabled = true;
    private boolean schoolPrivateEnabled = true;
    private SharedPreferences prefs = null;
    public void onCreate() {
        super.onCreate();
        SchoolExplorerApplication.instance = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        this.setSchoolPrivateEnabled(prefs.getBoolean("private", true));
        this.setSchoolPublicEnabled(prefs.getBoolean("public", true));
    }

    public static SchoolExplorerApplication getInstance() {
        return instance;
    }

    public boolean isSchoolPublicEnabled() {
        return schoolPublicEnabled;
    }

    public void setSchoolPublicEnabled(boolean schoolPublicEnabled) {
        this.schoolPublicEnabled = schoolPublicEnabled;
        prefs.edit()
                .putBoolean("public", schoolPublicEnabled)
                .apply();
    }

    public boolean isSchoolPrivateEnabled() {
        return schoolPrivateEnabled;
    }

    public void setSchoolPrivateEnabled(boolean schoolPrivateEnabled) {
        this.schoolPrivateEnabled = schoolPrivateEnabled;
        prefs.edit()
                .putBoolean("private", schoolPrivateEnabled)
                .apply();
    }
}