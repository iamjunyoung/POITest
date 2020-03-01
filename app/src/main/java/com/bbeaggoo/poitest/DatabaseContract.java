package com.bbeaggoo.poitest;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    //Database schema information
    public static int DB_VERSION = 1;
    public static final String DB_FILE_NAME = "pois.db";
    public static final String TABLE_POIS = "pois";

    public static String DATABASE_CREATE = "create table IF NOT EXISTS " +
            TABLE_POIS + " (" +
            POIColumns.ID + " integer primary key autoincrement, " +
            POIColumns.POI_ID + " text, " +
            POIColumns.FLOOR_ID + " text, " +
            POIColumns.FLOOR_NAME_EN + " text, " +
            POIColumns.FLOOR_NAME_KR + " text, " +
            POIColumns.FLOOR_ORDER + " integer, " +
            POIColumns.FLOOR_INDEX + " integer, " +
            POIColumns.ATTRIBUTE_EL_ID + " text, " +
            POIColumns.ATTRIBUTE_EL_VENDER + " text, " +
            POIColumns.ATTRIBUTE_EL_FLOOR_LIST + " text, " +
            POIColumns.ATTRIBUTE_DESC + " text, " +
            POIColumns.ATTRIBUTE_TEL + " text, " +
            POIColumns.RADIUS + " integer, " +
            POIColumns.IS_RESTRICTED + " integer, " +
            POIColumns.NAME_KR + " text, " +
            POIColumns.POSITION_X + " real, " + // real
            POIColumns.POSITION_Y + " real, " + // real
            POIColumns.POSITION_Z + " integer, " +
            POIColumns.THETA + " integer, " +
            POIColumns.TYPE + " integer, " +
            POIColumns.IS_HOME + " text, " +
            POIColumns.IS_CHARGER + " text, " +
            POIColumns.IS_IN_POILIST + " text);";

    public static final String DB_DROP = "DROP TABLE IF EXIST " + TABLE_POIS;


    public static final class POIColumns implements BaseColumns {
        public static final String ID = "_id";
        public static final String POI_ID = "poi_id";
        public static final String FLOOR_ID = "floor_id";
        public static final String FLOOR_NAME_EN = "floor_name_en";
        public static final String FLOOR_NAME_KR = "floor_name_kr";
        public static final String FLOOR_ORDER = "floor_order";
        public static final String FLOOR_INDEX = "floor_index";
        public static final String ATTRIBUTE_EL_ID = "attribute_el_id";
        public static final String ATTRIBUTE_EL_VENDER = "attribute_el_vendor";
        public static final String ATTRIBUTE_EL_FLOOR_LIST = "attribute_el_floor_list";
        public static final String ATTRIBUTE_DESC = "attribute_desc";
        public static final String ATTRIBUTE_TEL = "attribute_tel";
        public static final String RADIUS = "radius";
        public static final String IS_RESTRICTED = "is_restricted";
        public static final String NAME_KR = "name_kr";
        public static final String POSITION_X = "position_x";
        public static final String POSITION_Y = "position_y";
        public static final String POSITION_Z = "position_z";
        public static final String THETA = "theta";
        public static final String TYPE = "type";
        public static final String IS_HOME = "is_home";
        public static final String IS_CHARGER = "is_charger";
        public static final String IS_IN_POILIST = "is_in_poi_list";
    }

    public static final String CONTENT_AUTHORITY = "com.bbeaggoo.poitest";
    //Base content Uri for accessing the provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_POIS)
            .build();

    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }

}
