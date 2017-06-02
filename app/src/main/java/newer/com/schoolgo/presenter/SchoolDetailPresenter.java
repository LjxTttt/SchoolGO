package newer.com.schoolgo.presenter;

import android.util.Log;

import newer.com.schoolgo.modle.ISchoolDetailModle;
import newer.com.schoolgo.net.NetMannger;
import newer.com.schoolgo.rx.RxManager;
import newer.com.schoolgo.rx.RxObserver;
import newer.com.schoolgo.ui.view.SchoolDetailView;

/**
 * Created by Administrator on 2017/4/24.
 */

public class SchoolDetailPresenter extends BasePresenter<SchoolDetailView> {
    private ISchoolDetailModle iSchoolDetailModle;

    public SchoolDetailPresenter() {
        iSchoolDetailModle = NetMannger.getInstance().createConn
                (ISchoolDetailModle.class);
    }

    public void getSchoolDetailData(String content) {
        RxManager.getInstance().doSubscribe(iSchoolDetailModle.getSchoolDetailData(content), new RxObserver<String>() {
            @Override
            public void _OnNext(String responseBody) {
                mView.OnSuccess(responseBody);
            }

            @Override
            public void _OnError(Throwable e) {
                Log.d("TAG", "eeeee=" + e.getMessage());
            }
        });
    }
}
