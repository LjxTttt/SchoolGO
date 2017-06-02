package newer.com.schoolgo.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 樱花满地，集于我心 on 2016/11/29 0029.
 */
//单例模式的Toast
public class ToastUtil {
    private static long firstTime;
    private static long lastTime;
    private static Toast mToast = null;
    private static String oldStr;

    public static void showToast(Context context, String str) {
        if (mToast == null) {
            mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            mToast.show();
            firstTime = System.currentTimeMillis();
        } else {
            lastTime = System.currentTimeMillis();
            if (str.equals(oldStr)) {
                //如果现在的消息与之前的消息一样并且时间减去之前消息的时间小于最短
                //Toast时间则覆盖前面的消息
                if (lastTime - firstTime > Toast.LENGTH_SHORT) {
                    mToast.show();
                }
            } else {
                oldStr = str;
                mToast.setText(str);
                mToast.show();
            }
        }
        firstTime = lastTime;
    }
}
