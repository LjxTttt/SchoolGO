package newer.com.schoolgo.ui.view;

import newer.com.schoolgo.bean.User;

/**
 * Created by Administrator on 2017/5/5.
 */

public interface LoginView extends IBaseView {
    void onLoginSuccess(User user);
}
