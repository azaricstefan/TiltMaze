package com.azaric.tiltmaze;

import android.app.Activity;
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

    HashMap<String,Long> polygonNamesAndIds = new HashMap<>();

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
            //ListAdapter adapter = new ListAdapter(this, R.layout.item_score_list, scores);
            listView.setAdapter(adapter);
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

    /**
     * Get raw names of tracks from {@link DbOperationsHelper#getAllStatisticStrings()}. <br/>
     * Parse them and get names and ids.<br/>
     * Save data to {@link HashMap} {@link StatisticsActivity#polygonNamesAndIds} (we will use given IDs in this method
     * {@link StatisticsActivity#onItemClick(AdapterView, View, int, long)} in the {@link StatisticsActivity#listView}
     */
    private void updateListView(){
        //TODO: citaj iz baze naziv poligona i id zbog ucitavanja statistike posle!
        String[] rawNamesOfTracks = dbOperationsHelper.getAllStatisticStrings();
        if(rawNamesOfTracks != null) {
            namesOfTracks = new String[rawNamesOfTracks.length];
            for (int i = 0; i < rawNamesOfTracks.length; i++) {
                String[] s = rawNamesOfTracks[i].split(":");
                String nameOfThePolygon = s[0];
                long idOfThePolygon = Long.parseLong(s[1]);
                polygonNamesAndIds.put(nameOfThePolygon, idOfThePolygon); //add to hashmap so we could get id later when clicked on that item in list view
                namesOfTracks[i] = nameOfThePolygon; //add here the name so we can add it later to the adapter
            }
        } else namesOfTracks = new String[] {"EMPTY"};
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
        Cursor cursor = null;

        Long savedID = polygonNamesAndIds.get(nameOfTrack);
        if(savedID != null)
            cursor = dbOperationsHelper.getSingleStatistic(savedID);
        else
            throw new IllegalStateException();

        //TODO: update StatisticsActivity with details about the polygon

    }
}
