package com.bbeaggoo.poitest;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    //Database schema information
    public static final String TABLE_POIS = "pois";

            /*
         private String poiId;

         private String floorId;
         private FloorName floorName;
             private String en;
             private String kr;

         private int                 floorOrder;
         private Attribute attributes;
             private String elevatorID;
             private String elevatorVendor;
             private String elvatorFloorList;
             private String desc;
             private String tel;

         private int                 radius;
         private int                 isRestricted;
         private Name                name;
             private String kr;

         private Position pos;
             private int x;
             private int y;
             private int z;

         private int                 theta;
         private int                 type;
         private boolean             isHome;
         private boolean             isDock;

          */

    public static final class POIColumns implements BaseColumns {

        public static final String _ID = "poi_id";
        public static final String FLOOR_ID = "floor_id";
        public static final String FLOOR_NAME_EN = "floor_name_en";
        public static final String FLOOR_NAME_KR = "floor_name_kr";
        public static final String FLOOR_ORDER = "floor_order";
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
        public static final String IS_HOME = "is_home";
        public static final String IS_CHARGER = "is_charger";
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
