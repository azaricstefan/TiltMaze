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

import java.io.File;

public class MainActivity extends Activity
        implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    public static final String NAME_OF_DRAWING = "com.azaric.tiltmaze.MainActivity.NAME_OF_DRAWING";

    //GUI
    Button mainButton;
    ListView listView;
    ArrayAdapter<String> adapter;

    String[] namesOfDrawings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainButton = (Button) findViewById(R.id.buttonMain);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameActivity("");
            }
        });

        addDrawingsToList();
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

    private void updateListView(){
        namesOfDrawings = getApplicationContext().getFilesDir().list(new java.io.FilenameFilter() {
            /**
             * Ovo sam napravio iz razloga sto bez ovoga mi izlistava neke sistemske fajlove iz
             * default foldera.
             * @param dir
             * @param filename
             * @return
             */
            @Override
            public boolean accept(File dir, String filename) {
                return !(filename.contains("run") || filename.contains("rList-com"));
            }
        });
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namesOfDrawings);
        listView.setAdapter(adapter);
    }

    private void addDrawingsToList() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        listView = new ListView(getApplicationContext());
        updateListView();

        listView.setLayoutParams(lp);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }


    private void startGameActivity(String drawingNameToOpen){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(NAME_OF_DRAWING, drawingNameToOpen);
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
        String drawingNameToOpen = (String) parent.getItemAtPosition(position);
        startGameActivity(drawingNameToOpen);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: obradi meni za long klik
        return false;
    }
}
