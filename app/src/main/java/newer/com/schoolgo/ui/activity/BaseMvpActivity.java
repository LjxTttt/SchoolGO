package newer.com.schoolgo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import newer.com.schoolgo.presenter.BasePresenter;

/**
 * Created by Administrator on 2017/4/24.
 */

public abstract class BaseMvpActivity<V, P extends BasePresenter<V>> extends AppCompatActivity {
    protected P mPresenter;

    protected abstract void initView();

    protected abstract int initLayoutId();

    protected abstract P initPresenter();

    protected abstract void getDataDetail();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        mPresenter.attachView((V) this);
        setContentView(initLayoutId());
        initView();
    }
}
