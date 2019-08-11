package com.example.news.utils.widget;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 时间记录类
 */
public class UIUtils {

    /**
     * 设置上次数据更新时间
     * @param listView
     * @param key
     * @param context
     */
    public static void setPullToRefreshLastUpdated(PullToRefreshListView listView, String key, Context context) {
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(context);
        long lastUpdateTimeStamp = helper.getLongValue(key);
        listView.setLastUpdateTime(getUpdateTimeString(lastUpdateTimeStamp));
    }

    public static void savePullToRefreshLastUpdateAt(PullToRefreshListView listView, String key, Context context) {
        listView.onRefreshComplete();
        SharedPreferencesHelper helper = SharedPreferencesHelper.getInstance(context);
        long lastUpdateTimeStamp = System.currentTimeMillis();
        helper.putLongValue(key, lastUpdateTimeStamp);
        listView.setLastUpdateTime(getUpdateTimeString(lastUpdateTimeStamp));
    }

    /**
     * 更新时间字符串
     * @param timestamp
     * @return
     */
    public static String getUpdateTimeString(long timestamp) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        if (timestamp <= 0) {
            return "上次更新时间:";
        } else {
            SimpleDateFormat format = null;
            String textDate = "上次更新时间:";
            Calendar calendar = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(timestamp);
            if (c.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                    && c.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                    && c.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
                format = new SimpleDateFormat("HH:mm:ss");
                return textDate += format.format(c.getTime());
            } else if (c.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                format = new SimpleDateFormat("MM/dd HH:mm");
                return textDate += format.format(c.getTime());
            } else {
                format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                return textDate += format.format(c.getTime());
            }
        }
    }

}
