package newer.com.schoolgo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/3.
 */

public class DateUtil {

    public static String DatetoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = format.format(date);
        return fileName;
    }

    public static boolean isDestroyLoginInfo(long lastTime) {
        long three = 1000 * 60 * 60 * 24 * 3;
        long now = System.currentTimeMillis();
        return now > three + lastTime;
    }

    public static long getSystemTime() {
        return System.currentTimeMillis();
    }
}
