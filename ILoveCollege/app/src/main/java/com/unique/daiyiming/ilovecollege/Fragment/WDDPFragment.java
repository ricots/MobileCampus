package com.unique.daiyiming.ilovecollege.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unique.daiyiming.ilovecollege.R;
import com.unique.daiyiming.ilovecollege.View.ViewPagerTabBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daiyiming on 2015/6/19.
 * 我的地盘
 */
public class WDDPFragment extends Fragment {

    private ViewPager vp_viewPager = null;
    private List<Fragment> fragmentList = null;
    private ViewPagerTabBar vptb_tabBar = null;

    private class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {
        public FragmentViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wddp, null);

        vp_viewPager = (ViewPager) view.findViewById(R.id.vp_viewPager);
        vptb_tabBar = (ViewPagerTabBar) view.findViewById(R.id.vptb_tabBar);

        fragmentList = new ArrayList<>();
        fragmentList.add(new DHFragment());
        fragmentList.add(new ZJFragment());
        fragmentList.add(new QYHFragment());

        vp_viewPager.setOffscreenPageLimit(2); //设置相邻两页都会被缓存
        vp_viewPager.setAdapter(new FragmentViewPagerAdapter(getActivity().getSupportFragmentManager()));
        vp_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    vptb_tabBar.setCurrentPosition(vp_viewPager.getCurrentItem());
                }
            }
        });

        vptb_tabBar.setOnViewPagerTabBarChangeListener(new ViewPagerTabBar.OnViewPagerTabBarChangeListener() {
            @Override
            public void OnViewPagerTabBarChanged(int currentPosition) {
                vp_viewPager.setCurrentItem(currentPosition, false);
            }
        });

        return view;
    }
}
