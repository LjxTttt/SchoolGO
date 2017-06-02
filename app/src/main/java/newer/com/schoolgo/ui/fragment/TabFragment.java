package newer.com.schoolgo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import newer.com.schoolgo.R;
import newer.com.schoolgo.ui.adpater.TypeFragmentAdapter;
import newer.com.schoolgo.ui.diyview.ViewPagerIndicator;
import newer.com.schoolgo.util.TypeChess;

/**
 * Created by Administrator on 2017/3/2.
 */

public class TabFragment extends Fragment {

    protected List<String> tabItems = new ArrayList<>();
    protected List<Fragment> contetFragment = new ArrayList<>();
    protected ViewPagerIndicator tabView;
    protected ViewPager viewPager;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_view, container, false);
        tabView = (ViewPagerIndicator) rootView.findViewById(R.id.tab_item);
        viewPager = (ViewPager) rootView.findViewById(R.id.content_viewPager);
        tabItems = Arrays.asList(TypeChess.SCHOOL);
        for (String tabItem : tabItems) {
            contetFragment.add(SchoolItemFragment.newInstance(tabItem));
        }
        tabView.setTabTitleItems(tabItems);
        TypeFragmentAdapter fragmentAdapter = new TypeFragmentAdapter(getChildFragmentManager());
        fragmentAdapter.setData(contetFragment);
        viewPager.setCurrentItem(0, true);
        viewPager.setOffscreenPageLimit(tabItems.size());
        viewPager.setAdapter(fragmentAdapter);
        tabView.setViewPager(viewPager, 0);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
