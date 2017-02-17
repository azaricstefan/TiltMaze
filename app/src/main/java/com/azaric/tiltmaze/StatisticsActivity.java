package com.azaric.tiltmaze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class StatisticsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
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
                return true;
            case R.id.delete_all_statistics:
                //TODO: DELETE ALL STATISTICS and return main activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
