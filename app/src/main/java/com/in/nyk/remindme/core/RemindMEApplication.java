package com.in.nyk.remindme.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by nikhilkanse on 27/11/17.
 */

public class RemindMEApplication extends Application {
    private static RemindMEApplication mApplication;

    public static Context getAppContext() {
        return mApplication.getApplicationContext();
    }

    public static RemindMEApplication getApplicationInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mApplication == null)
            mApplication = this;
    }
}
