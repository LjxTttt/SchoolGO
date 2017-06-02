package newer.com.schoolgo.presenter;

import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.bean.User;
import newer.com.schoolgo.modle.ILoginOrRegModle;
import newer.com.schoolgo.net.NetMannger;
import newer.com.schoolgo.rx.RxManager;
import newer.com.schoolgo.rx.RxObserver;
import newer.com.schoolgo.ui.view.RegView;

/**
 * Created by Administrator on 2017/5/5.
 */

public class RegPresenter extends BasePresenter<RegView> {
    private ILoginOrRegModle modle;

    public RegPresenter() {
        modle = NetMannger.getInstance().createConnection(ILoginOrRegModle.class);
    }

    public void regYouAccount(String username, String password) {
        RxManager.getInstance().doSubscribe(modle.loginOrReg(username, password, 0), new RxObserver<User>() {
            @Override
            public void _OnNext(User user) {
                if (user.getMessage().equals("注册成功")) {
                    mView.RegSucess(user);
                } else {
                    mView.OnError(ErrorTag.ERROR_REG_EXISTS);
                }
            }

            @Override
            public void _OnError(Throwable e) {
                mView.OnError(ErrorTag.ERROR_REG_SEV);
            }
        });
    }
}
