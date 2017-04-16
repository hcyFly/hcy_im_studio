package com.stay4it.im.untils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


/**
 * Created by Stay on 22/10/15.
 * Powered by www.stay4it.com
 */
public class AppStatusTracker implements Application.ActivityLifecycleCallbacks {
    public static AppStatusTracker instance;
    private int appStatus = Constants.STATUS_FORCEKILLED;
    private boolean isScreenOff;
    private int activityCount = 0;
    private long backgroundStamp = 0l;
    private static final long MAX_ALIVE_INTERVAL = 10 * 60 * 1000l;
    private boolean isForeground;

    private AppStatusTracker() {

    }

    public static void init(Application application) {
        instance = new AppStatusTracker();
        application.registerActivityLifecycleCallbacks(instance);
    }

    public static AppStatusTracker getInstance() {
        return instance;
    }

    public void setAppStatus(int status) {
        this.appStatus = status;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void onScreenOff(boolean isScreenOff) {
        this.isScreenOff = isScreenOff;
    }

    public boolean shouldShowGesture() {
        return isScreenOff() || isOutOfDate();
    }

    private boolean isOutOfDate() {
        return appStatus == Constants.STATUS_ONLINE && !isForeground() && backgroundStamp != 0l && System.currentTimeMillis() - backgroundStamp > MAX_ALIVE_INTERVAL;
    }

    private boolean isScreenOff() {
        return appStatus == Constants.STATUS_ONLINE && isScreenOff;
    }

    public boolean isForeground() {
        return isForeground;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        Trace.d(activity.toString() + ":onActivityStarted");
        activityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        isForeground = true;
        isScreenOff = false;
        backgroundStamp = 0l;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Trace.d(activity.toString() + ":onActivityStopped");
        activityCount--;
        if (activityCount == 0) {
            backgroundStamp = System.currentTimeMillis();
            isForeground = false;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
