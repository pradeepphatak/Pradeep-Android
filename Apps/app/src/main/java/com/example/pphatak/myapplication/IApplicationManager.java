/**
 * Copyright: This code belongs to Pradeep Phatak (pradeep.phatak@gmail.com)
 * Use of this code will require permission from Pradeep Phatak
 */
package com.example.pphatak.myapplication;

import java.util.List;

/**
 * Created by pphatak on 6/13/15.
 */
public interface IApplicationManager {
    List<String> getApplicationNames();
    boolean stopAccess(List<String> pkgNames);
    boolean grantAccess(List<String> pkgNames);
    String getApplicationPackageName(String applicationName);
    void startApplication(String packageName);
}
