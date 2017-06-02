package newer.com.schoolgo.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import newer.com.schoolgo.R;
import newer.com.schoolgo.bean.HttpResult;
import newer.com.schoolgo.bean.TokenAcess;
import newer.com.schoolgo.modle.IMomentAddModle;
import newer.com.schoolgo.modle.ITokenModle;
import newer.com.schoolgo.net.NetMannger;
import newer.com.schoolgo.rx.RxManager;
import newer.com.schoolgo.rx.RxObserver;
import newer.com.schoolgo.ui.fragment.FindFragment;
import newer.com.schoolgo.util.DateUtil;
import newer.com.schoolgo.util.QiNiuUtil;
import newer.com.schoolgo.util.SPUtil;
import newer.com.schoolgo.util.ToastUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/5/2.
 */

public class MomentActivity extends AppCompatActivity implements
        EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate, QiNiuUtil.OnUploadCompleteListener {
    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    ProgressDialog progressDialog;
    ArrayList<String> filePath = new ArrayList<>();
    ArrayList<String> fileSucess = new ArrayList<>();
    private BGASortableNinePhotoLayout mPhotosSnpl;
    private EditText mContentEt;
    private long userId = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_talk_layout);
        userId = getIntent().getLongExtra(FindFragment.INTENT_EX, 0L);
        mPhotosSnpl = (BGASortableNinePhotoLayout) findViewById(R.id.add_photo);
        mContentEt = (EditText) findViewById(R.id.et_moment_add_content);
        //设置最多选择图片
        mPhotosSnpl.setMaxItemCount(4);
        //设置可编辑
        mPhotosSnpl.setEditable(true);
        //设置是否显示加号图片
        mPhotosSnpl.setPlusEnable(true);
        mPhotosSnpl.setDelegate(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.release) {
            String content = mContentEt.getText().toString().trim();
            if (content.length() == 0 && mPhotosSnpl.getItemCount() == 0) {
                Toast.makeText(this, "必须填写这一刻的想法或选择照片！", Toast.LENGTH_SHORT).show();
                return;
            }
            setProgessDialog();
            getAccessToken();
        } else if (v.getId() == R.id.cancelRelease) {
            finish();
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotosSnpl.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "SchoolGoTakePhoto");
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotosSnpl.getMaxItemCount() - mPhotosSnpl.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            mPhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedImages(data));
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }
    }

    private void getAccessToken() {
        progressDialog.show();
        filePath.clear();
        filePath = mPhotosSnpl.getData();
        ITokenModle tokenModle = NetMannger.getInstance().createConnection(ITokenModle.class);
        RxManager.getInstance().doSubscribe(tokenModle.getSchoolDetailData(), new RxObserver<TokenAcess>() {
            @Override
            public void _OnNext(TokenAcess tokenAcess) {
                if (!filePath.isEmpty()) {
                    Configuration cfg = QiNiuUtil.configQiniu();
                    //设置上传完成监听
                    QiNiuUtil.setUploadCompleteListener(MomentActivity.this);
                    for (String s : filePath) {
                        QiNiuUtil.upLoadImag(s, tokenAcess.getToken(), cfg, MomentActivity.this);
                    }
                } else {
                    insertMyServies();
                }
            }

            @Override
            public void _OnError(Throwable e) {
                Log.d("TAG", "e===" + e.getMessage());
                progressDialog.cancel();
            }
        });

    }

    private void setProgessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void resultRetrun() {
        Intent intent = new Intent();
        intent.putExtra("content", mContentEt.getText().toString());
        intent.putStringArrayListExtra("img", filePath);
        intent.putExtra("name", ((String) SPUtil.get(LoginActivity.USER_NAME, "")));
        setResult(RESULT_OK, intent);
        progressDialog.dismiss();
        ToastUtil.showToast(this, "发布成功");
        finish();
    }

    @Override
    public void upLoadSuceess(String key) {
        fileSucess.add(key);
        if (filePath.size() == fileSucess.size()) {
            insertMyServies();
        }
    }

    @Override
    public void upLoadError(ResponseInfo info, JSONObject response) {
    }

    public void insertMyServies() {
        IMomentAddModle addModle = NetMannger.getInstance().createConnection(IMomentAddModle.class);
        RxManager.getInstance().doSubscribe(addModle.add(fileSucess, ((long) SPUtil.get(LoginActivity.USER_ID, 0L)), mContentEt.getText().toString(),
                Long.parseLong(DateUtil.DatetoFileName())), new RxObserver<HttpResult>() {
            @Override
            public void _OnNext(HttpResult httpResult) {
                Log.d("TAG", "onNext ==" + httpResult.getMessage());
                progressDialog.dismiss();
                resultRetrun();
            }

            @Override
            public void _OnError(Throwable e) {
                Log.d("TAG", "_OnError ==" + e.getMessage());
                progressDialog.dismiss();
                ToastUtil.showToast(MomentActivity.this, "发布失败");
            }
        });
    }
}
