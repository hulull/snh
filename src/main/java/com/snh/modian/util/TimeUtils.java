package com.snh.modian.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
    public static long StringToLong(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long getZeroClock() {
        return getHourClock(0);
    }

    public static long getSevenClock() {
        return getHourClock(7);
    }

    public static long getHourClock(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, n);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//        System.out.println(calendar.getTime());
        return calendar.getTimeInMillis();
    }

    public static long getAfterHalfAnHour() {
        long INTERVAL = 1000 * 60 * 30;
        return System.currentTimeMillis() + INTERVAL;
    }
}
