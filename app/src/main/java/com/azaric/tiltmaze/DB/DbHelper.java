package com.azaric.tiltmaze.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stefan on 17-Feb-17 | 01:37.
 * Created in project with name: "Tiltmaze"
 */

public class DbHelper extends SQLiteOpenHelper {

    //CREATE TABLE
    private static final String SQL_CREATE_TABLE_TILT_MAZE =
            "CREATE TABLE " + DBGame.GameEntry.TABLE_NAME + " (" +
            DBGame.GameEntry._ID + " INTEGER PRIMARY KEY," +
            DBGame.GameEntry.COLUMN_PLAYER_NAME + " TEXT," +
            DBGame.GameEntry.COLUMN_POLYGON_NAME + " TEXT," +
            DBGame.GameEntry.COLUMN_SCORE_TIME + " REAL);";

    //DELETE TABLE
    private static final String SQL_DELETE_TABLE_TILT_MAZE =
            "DROP TABLE IF EXISTS " + DBGame.GameEntry.TABLE_NAME;

    //DATABASE INFORMATION
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PlayerPolygonScore.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TILT_MAZE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE_TILT_MAZE);
        onCreate(sqLiteDatabase);
    }
}
