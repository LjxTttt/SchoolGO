package newer.com.schoolgo.util;

import android.content.Context;
import android.util.Log;

import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/3.
 */

public class QiNiuUtil {
    static Context mContext;
    private static OnUploadCompleteListener completeListener;

    public static Configuration configQiniu() {
        Configuration cfg = new Configuration.Builder()
                .chunkSize(256 * 1024)
                .putThreshhold(521 * 1024)
                .connectTimeout(20)
                .responseTimeout(60)
                .zone(Zone.zone2)
                .build();
        return cfg;
    }

    public static void upLoadImag(String filePath, String token, Configuration cfg,
                                  Context context) {
        mContext = context;
        UploadManager uploadManager = new UploadManager(cfg);
        String key = "moment/IMG" + DateUtil.DatetoFileName();
        uploadManager.put(filePath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    Log.d("TAG", "sucess  key=" + key + "   info=" + info + "   res" + response);
                    if (completeListener != null) {
                        completeListener.upLoadSuceess(key);
                    }
                } else {
                    ToastUtil.showToast(mContext, "Fail");
                    Log.d("TAG", "fail  key=" + key + "   info=" + info + "   res" + response);
                    completeListener.upLoadError(info, response);
                }
            }
        }, null);

    }

    public static void setUploadCompleteListener(OnUploadCompleteListener listener) {
        completeListener = listener;
    }

    public interface OnUploadCompleteListener {
        void upLoadSuceess(String key);

        void upLoadError(ResponseInfo info, JSONObject response);
    }

}
