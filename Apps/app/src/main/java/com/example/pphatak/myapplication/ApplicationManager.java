/**
 * Created by pphatak on 12/13/14.
 * Copyright: This code belongs to Pradeep Phatak (pradeep.phatak@gmail.com)
 * Use of this code will require permission from Pradeep Phatak
 */
package com.example.pphatak.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides running applications and launch action.
 * It calls standard Android APIs.
 */
public class ApplicationManager implements IApplicationManager{
    List<RunningTaskInfo> mRecentTasks;
    ActivityManager mActivityManager;
    private static String TAG = "ApplicationManager";
    private State mState;
    private Context mContext;
    private List<String> mAllApplicationNames;
    private List<String> mGrantedApplicationNames;
    private List<String> mDeniedApplicationNames;
    private Map<String, String> mApplicationNamePkgMap;
    private PackageManager mPackageManager;
    private enum State {
        NOVPN,
        VPN
    }

    /**
     * Constructor. Initialization is done here.
     * @param context
     */
    ApplicationManager(Context context) {
        mContext = context;
        mState = State.NOVPN;
        mActivityManager = (ActivityManager) mContext
                .getSystemService(Activity.ACTIVITY_SERVICE);
        mAllApplicationNames = new ArrayList<String>();
        mApplicationNamePkgMap = new HashMap<String, String>();
        mPackageManager = mContext.getPackageManager();
    }

    public void getApplicationPackages() {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = mActivityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList != null) {
            for (int i = 0; i < runningAppProcessInfoList.size(); i++) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcessInfoList.get(i);
                if (runningAppProcessInfo != null) {
                    String applicationPkgName = runningAppProcessInfo.processName;
                    if (!TextUtils.isEmpty(applicationPkgName)) {
                        Log.d(TAG, applicationPkgName);
                    }
                }
            }
        }
    }

    /**
     * Get list of applications running on the device
     * @return List of Application names
     */
    @Override
    public List<String> getApplicationNames() {
        List l = mActivityManager.getRunningAppProcesses();
        if (l == null) {
            return null;
        }
        Iterator i = l.iterator();
        while(i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
            try {
                ApplicationInfo applicationInfo = mPackageManager.getApplicationInfo(info.processName, PackageManager.GET_META_DATA);
                CharSequence applicationLabel = mPackageManager.getApplicationLabel(applicationInfo);
                String packageName = applicationInfo.packageName;
                if (applicationLabel != null) {
                    mAllApplicationNames.add(applicationLabel.toString());
                    mApplicationNamePkgMap.put(applicationLabel.toString(), packageName);
                    Log.d(TAG, applicationLabel.toString());
                }
            }catch(Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return mAllApplicationNames;
    }

    /**
     * Get application name when user clicks on Grid View
     * @param position
     * @return
     */
    public String getApplicationName(int position) {
        if (mAllApplicationNames != null) {
            return mAllApplicationNames.get(position);
        } else {
            return null;
        }
    }

    /**
     * Get package name for a particular application
     * @param applicationName
     * @return
     */
    public String getApplicationPackageName(String applicationName) {
        return mApplicationNamePkgMap.get(applicationName);
    }

    /**
     * Launch application that is clicked by the user.
     * @param packageName
     */
    public void startApplication(String packageName) {
        Log.d(TAG, "starting application package " + packageName);
        Intent launchIntent = mPackageManager.getLaunchIntentForPackage(packageName);
        mContext.startActivity(launchIntent);
    }

    @Override
    public boolean stopAccess(List<String> pkgNames) {
        return false;
    }

    @Override
    public boolean grantAccess(List<String> pkgNames) {
        return false;
    }
}
