package com.bjh.myaccountmanager.db;

import android.provider.BaseColumns;

public class DatabaseColumns implements BaseColumns {
    public static final String TIME_SECTION = "time_section";  /* 기준 시/분 구분 */
    public static final String BASE_TIME = "base_time";        /* 기준 시간 */
    public static final String BASE_AMOUNT = "base_amount";    /* 기준 금액 */

    public static final String WORK_DAY = "work_day";          /* 근무일자 */
    public static final String WORK_TIME = "work_time";        /* 근무시간 */
    public static final String WORK_AMOUNT = "work_amount";    /* 근무금액 */

    public static final String _TABLENAME0 = "base_info";
    public static final String _TABLENAME1 = "daily_info";

    public static final String _CREATE0 = "create table "+_TABLENAME0+" ("+_ID+" integer primary key autoincrement, "+TIME_SECTION+" text not null, "+BASE_TIME+" text not null, "+BASE_AMOUNT+" integer not null)";
    public static final String _CREATE1 = "create table "+_TABLENAME1+" ("+_ID+" integer primary key autoincrement, "+WORK_DAY+" text not null, "+WORK_TIME+" text not null, "+WORK_AMOUNT+" integer not null)";
}
