package com.azaric.tiltmaze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity
        implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    public static final String NAME_OF_POLYGON = "com.azaric.tiltmaze.MainActivity.NAME_OF_DRAWING";

    //GUI
    ListView listView;
    TextView textView;
    ArrayAdapter<String> adapter;

    String[] namesOfTracks;
    String nameOfTrack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Tilt maze MAIN");

        textView = (TextView) findViewById(R.id.textViewMainStatus);
        listView = (ListView) findViewById(R.id.mainActivityListView);
        addTracksToList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_terrain:
                startActivity(new Intent(this, NewTerrainActivity.class));
                return true;
            case R.id.show_stats:
                startActivity(new Intent(this, StatisticsActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Ovo se poziva kada se vrati na ovu aktivnost
     */
    @Override
    protected void onResume() {
        updateListView();
        super.onResume();
    }

    //CODE FOR LIST VIEW
    private void updateListView(){
        //citaj sve fajlove iz files direktorijuma
        namesOfTracks = getApplicationContext().getExternalFilesDir(null).list();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namesOfTracks);
        listView.setAdapter(adapter);
        if(namesOfTracks.length == 0){
            textView.setText("Nema poligona raspolozivih");
        } else textView.setText("");
    }

    private void addTracksToList() {
        updateListView();
        listView.setOnItemClickListener(this);
    }


    private void startGameActivity(String drawingNameToOpen){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(NAME_OF_POLYGON, drawingNameToOpen);
        startActivity(intent);
    }

    /**
     * Ova metoda je potrebna da prepozna kad se klikne na item u listView-u
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String nameOfTrack = (String) parent.getItemAtPosition(position);
        startGameActivity(nameOfTrack);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: obradi meni za long klik
        return false;
    }
}
