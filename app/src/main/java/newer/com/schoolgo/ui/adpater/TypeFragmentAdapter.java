package newer.com.schoolgo.ui.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 樱花满地，集于我心 on 2016/11/18 0018.
 */

public class TypeFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public TypeFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<Fragment> fragments) {
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
