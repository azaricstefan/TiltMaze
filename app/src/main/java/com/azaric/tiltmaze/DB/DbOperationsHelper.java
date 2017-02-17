package com.azaric.tiltmaze.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.azaric.tiltmaze.DB.DBGameModel;
/**
 * Created by Stefan on 17-Feb-17 | 01:57.
 * Created in project with name: "Tiltmaze"
 */

public class DbOperationsHelper {
    private DbHelper dbHelper;
    private Context context;

    public DbOperationsHelper(Context context){
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    //SELECT

    /**
     * SELECT * FROM {@link com.azaric.tiltmaze.DB.DBGameModel.GameEntry}
     * @return
     */
    public Cursor getAllStatistic(){
        Cursor cursor = null;
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String columns[] = {DBGameModel.GameEntry._ID,
                    DBGameModel.GameEntry.COLUMN_PLAYER_NAME,
                    DBGameModel.GameEntry.COLUMN_POLYGON_NAME,
                    DBGameModel.GameEntry.COLUMN_SCORE_TIME};
            String orderBy = DBGameModel.GameEntry.COLUMN_SCORE_TIME + " DESC"; //TODO: check if order of statistic is good
            cursor = db.query(
                    DBGameModel.GameEntry.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    orderBy
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }


    //INSERT

    /**
     * Insert new row into the database.
     * @param playerName Player name to insert
     * @param polygonName Polygon name to insert
     * @param scoreTime Time that took player to finish the polygon
     * @return {@link Long} ID of the inserted row
     */
    public long insert(String playerName, String polygonName, Float scoreTime){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBGameModel.GameEntry.COLUMN_PLAYER_NAME, playerName);
        values.put(DBGameModel.GameEntry.COLUMN_POLYGON_NAME, polygonName);
        values.put(DBGameModel.GameEntry.COLUMN_SCORE_TIME, scoreTime);

        return db.insert(DBGameModel.GameEntry.TABLE_NAME, null, values);
    }

    //DELETE
    public void resetAllStatistics(){
        //TODO: DB code for reseting all stats
    }

    public void resetStatistic(int id){
        //TODO: DB code for reseting single stats
    }
}
