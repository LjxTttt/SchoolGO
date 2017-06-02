package newer.com.schoolgo.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.imageloader.BGARVOnScrollListener;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.Moment;
import newer.com.schoolgo.presenter.MomentListPre;
import newer.com.schoolgo.ui.activity.LoginActivity;
import newer.com.schoolgo.ui.activity.MomentActivity;
import newer.com.schoolgo.ui.adpater.FindListAdpater;
import newer.com.schoolgo.ui.view.IMomentListView;
import newer.com.schoolgo.util.SPUtil;
import newer.com.schoolgo.util.ToastUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/5/2.
 */

public class FindFragment extends BaseMvpFragment<MomentListPre> implements EasyPermissions.PermissionCallbacks
        , FindListAdpater.OnClickNinePhotoItem, View.OnClickListener, IMomentListView, FindListAdpater.OnComentLisenter {
    public static final String INTENT_EX = "userId";
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PREVIEW = 1;
    RecyclerView mListFindView;
    FindListAdpater mAdapter;
    BGANinePhotoLayout mCurrentClickNpl;
    ImageView addView;

    @Override
    public int initLayoutId() {
        return R.layout.find_list_view;
    }

    @Override
    public void initView() {
        mListFindView = (RecyclerView) mRootView.findViewById(R.id.find_recycleView);
        addView = (ImageView) mRootView.findViewById(R.id.add_talk);
        addView.setOnClickListener(this);
        mAdapter = new FindListAdpater(mListFindView, getContext());
        mAdapter.setOnComentLisenter(this);
        mListFindView.addOnScrollListener(new BGARVOnScrollListener(getActivity()));
        mListFindView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListFindView.setAdapter(mAdapter);
        mAdapter.setOnClickNinePhotoItem(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public MomentListPre initPresenter() {
        return new MomentListPre();
    }

    @Override
    public void getData() {
        mPresenter.getListMoment();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PREVIEW) {
            Toast.makeText(getContext(), "您拒绝了「图片预览」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (mCurrentClickNpl == null) {
            return;
        }
        // 保存图片的目录，改成你自己要保存图片的目录。如果不传递该参数的话就不会显示右上角的保存按钮
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            if (mCurrentClickNpl.getItemCount() == 1) {
                // 预览单张图片
                startActivity(BGAPhotoPreviewActivity.newIntent(getContext(), null, mCurrentClickNpl.getCurrentClickItem()));
            } else if (mCurrentClickNpl.getItemCount() > 1) {
                // 预览多张图片
                startActivity(BGAPhotoPreviewActivity.newIntent(getContext(), null, mCurrentClickNpl.getData(), mCurrentClickNpl.getCurrentClickItemPosition()));
            }
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", REQUEST_CODE_PERMISSION_PHOTO_PREVIEW, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onClickNinePhotoItemByFind(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_talk) {
            if (SPUtil.contain(LoginActivity.USER_ID)) {
                Intent intent = new Intent(getContext(), MomentActivity.class);
                intent.putExtra(INTENT_EX, ((long) SPUtil.get(LoginActivity.USER_ID, 0L)));
                startActivityForResult(intent, 1);
            } else {
                ToastUtil.showToast(getContext(), "嘿，你还没登陆呢，别做个无名的人");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
//            Moment moment = new Moment(data.getStringExtra("content"),
//                    data.getStringArrayListExtra("img"));
//            moment.moment_user_name = data.getStringExtra("name");
//            mAdapter.addFirstItem(moment);
//            mListFindView.smoothScrollToPosition(0);
            getData();
        }
    }

    @Override
    public void onListSucess(List<Moment> moment) {
        mAdapter.setData(moment);
    }

    @Override
    public void addComentSucess() {
        ToastUtil.showToast(getContext(), "评论成功");
        getData();
    }

    @Override
    public void commit(String text, long tag, int pos) {
        mPresenter.addComent(SPUtil.get(LoginActivity.USER_NAME, "").toString()
                , text, tag);
    }

    @Override
    public void OnError(ErrorTag errorTag) {
        switch (errorTag) {
            case ERROR_REG_SEV:
                ToastUtil.showToast(getContext(), "服务器可能出问题了");
                break;
            case ERROR_MOMENT_LIST:
                ToastUtil.showToast(getContext(), "评论失败，请检查网络连接");
                break;
        }
    }
}
