package com.bjh.myaccountmanager.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class StringUtil {

    /**
     * 숫자 3자리 마다 콤마 추가 (#,###)
     * @param strArgValue
     * @return
     */
    public static String convertNumberToComma(String strArgValue){
        double amount = Double.parseDouble(strArgValue);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    /**
     * 날짜포맷 yyyyMMdd 형식으로 리턴
     * @param strArgValue
     * @return
     */
    public static String getDateYYYYMMDD(long strArgValue){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return sdf.format(new Date(strArgValue));
    }

    /**
     * 날짜포맷 yyyy 형식으로 리턴
     * @param strArgValue
     * @return
     */
    public static String getDateYYYY(long strArgValue){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        return sdf.format(new Date(strArgValue));
    }

    /**
     * 현재 년도 반환
     * @return
     */
    public static String getCurYear(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 현재 월 반환
     * @return
     */
    public static String getCurMonth(){
        SimpleDateFormat format = new SimpleDateFormat("MM", Locale.getDefault());
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 현재 일 반환
     * @return
     */
    public static String getCurDay(){
        SimpleDateFormat format = new SimpleDateFormat("dd", Locale.getDefault());
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 날짜 계산 후 yyyyMMdd 형식으로 리턴
     * argAmount 값이 + 일 경우 날짜 더하기 - 일 경우 날짜 빼기
     * @param argYear
     * @param argMonth
     * @param argDay
     * @param argAmount
     * @param argCalSection
     * @return
     */
    public static String getCalculatorDay(int argYear, int argMonth, int argDay, int argAmount, int argCalSection){

        Calendar cal = new GregorianCalendar(argYear, argMonth, argDay);

        cal.add(argCalSection, argAmount);

        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        return sf.format(cal.getTime());
    }
}
