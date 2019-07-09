package com.bjh.myaccountmanager.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
}
