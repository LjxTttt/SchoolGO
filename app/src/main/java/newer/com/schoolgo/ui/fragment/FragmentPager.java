package newer.com.schoolgo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 樱花满地，集于我心 on 2016/11/15 0015.
 */

public class FragmentPager extends Fragment {
    private static final String BUNDLE_TITLE = "title";
    private String viewPagerTxt;

    public static FragmentPager newInstanceFragment(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        FragmentPager fragmentPager = new FragmentPager();
        fragmentPager.setArguments(bundle);
        return fragmentPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            viewPagerTxt = bundle.getString(BUNDLE_TITLE);
        } else {
            viewPagerTxt = "NULL";
        }
        TextView textView = new TextView(getContext());
        textView.setText(viewPagerTxt);
        textView.setTextColor(Color.GRAY);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
