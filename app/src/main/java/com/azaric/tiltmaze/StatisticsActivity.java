package com.azaric.tiltmaze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.azaric.tiltmaze.DB.DBGame;
import com.azaric.tiltmaze.DB.DbOperationsHelper;

import java.io.File;
import java.util.HashMap;

public class StatisticsActivity extends Activity
        implements
        AdapterView.OnItemClickListener{

    public static final String STATISTICS_SINGLE_TRACK = "com.azaric.tiltmaze.StatisticsActivity.STATISTICS_SINGLE_TRACK";

    TextView titleTextView;

    DbOperationsHelper dbOperationsHelper;

    ListView listView;
    ArrayAdapter<String> adapter;
    String[] namesOfTracks;
    String nameOfTrack = null;
    SingleStatsCursorAdapter myCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //GET GUI ELEMENTS
        titleTextView = (TextView) findViewById(R.id.statisticsTitleTextView);
        listView = (ListView) findViewById(R.id.statisticsListView);

        dbOperationsHelper = new DbOperationsHelper(this);

        Intent intent = getIntent();
        if(intent.hasExtra(STATISTICS_SINGLE_TRACK)){
            nameOfTrack = intent.getStringExtra(STATISTICS_SINGLE_TRACK);
            titleTextView.setText("Statistics for track: " + nameOfTrack);
            //dodaj sve podatke o JEDNOJ statistici
            myCursorAdapter = new SingleStatsCursorAdapter(this,dbOperationsHelper.getSingleStatistic(nameOfTrack));
            listView.setAdapter(myCursorAdapter);
        }else {
            //ovde ide obrada svih podataka o svim statistikama
            addTracksToList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.statistics_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_statistic_for_selected_polygon:
                //TODO: DELETE STATISTICS FOR SELECTED POLYGON and return main activity
                dbOperationsHelper.resetStatistic(item.getItemId()); //DEBUG
                return true;
            case R.id.delete_all_statistics:
                dbOperationsHelper.resetAllStatistics();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //CODE FOR LIST VIEW

    private void updateListView(){
        //TODO: citaj iz baze naziv poligona
        Cursor cursor = dbOperationsHelper.getAllStatistic();
        if(cursor != null) {
            namesOfTracks = new String[cursor.getCount()]; int i = 0;
            while (cursor.moveToNext()) {
                String polygonName = cursor.getString(cursor.getColumnIndex(DBGame.GameEntry.COLUMN_POLYGON_NAME));
                namesOfTracks[i++] = polygonName;
            }
        } else namesOfTracks = new String[] {"EMPTY DELETE LATER"};
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namesOfTracks);
        listView.setAdapter(adapter);
    }

    /**
     * Called only once in the method onCreate.
     */
    private void addTracksToList() {
        updateListView();
        listView.setOnItemClickListener(this);
    }

    /**
     * Ova metoda je potrebna da prepozna kad se klikne na item u listView-u
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String nameOfTrack = (String) parent.getItemAtPosition(position);

        myCursorAdapter = new SingleStatsCursorAdapter(this,dbOperationsHelper.getSingleStatistic(nameOfTrack));
        listView.setAdapter(myCursorAdapter);
        //OPEN STATS FOR POLYGON AND SHOW THEM
        //TODO: TEST update StatisticsActivity with details about the polygon


    }

    public class SingleStatsCursorAdapter extends CursorAdapter{

        public SingleStatsCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String playerName = cursor.getString(cursor.getColumnIndex(DBGame.GameEntry.COLUMN_PLAYER_NAME));
            double result = cursor.getDouble(cursor.getColumnIndex(DBGame.GameEntry.COLUMN_SCORE_TIME));
            TextView textView = (TextView)view;
            textView.setText("Player name: " + playerName + "Result: " + result );

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View v = new TextView(context);
            bindView(v, context, cursor);
            return v;
        }

    }
}
