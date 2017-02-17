package com.azaric.tiltmaze.DB;

import android.provider.BaseColumns;

/**
 * Created by Stefan on 17-Feb-17 | 01:45.
 * Created in project with name: "Tiltmaze"
 */

public class DBGameModel implements BaseColumns {

    public static class GameEntry implements BaseColumns{
        public static final String TABLE_NAME = "GAME";
        public static final String COLUMN_PLAYER_NAME = "player_name";
        public static final String COLUMN_POLYGON_NAME = "polygon_name";
        public static final String COLUMN_SCORE_TIME = "time";
    }

}
