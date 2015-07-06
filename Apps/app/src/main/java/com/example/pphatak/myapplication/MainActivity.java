/**
 * Created by pphatak on 12/13/14.
 * Copyright: This code belongs to Pradeep Phatak (pradeep.phatak@gmail.com)
 * Use of this code will require permission from Pradeep Phatak
 */
package com.example.pphatak.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

/**
 * First Activity of the application
 * It shows all running applications on the device
 * User can click on application name to launch the application
 */
public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ApplicationManager mApplicationManager;
    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView = (GridView) findViewById(R.id.gridview);
    }

    /**
     * ApplicationManager is instantiated and running applications are determined in onResume.
     * This is because applications can be started or killed.
     * User needs to see the latest list everytime onResume.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mApplicationManager = new ApplicationManager(this);
        List<String> appNames = showRunningApplications();
        if(appNames!=null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, appNames);
            mGridView.setAdapter(arrayAdapter);
            mGridView.setOnItemClickListener(this);
        }
    }

    /**
     * Default implementation added by Android Studio.
     * TODO: Update when actions are added.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Default implementation added by Android Studio.
     * TODO: Update when actions are added.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Show running applications on the device
     * @return
     * List of application names
     */
    private List<String> showRunningApplications() {
        return mApplicationManager.getApplicationNames();
    }

    /**
     * Launch application that the user clicks
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String appName = mApplicationManager.getApplicationName(position);
        if (!TextUtils.isEmpty(appName)) {
            String packageName = mApplicationManager.getApplicationPackageName(appName);
            if (!mApplicationManager.startApplication(packageName)) {
                Toast.makeText(this, "Application " + appName + " cannot be launched",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Application name not found " + position,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
