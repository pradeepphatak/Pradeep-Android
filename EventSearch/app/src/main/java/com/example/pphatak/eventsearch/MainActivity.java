package com.example.pphatak.eventsearch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Takes user input and shows key Event information in Recycler View.
 * Supports portrait and landscape modes.
 * Shows progress bar as it is loading new events.
 * Recreates Activity with stored event information if killed by system.
 */
public class MainActivity extends Activity implements IEventNotify {
    private RecyclerView mRecyclerView;
    private EventAdapter mEventAdapter;
    private EventManager mEventManager;
    private ProgressBar mProgressBar;
    List<Event> mEvents;
    private final String JSON_EVENT_KEY = "event_json";
    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEventManager = new EventManager(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.eventList);
        mEvents = Collections.emptyList();
        if (savedInstanceState != null) {
            String eventJsonData = savedInstanceState.getString(JSON_EVENT_KEY);
            if (!TextUtils.isEmpty(eventJsonData)) {
                Log.d(TAG, "Updating events after application restart");
                mEvents = mEventManager.getEventsFromJson(eventJsonData);
            }
        }
        mEventAdapter = new EventAdapter(this, mEvents);
        mRecyclerView.setAdapter(mEventAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mProgressBar = (ProgressBar)findViewById(R.id.progress);
        final EditText editTextKeyword = (EditText)findViewById(R.id.editTextKeyword);
        final EditText editTextLocation = (EditText)findViewById(R.id.editTextLocation);

        Button goButton = (Button)findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = editTextKeyword.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    editTextKeyword.setError(getString(R.string.field_empty_error));
                }
                String location = editTextLocation.getText().toString().trim();
                if (TextUtils.isEmpty(location)) {
                    editTextLocation.setError(getString(R.string.field_empty_error));
                }
                if (!TextUtils.isEmpty(keyword) && !TextUtils.isEmpty(location)) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mEventManager.getEvents(keyword, location);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    @Override
    public void onEventsReceived(List<Event> events) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (events != null) {
            mEvents = events;
            mEventAdapter = new EventAdapter(MainActivity.this, mEvents);
            mRecyclerView.setAdapter(mEventAdapter);
            mEventAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, getString(R.string.download_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        String eventJsonData = mEventManager.getEventJsonData();
        if (!TextUtils.isEmpty(eventJsonData)) {
            savedInstanceState.putString(JSON_EVENT_KEY, eventJsonData);
        }
    }
}
