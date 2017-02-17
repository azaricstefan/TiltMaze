package com.azaric.tiltmaze;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.azaric.tiltmaze.DB.DbOperationsHelper;

public class StatisticsActivity extends Activity {

    DbOperationsHelper dbOperationsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dbOperationsHelper = new DbOperationsHelper(this);
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
                //TODO: DELETE ALL STATISTICS and return main activity
                dbOperationsHelper.resetAllStatistics();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
