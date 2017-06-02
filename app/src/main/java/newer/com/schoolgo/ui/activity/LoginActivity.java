package newer.com.schoolgo.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.User;
import newer.com.schoolgo.presenter.LoginPresenter;
import newer.com.schoolgo.ui.view.LoginView;
import newer.com.schoolgo.util.DateUtil;
import newer.com.schoolgo.util.SPUtil;
import newer.com.schoolgo.util.ToastUtil;

public class LoginActivity extends BaseMvpActivity<LoginView, LoginPresenter> implements LoginView {

    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String USER_ID = "userId";
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_go)
    Button btGo;
    @BindView(R.id.cv)
    CardView cv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private Unbinder unbinder;
    private ActivityOptionsCompat oc2;

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void getDataDetail() {
        mPresenter.login(etUsername.getText().toString(), etPassword.getText().toString());
    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                Explode explode = new Explode();
                explode.setDuration(500);
                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);

                if (etUsername.getText().length() == 0) {
                    ToastUtil.showToast(this, "用户名不能为空");
                } else if (etPassword.getText().length() == 0) {
                    ToastUtil.showToast(this, "密码不能为空");
                } else {
                    getDataDetail();
                }
                break;
        }
    }

    @Override
    public void onLoginSuccess(User user) {
        Intent i2 = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(USER_NAME, user.getUsername());
        bundle.putLong(USER_ID, user.getUserId());
        i2.putExtras(bundle);
        i2.putExtras(oc2.toBundle());
        SPUtil.save(USER_NAME, user.getUsername());
        SPUtil.save(USER_ID, user.getUserId());
        SPUtil.save("lastTime", DateUtil.getSystemTime());
        setResult(1, i2);
        ToastUtil.showToast(this, "登录成功");
        finish();
    }


    @Override
    public void OnError(ErrorTag errorTag) {
        switch (errorTag) {
            case ERROR_LOGIN_NAME:
                ToastUtil.showToast(this, "用户名或密码错误");
                break;
            case ERROR_REG_SEV:
                ToastUtil.showToast(this, "服务器暂时不可用");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
