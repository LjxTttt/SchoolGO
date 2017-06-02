package newer.com.schoolgo.presenter;

import java.util.List;

import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.bean.HttpResult;
import newer.com.schoolgo.bean.Moment;
import newer.com.schoolgo.modle.IAddComentModle;
import newer.com.schoolgo.modle.IMomentListModle;
import newer.com.schoolgo.net.NetMannger;
import newer.com.schoolgo.rx.RxManager;
import newer.com.schoolgo.rx.RxObserver;
import newer.com.schoolgo.ui.view.IMomentListView;

/**
 * Created by Administrator on 2017/5/5.
 */

public class MomentListPre extends BasePresenter<IMomentListView> {
    IMomentListModle modle;
    IAddComentModle addModle;

    public MomentListPre() {
        modle = NetMannger.getInstance().createConnection(IMomentListModle.class);
    }

    public void getListMoment() {
        RxManager.getInstance().doSubscribe(modle.getListMoment(), new RxObserver<List<Moment>>() {
            @Override
            public void _OnNext(List<Moment> moment) {
                mView.onListSucess(moment);
            }

            @Override
            public void _OnError(Throwable e) {
                mView.OnError(ErrorTag.ERROR_REG_SEV);
            }
        });
    }

    public void addComent(String name, String content, long tag) {
        addModle = NetMannger.getInstance().createConnection(IAddComentModle.class);
        RxManager.getInstance().doSubscribe(addModle.addComent(content, tag, name), new RxObserver<HttpResult>() {
            @Override
            public void _OnNext(HttpResult httpResult) {
                if (httpResult.getMessage().equals("true")) {
                    mView.addComentSucess();
                }
            }

            @Override
            public void _OnError(Throwable e) {
                mView.OnError(ErrorTag.ERROR_MOMENT_LIST);
            }
        });
    }

}
