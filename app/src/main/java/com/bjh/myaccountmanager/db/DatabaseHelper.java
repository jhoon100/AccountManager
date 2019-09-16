package com.bjh.myaccountmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myAccountManager";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){

        // 버전 1일경우 신규 테이블 생성
        if(oldVersion < 1){
            db.execSQL(DatabaseColumns._CREATE0);
            db.execSQL(DatabaseColumns._CREATE1);
        }

        // 버전 2일 경우 daily_info 테이블 근무명 컬럼 추가
        if(oldVersion < 2){

            db.execSQL("ALTER TABLE "+DatabaseColumns._TABLENAME1+" ADD COLUMN "+DatabaseColumns.WORK_NM+" TEXT NOT NULL");
            db.execSQL("ALTER TABLE "+DatabaseColumns._TABLENAME0+" ADD COLUMN "+DatabaseColumns.BASE_DAY_OF_MONTH+" TEXT NOT NULL");
            /*
            db.execSQL("DROP TABLE IF EXISTS "+DatabaseColumns._TABLENAME0);
            db.execSQL("DROP TABLE IF EXISTS "+DatabaseColumns._TABLENAME1);

            db.execSQL(DatabaseColumns._CREATE0);
            db.execSQL(DatabaseColumns._CREATE1);
            */
        }
    }
}
