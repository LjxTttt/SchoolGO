package newer.com.schoolgo.presenter;

import java.util.List;

import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.bean.School;
import newer.com.schoolgo.modle.ISchoolItemModle;
import newer.com.schoolgo.net.NetMannger;
import newer.com.schoolgo.rx.RxManager;
import newer.com.schoolgo.rx.RxObserver;
import newer.com.schoolgo.ui.view.SchoolItemView;

/**
 * Created by Administrator on 2017/3/8.
 */

public class SchoolItemPresenter extends BasePresenter<SchoolItemView> {
    private final int PAGE_NUMS = 15;
    public int pageNow = 1;
    private ISchoolItemModle mModle;

    public SchoolItemPresenter() {
        mModle = NetMannger.getInstance().createConnection(ISchoolItemModle.class);
    }

    public void getSchoolItemData(int typeId) {
        RxManager.getInstance().doSubscribeByResult(mModle.getSchoolItem(typeId, PAGE_NUMS, pageNow), new RxObserver<List<School>>() {
            @Override
            public void _OnNext(List<School> schools) {
                mView.onSuccess(schools);
            }

            @Override
            public void _OnError(Throwable e) {
                mView.OnError(ErrorTag.ERROR_SCHOOL_ERROR);
            }
        });
    }
}
