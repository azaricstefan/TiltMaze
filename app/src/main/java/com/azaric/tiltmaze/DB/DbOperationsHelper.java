package com.azaric.tiltmaze.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
     * Get all the info about all statistics {@link DBGame.GameEntry}
     * @return {@link Cursor} cursor with the details
     */
    public Cursor getAllStatistic(){
        Cursor cursor = null;
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String columns[] = {DBGame.GameEntry._ID,
                    DBGame.GameEntry.COLUMN_PLAYER_NAME,
                    DBGame.GameEntry.COLUMN_POLYGON_NAME,
                    DBGame.GameEntry.COLUMN_SCORE_TIME};
            String orderBy = DBGame.GameEntry.COLUMN_SCORE_TIME + " DESC"; //TODO: TEST if order of statistic is good
            cursor = db.query(
                    true,
                    DBGame.GameEntry.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    orderBy,
                    null);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    /**
     * This is used for getting info about one polygon statistics.
     * @param nameOfPolygon name of the Polygon to filter
     * @return {@link Cursor} cursor with the details
     */
    public Cursor getSingleStatistic(String nameOfPolygon){
        Cursor cursor = null;
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String columns[] = {DBGame.GameEntry._ID,
                    DBGame.GameEntry.COLUMN_PLAYER_NAME,
                    DBGame.GameEntry.COLUMN_POLYGON_NAME,
                    DBGame.GameEntry.COLUMN_SCORE_TIME};
            String selection = DBGame.GameEntry.COLUMN_POLYGON_NAME+ " = ?";
            String selectionArgs[] = {nameOfPolygon};

            String orderBy = DBGame.GameEntry.COLUMN_SCORE_TIME + " DESC"; //TODO: TEST if order of statistic is good
            cursor = db.query(
                    DBGame.GameEntry.TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
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
        values.put(DBGame.GameEntry.COLUMN_PLAYER_NAME, playerName);
        values.put(DBGame.GameEntry.COLUMN_POLYGON_NAME, polygonName);
        values.put(DBGame.GameEntry.COLUMN_SCORE_TIME, scoreTime);

        return db.insert(DBGame.GameEntry.TABLE_NAME, null, values);
    }

    //DELETE

    /**
     * Delete all rows from the database
     */
    public void resetAllStatistics(){
        //TODO: TEST DB code for resetting all stats
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBGame.GameEntry.TABLE_NAME, null, null);
    }

    /**
     * Delete row with the id of the chosen statistics.
     * @param id id of the chosen statistics
     */
    public void resetStatistic(int id){
        //TODO: TEST DB code for resetting single stats
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = DBGame.GameEntry._ID + "=?" ;
        String[] whereArgs = { "" + id };
        db.delete(DBGame.GameEntry.TABLE_NAME, whereClause, whereArgs);
    }

}
