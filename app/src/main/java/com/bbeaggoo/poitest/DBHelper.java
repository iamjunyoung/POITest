package com.bbeaggoo.poitest;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.bbeaggoo.poitest.DatabaseContract.DATABASE_CREATE;
import static com.bbeaggoo.poitest.DatabaseContract.DB_FILE_NAME;
import static com.bbeaggoo.poitest.DatabaseContract.DB_VERSION;
import static com.bbeaggoo.poitest.DatabaseContract.TABLE_POIS;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    /*
    ② MyDB의 onUpgrade()가 호출되는 시점
       ⇒ super(context,"Test.db", null, 2); 의 맨 마지막 인자인 DB의
       version 값이 상향 조정될 때 호출된다.
     */
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    /*
    ① MyDB dbHelp = new MyDB(this);
      ⇒ 이 단계에서는 SQLiteOpenHelper를 상속 받은 MyDB의 생성자만 호출이
      되고 MyDB의 onCreate()는 아직 호출이 안된다.

      SQLiteDatabase db = dbHelp.getReadableDatabase() 혹은
      dbHelp.getWritableDatabase()가 호출 될 때 비로소
      MyDB의 onCreate()가 호출되서 테이블이 생성이 된다.

      getReadableDatabase()하는 시점에서 테이블이 없으면
      테이블이 새롭게 생성이 되고 기존 존재하면 그것을 Radable이나 Writable 중
      하나로 open한다.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POIS);
        onCreate(db);
    }


}
