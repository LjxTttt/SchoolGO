package newer.com.schoolgo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.RadioGroup;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import newer.com.schoolgo.R;
import newer.com.schoolgo.ui.fragment.TypeFragment;
import newer.com.schoolgo.util.DateUtil;
import newer.com.schoolgo.util.SPUtil;
import newer.com.schoolgo.util.ToastUtil;


public class MainActivity extends AppCompatActivity implements AccountHeader.OnAccountHeaderListener, Drawer.OnDrawerItemClickListener {
    private static final int LOGIN_REQUEST = 1;
    @BindView(R.id.bottom_menu)
    RadioGroup bottom_menu;
    private Unbinder mUnbinder;
    private int mCurrentTypeId;
    private Drawer drawer;
    private AccountHeader header;
    private SparseArray<Fragment> mTypeFragments = new SparseArray<>();
    private Bundle bundle;
    private ProfileDrawerItem profileDrawerItem;
    private Intent regIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        drawerSetting();
        //如果用户名超过有效期则删除信息，重新登录
        if (DateUtil.isDestroyLoginInfo(((long) SPUtil.get("lastTime", 0L)))) {
            SPUtil.clear();
        }
        if (SPUtil.contain(LoginActivity.USER_ID)) {
            updateHeaer(SPUtil.get(LoginActivity.USER_NAME, "").toString(),
                    ((long) SPUtil.get(LoginActivity.USER_ID, 0L)));
        }
        //当前选中的RadioButton显示界面
        setContentFragment(bottom_menu.getCheckedRadioButtonId());
        bottom_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.School:
                        setContentFragment(R.id.School);
                        break;
                    case R.id.find:
                        setContentFragment(R.id.find);
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    public void setContentFragment(int resId_Click) {
        if (resId_Click != mCurrentTypeId) {
            replaceFragment(TypeFragment.newInstanceTypeFragment(resId_Click), resId_Click, mCurrentTypeId);
            mCurrentTypeId = resId_Click;
        }
    }

    private void replaceFragment(Fragment frag, int tag, int lastTag) {
        if (mTypeFragments.get(tag) == null) {
            FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
            mTypeFragments.put(tag, frag);
            fragTran.add(R.id.fragment_type_content, mTypeFragments.get(tag));
            fragTran.commit();
        }
        if (mTypeFragments.get(tag) != null && lastTag != 0) {
            FragmentTransaction fragTran = getSupportFragmentManager().beginTransaction();
            fragTran.hide(mTypeFragments.get(lastTag))
                    .show(mTypeFragments.get(tag))
                    .commit();
            mTypeFragments.get(tag).setUserVisibleHint(true);
        }
    }

    private void drawerSetting() {
        profileDrawerItem = new ProfileDrawerItem()
                .withName("点击头像开启大学生活")
                .withIcon(R.drawable.logo);
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profileDrawerItem)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(this)
                .withSelectionListEnabled(false)
                .build();
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("个人中心")
                .withIcon(GoogleMaterial.Icon.gmd_account_circle)
                .withSelectedIconColorRes(R.color.tabColor)
                .withSelectable(false);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("退出登录")
                .withIcon(GoogleMaterial.Icon.gmd_directions_run)
                .withSelectedIconColorRes(R.color.tabColor);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .withSliderBackgroundColorRes(R.color.slide_background)
                .withInnerShadow(true)
                .addDrawerItems(
                        item1,
                        item2)
                .withOnDrawerItemClickListener(this)
                .build();
    }

    //点击头像跳转登录注册
    @Override
    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST);
        return false;
    }

    //menu菜单点击事件
    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        if (position == 2) {
            SPUtil.clear();
            ToastUtil.showToast(this, "注销成功");
            profileDrawerItem.withName("点击头像开启大学生活").withEmail("");
            header.updateProfile(profileDrawerItem);
        }
        return false;
    }

    //注册登录页面返回的信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            bundle = data.getExtras();
            updateHeaer(bundle.getString(LoginActivity.USER_NAME), bundle.getLong(LoginActivity.USER_ID));
        }
    }

    private void getRegInfo() {
        regIntent = getIntent();
        if (regIntent.getStringExtra(LoginActivity.USER_NAME) != null) {
            updateHeaer(regIntent.getStringExtra(LoginActivity.USER_NAME), regIntent.getLongExtra(LoginActivity.USER_ID, 0));
        }
    }

    private void updateHeaer(String name, Long userId) {
        profileDrawerItem.withName("用户名:" + name)
                .withEmail("用户ID:" + userId);
        header.updateProfile(profileDrawerItem);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRegInfo();
    }
}
