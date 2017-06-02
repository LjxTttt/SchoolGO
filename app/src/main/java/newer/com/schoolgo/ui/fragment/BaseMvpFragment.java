package newer.com.schoolgo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import newer.com.schoolgo.presenter.BasePresenter;

/**
 * Created by Administrator on 2017/3/22.
 */

public abstract class BaseMvpFragment<P extends BasePresenter> extends Fragment {
    public View mRootView;
    protected P mPresenter;
    protected boolean isUserVisible;
    protected boolean isViewInited;
    protected boolean isDataInited;

    public abstract int initLayoutId();

    public abstract void initView();

    public abstract void initData();

    public abstract P initPresenter();

    public abstract void getData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getData();
        mRootView = inflater.inflate(initLayoutId(), container, false);
        initView();
        return mRootView;
    }

    protected void initFetchData() {
        if (isUserVisible && isViewInited && !isDataInited) {
            Log.d("TAG", "ok");
            getData();
            isDataInited = true;
        }
    }

    //当前用户界面是否可见
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isUserVisible = isVisibleToUser;
        //界面可见时，请求数据
        initData();
        initFetchData();
        Log.d("TAG", "setUserVisibleHint");
    }

    //Activity是否创建完成View是否初始化完成
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //当Activity创建完毕Presenter与IBaseView建立关系通过Presenter与Model建立联系
        mPresenter = initPresenter();
        mPresenter.attachView(this);
        isViewInited = true;
        initFetchData();
        Log.d("TAG", "onActivityCreated");
    }

    /**
     * @param typeId 成功时全部隐藏，点击后ProgressBar展现
     */
    public void setWhenNetSuccess(int typeId, View... views) {
        for (View mViews : views) {
            if (typeId != 0 && mViews.getClass().getName()
                    .equals(ProgressBar.class.getName())) {
                mViews.setVisibility(View.VISIBLE);
            } else if (typeId == 0 || mViews.getClass().getName()
                    .equals(LinearLayout.class.getName())) {
                mViews.setVisibility(View.GONE);
            }

        }
    }

    public void setWhenNetFailed(View... views) {
        for (View mViews : views) {
            if (mViews.getClass().getName().equals(LinearLayout.class.getName())) {
                Log.d("TAG", mViews.getClass().getName());
                mViews.setVisibility(View.VISIBLE);
            } else if (mViews.getClass().getName().equals(ProgressBar.class.getName())) {
                mViews.setVisibility(View.GONE);
            }
        }
    }
}
