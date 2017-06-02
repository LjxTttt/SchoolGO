package newer.com.schoolgo.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import newer.com.schoolgo.Api;
import newer.com.schoolgo.ErrorTag;
import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.SchoolDetail;
import newer.com.schoolgo.presenter.SchoolDetailPresenter;
import newer.com.schoolgo.ui.fragment.SchoolItemFragment;
import newer.com.schoolgo.ui.view.SchoolDetailView;
import newer.com.schoolgo.util.JsoupUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/4/10.
 */

public class TextActivity extends BaseMvpActivity<SchoolDetailView, SchoolDetailPresenter> implements
        SchoolDetailView, BGANinePhotoLayout.Delegate, EasyPermissions.PermissionCallbacks {

    RelativeLayout mLayout;
    private BGANinePhotoLayout nineGridView;
    private AVLoadingIndicatorView avi;

    @Override
    protected void initView() {
        mLayout = (RelativeLayout) findViewById(R.id.main_layout);
        avi = (AVLoadingIndicatorView) findViewById(R.id.loading);
        avi.show();
        getDataDetail();
    }

    @Override
    protected int initLayoutId() {
        return R.layout.text_pull_refrsh;
    }

    @Override
    protected SchoolDetailPresenter initPresenter() {
        return new SchoolDetailPresenter();
    }


    @Override
    protected void getDataDetail() {
        Intent intent = getIntent();
        mPresenter.getSchoolDetailData(intent.getStringExtra(SchoolItemFragment.INTENT_TAG));
    }

    @Override
    public void OnSuccess(String s) {
        SchoolDetail sc = JsoupUtil.parseSchool_News(s);
        List<String> title = sc.getTitle();
        int lastChildId = 0;
        avi.hide();
        for (int i = 0; i < title.size(); i++) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            TextView titleView = new TextView(this);
            titleView.setText(title.get(i));
            titleView.setId((R.id.title1) + i);
            titleView.setGravity(Gravity.CENTER_HORIZONTAL);
            if (i == 0) {
                //第一个标题字体更改
                titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                titleView.setMaxLines(2);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                layoutParams.addRule(RelativeLayout.BELOW,
                        lastChildId);
            }
            titleView.setEllipsize(TextUtils.TruncateAt.END);
            titleView.setLayoutParams(layoutParams);
            mLayout.addView(titleView);
            lastChildId = mLayout.getChildAt(i).getId();
        }
        //添加内容布局
        TextView content = new TextView(this);
        RelativeLayout.LayoutParams contentLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        contentLp.addRule(RelativeLayout.BELOW, lastChildId);
        content.setLayoutParams(contentLp);
        content.setId(R.id.detail_content);
        contentLp.topMargin = 60;
        content.setText(sc.getContent());
        mLayout.addView(content);
        //添加九宫格布局
        nineGridView = new BGANinePhotoLayout(this);
        RelativeLayout.LayoutParams imageLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageLp.addRule(RelativeLayout.BELOW, R.id.detail_content);
        imageLp.addRule(RelativeLayout.CENTER_IN_PARENT);
        nineGridView.setLayoutParams(imageLp);
        nineGridView.setId(R.id.img_grid);
        nineGridView.setDelegate(this);
        ArrayList<String> imgSrc = new ArrayList<>();
        for (int i = 0; i < sc.getImgSrc().size(); i++) {
            imgSrc.add(Api.HNGCZY_URL + sc.getImgSrc().get(i));
        }
        nineGridView.setData(imgSrc);
        mLayout.addView(nineGridView, imageLp);
        View view = new View(this);
        RelativeLayout.LayoutParams noneLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 80);
        noneLp.addRule(RelativeLayout.BELOW, R.id.img_grid);
        view.setLayoutParams(noneLp);
        mLayout.addView(view);
    }


    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        photoPreviewWrapper();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 1) {
            Toast.makeText(this, "您拒绝了「图片预览」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(1)
    private void photoPreviewWrapper() {
        if (nineGridView == null) {
            return;
        }

        // 保存图片的目录，改成你自己要保存图片的目录。如果不传递该参数的话就不会显示右上角的保存按钮

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (nineGridView.getItemCount() == 1) {
                // 预览单张图片
                startActivity(BGAPhotoPreviewActivity.newIntent(this, null, nineGridView.getCurrentClickItem()));
            } else if (nineGridView.getItemCount() > 1) {
                // 预览多张图片
                startActivity(BGAPhotoPreviewActivity.newIntent(this, null, nineGridView.getData(), nineGridView.getCurrentClickItemPosition()));
            }
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", 1, perms);
        }
    }


    @Override
    public void OnError(ErrorTag errorTag) {

    }
}
