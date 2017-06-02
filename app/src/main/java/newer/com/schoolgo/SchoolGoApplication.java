package newer.com.schoolgo;

import android.app.Application;
import android.content.Context;

import newer.com.schoolgo.util.SPUtil;

/**
 * Created by Administrator on 2017/4/9.
 */

public class SchoolGoApplication extends Application {
    private static Context mComtext;

    public static Context getContext() {
        return mComtext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mComtext = getApplicationContext();
        SPUtil.init(mComtext, "schoogo");
    }
}
