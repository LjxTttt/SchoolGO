package newer.com.schoolgo.presenter;

import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.bean.User;
import newer.com.schoolgo.modle.ILoginOrRegModle;
import newer.com.schoolgo.net.NetMannger;
import newer.com.schoolgo.rx.RxManager;
import newer.com.schoolgo.rx.RxObserver;
import newer.com.schoolgo.ui.view.LoginView;

/**
 * Created by Administrator on 2017/5/5.
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    private ILoginOrRegModle modle;

    public LoginPresenter() {
        modle = NetMannger.getInstance().createConnection(ILoginOrRegModle.class);
    }

    public void login(String usename, String password) {
        RxManager.getInstance().doSubscribe(modle.loginOrReg(usename, password, 1),
                new RxObserver<User>() {
                    @Override
                    public void _OnNext(User user) {
                        if (user.getMessage().equals("密码或用户名错误")) {
                            mView.OnError(ErrorTag.ERROR_LOGIN_NAME);
                        } else {
                            mView.onLoginSuccess(user);
                        }
                    }

                    @Override
                    public void _OnError(Throwable e) {
                        mView.OnError(ErrorTag.ERROR_REG_SEV);
                    }
                });
    }
}
