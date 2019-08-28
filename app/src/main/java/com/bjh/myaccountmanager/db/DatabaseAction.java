package com.bjh.myaccountmanager.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAction {

    /**
     * Base insert
     * @param db
     * @param timeSection
     * @param baseTime
     * @param baseAmount
     * @return
     */
    public long insertBaseColumn(SQLiteDatabase db, String timeSection, String baseTime, long baseAmount, String baseDayOfMonth){
        ContentValues values = new ContentValues();
        values.put(DatabaseColumns.TIME_SECTION, timeSection);
        values.put(DatabaseColumns.BASE_TIME, baseTime);
        values.put(DatabaseColumns.BASE_AMOUNT, baseAmount);
        values.put(DatabaseColumns.BASE_DAY_OF_MONTH, baseDayOfMonth);
        return db.insert(DatabaseColumns._TABLENAME0, null, values);
    }

    /**
     * Base update
     * @param db
     * @param timeSection
     * @param baseTime
     * @param baseAmount
     * @return
     */
    public long updateBaseColumn(SQLiteDatabase db, String timeSection, String baseTime, long baseAmount, String baseDayOfMonth){
        ContentValues values = new ContentValues();
        values.put(DatabaseColumns.TIME_SECTION, timeSection);
        values.put(DatabaseColumns.BASE_TIME, baseTime);
        values.put(DatabaseColumns.BASE_AMOUNT, baseAmount);
        values.put(DatabaseColumns.BASE_DAY_OF_MONTH, baseDayOfMonth);
        return db.update(DatabaseColumns._TABLENAME0, values, DatabaseColumns._ID+" = ?", new String[]{Integer.toString(1)});
    }

    /**
     * Daily insert
     * @param db
     * @param workNm
     * @param workDay
     * @param workTime
     * @param workAmount
     * @return
     */
    public long insertDailyColumn(SQLiteDatabase db, String workDay, String workNm, String workTime, long workAmount){
        ContentValues values = new ContentValues();
        values.put(DatabaseColumns.WORK_NM, workNm);
        values.put(DatabaseColumns.WORK_DAY, workDay);
        values.put(DatabaseColumns.WORK_TIME, workTime);
        values.put(DatabaseColumns.WORK_AMOUNT, workAmount);
        return db.insert(DatabaseColumns._TABLENAME1, null, values);
    }

    /**
     * Daily update
     * @param db
     * @param workNm
     * @param workDay
     * @param workTime
     * @param workAmount
     * @return
     */
    public long updateDailyColumn(SQLiteDatabase db, String workDay, String workNm, String workTime, long workAmount){
        ContentValues values = new ContentValues();
        values.put(DatabaseColumns.WORK_NM, workNm);
        values.put(DatabaseColumns.WORK_TIME, workTime);
        values.put(DatabaseColumns.WORK_AMOUNT, workAmount);
        return db.update(DatabaseColumns._TABLENAME1, values, DatabaseColumns.WORK_DAY+" = ?", new String[]{workDay});
    }
}
