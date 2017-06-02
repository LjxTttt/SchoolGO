package newer.com.schoolgo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import newer.com.schoolgo.Api;
import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.School;
import newer.com.schoolgo.bean.SchoolDetail;
import newer.com.schoolgo.presenter.SchoolItemPresenter;
import newer.com.schoolgo.ui.activity.TextActivity;
import newer.com.schoolgo.ui.adpater.SchooloItemAdpater;
import newer.com.schoolgo.ui.adpater.baseadpater.BaseApater;
import newer.com.schoolgo.ui.adpater.baseadpater.ViewHolder;
import newer.com.schoolgo.ui.diyview.SmartPullableLayout;
import newer.com.schoolgo.ui.view.SchoolItemView;
import newer.com.schoolgo.util.JsoupUtil;

/**
 * Created by 樱花满地，集于我心 on 2016/11/15 0015.
 */

public class SchoolItemFragment extends BaseMvpFragment<SchoolItemPresenter> implements SchoolItemView, View.OnClickListener, SmartPullableLayout.OnPullListener {
    public static final String TAG_SCHOOL = "school";
    public static final String INTENT_TAG = "content";
    public static final int SCHOOL_NEW = 14172601;
    public static final int SCHOOL_ACT = 14172602;
    public static final int SCHOOL_NOTCIE = 14172603;
    public static int typeId;
    static List<School> schools;
    private RecyclerView school_recy;
    private SmartPullableLayout mPullRefresh;
    private SchooloItemAdpater adpater;
    private ProgressBar pb;
    private View net_not_use;
    //这里是解析一些详情信息放在列表当中
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.arg1 == 1) {
                List<School> schoolss = (List<School>) message.obj;
                if (mPullRefresh.getCurrentState() == SmartPullableLayout.State.PULL_DOWN_RELEASE) {
                    adpater.setDatas(schoolss);
                } else if (mPullRefresh.getCurrentState() == SmartPullableLayout.State.PULL_UP_RELEASE) {
                    adpater.setNewSchoolNoticeData(schoolss);
                } else if (mPullRefresh.getCurrentState() == SmartPullableLayout.State.NORMAL) {
                    adpater.setDatas(schoolss);
                }
                mPullRefresh.stopPullBehavior();
                setWhenNetSuccess(0, pb, net_not_use);
            }
            return false;
        }
    });
    private boolean isLoadMore = false;

    //根据标题创建不同的Fragment
    public static SchoolItemFragment newInstance(String school_title) {
        SchoolItemFragment schoolItemFragment = new SchoolItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG_SCHOOL, school_title);
        schoolItemFragment.setArguments(bundle);
        return schoolItemFragment;
    }

    //加载布局文件
    @Override
    public int initLayoutId() {
        return R.layout.recycleview_type_layout;
    }

    //初始化View
    @Override
    public void initView() {
        school_recy = (RecyclerView) mRootView.findViewById(R.id.school_recycle_view);
        net_not_use = mRootView.findViewById(R.id.net_work_refresh_id);
        adpater = new SchooloItemAdpater(getContext());
        LinearLayoutManager lin = new LinearLayoutManager(getContext());
        lin.setOrientation(LinearLayoutManager.VERTICAL);
        school_recy.setLayoutManager(lin);
        school_recy.setAdapter(adpater);
        pb = (ProgressBar) mRootView.findViewById(R.id.load);
        net_not_use.setOnClickListener(this);
        mPullRefresh = (SmartPullableLayout) mRootView.findViewById(R.id.pull_recy_refresh);
        mPullRefresh.setOnPullListener(this);
        adpater.setOnItemClickListener(new BaseApater.OnItemClickListener<School>() {
            @Override
            public void onItemClicked(ViewHolder vh, School school, int pos) {
                Intent mIntent = new Intent(getContext(), TextActivity.class);
                mIntent.putExtra(INTENT_TAG, school.getNotice_content());
                startActivity(mIntent);
            }
        });
    }

    //初始化数据
    @Override
    public void initData() {
        if (getArguments() == null) {
            return;
        }
        typeId = getTypeId(getArguments().getString(TAG_SCHOOL));
    }

    //获得学校的Presenter
    @Override
    public SchoolItemPresenter initPresenter() {
        return new SchoolItemPresenter();
    }

    //获取数据
    @Override
    public void getData() {
        mPresenter.getSchoolItemData(typeId);
    }

    private int getTypeId(String str) {
        switch (str) {
            case "校园公告":
                return SCHOOL_NOTCIE;
            case "校园活动":
                return SCHOOL_ACT;
            case "校园新闻":
                return SCHOOL_NEW;
        }
        return 0;
    }

    //成功时View做出的操作
    @Override
    public void onSuccess(List<School> listSchool) {
        schools = listSchool;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<School> schoolss = new ArrayList<>();
                for (School school : schools) {
                    if (school.getNotice_content().length() > 30) {
                        SchoolDetail sc = JsoupUtil.connectParse(Api.HNGCZY_URL + school.getNotice_content());
                        school.setContent(sc.getContent());
                        ArrayList<String> list = new ArrayList<String>();
                        for (String s : sc.getImgSrc()) {
                            list.add(Api.HNGCZY_URL + s);
                        }
                        school.setImgSrc(list);
                        schoolss.add(school);
                    }
                }
                if (schools.get(0).getNotice_content().length() < 30) {
                    for (School school1 : schools) {
                        schoolss.add(school1);
                    }
                }
                Message msg = new Message();
                msg.arg1 = 1;
                msg.obj = schoolss;
                handler.sendMessage(msg);
            }
        }).start();
    }

    //当网络出错时，可点击图标重新加载
    @Override
    public void onClick(View v) {
        setWhenNetSuccess(1, pb, net_not_use);
        getData();
    }

    //下拉刷新
    @Override
    public void onPullDown() {
        mPresenter.pageNow = 1;
        getData();
    }

    //上拉加载
    @Override
    public void onPullUp() {
        mPresenter.pageNow++;
        isLoadMore = true;
        getData();
    }

    //错误时View的显示
    @Override
    public void OnError(ErrorTag errorTag) {
        if (isLoadMore) {
            mPullRefresh.stopPullBehavior();
            isLoadMore = false;
        } else {
            setWhenNetFailed(pb, net_not_use);
        }
    }
}
