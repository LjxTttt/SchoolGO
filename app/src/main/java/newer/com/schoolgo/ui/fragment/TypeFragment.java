package newer.com.schoolgo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import newer.com.schoolgo.R;

/**
 * Created by 樱花满地，集于我心 on 2016/11/15 0015.
 */

public class TypeFragment extends TabFragment {
    private static final String BUNDLE_TITLE = "type";

    public static Fragment newInstanceTypeFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TITLE, type);
        Fragment typeFragment = new TypeFragment();
        switch (type) {
            case R.id.School:
                typeFragment = new TabFragment();
                break;
            case R.id.find:
                typeFragment = new FindFragment();
        }
        typeFragment.setArguments(bundle);
        return typeFragment;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
