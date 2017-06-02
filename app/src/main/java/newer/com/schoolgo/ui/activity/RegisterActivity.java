package newer.com.schoolgo.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.User;
import newer.com.schoolgo.presenter.RegPresenter;
import newer.com.schoolgo.ui.view.RegView;
import newer.com.schoolgo.util.DateUtil;
import newer.com.schoolgo.util.SPUtil;
import newer.com.schoolgo.util.ToastUtil;

public class RegisterActivity extends BaseMvpActivity<RegView, RegPresenter> implements RegView {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.cv_add)
    CardView cvAdd;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_go)
    Button btGo;
    @BindView(R.id.et_repeatpassword)
    EditText etRepeatpass;
    private Unbinder unbinder;

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @OnClick(R.id.bt_go)
    public void onClick(View view) {
        if (etUsername.getText().length() == 0) {
            ToastUtil.showToast(this, "用户名不能为空");
        } else if (etPassword.getText().length() == 0) {
            ToastUtil.showToast(this, "密码不能为空");
        } else if (!etPassword.getText().toString().trim().
                equals(etRepeatpass.getText().toString().trim())) {
            ToastUtil.showToast(this, "两次密码不相同");
        } else {
            getDataDetail();
        }
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected RegPresenter initPresenter() {
        return new RegPresenter();
    }

    @Override
    protected void getDataDetail() {
        mPresenter.regYouAccount(etUsername.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void RegSucess(User user) {
        ToastUtil.showToast(this, "注册成功");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(LoginActivity.USER_NAME, user.getUsername());
        intent.putExtra(LoginActivity.USER_ID, user.getUserId());
        SPUtil.clear();
        SPUtil.save(LoginActivity.USER_NAME, user.getUsername());
        SPUtil.save(LoginActivity.USER_ID, user.getUserId());
        SPUtil.save("lastTime", DateUtil.getSystemTime());
        startActivity(intent);
    }


    @Override
    public void OnError(ErrorTag errorTag) {
        if (errorTag == ErrorTag.ERROR_REG_EXISTS) {
            ToastUtil.showToast(this, "您当前的用户名已被注册");
        } else {
            ToastUtil.showToast(this, "当前服务器可能存在问题");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
    }
}
