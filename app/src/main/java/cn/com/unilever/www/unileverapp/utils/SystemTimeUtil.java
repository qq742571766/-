package cn.com.unilever.www.unileverapp.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @class 获取系统时间
 * @name 林郝
 * @anthor QQ:742571766
 * @time 2017/7/4 11:11
 */
public class SystemTimeUtil {
    public static String getErrorDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sDateFormat.format(new java.util.Date());
    }

    public static String getCloseDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sDateFormat.format(new java.util.Date());
    }
}
