package com.example.news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.adapter.FixedPagerAdapter;
import com.example.news.common.DefineView;
import com.example.news.fragment.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainInfoFragment extends BaseFragment implements DefineView, ViewPager.OnPageChangeListener {

    @BindView(R.id.main_tab_layout)
    TabLayout mainTabLayout;
    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;

    private Unbinder unbinder;
    private FixedPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private String[] titles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_info_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initValidata();
        initListener();
        bindData();
    }

    @Override
    public void initValidata() {
        titles = new String[]{"推荐", "要闻", "新思想", "综合", "快闪", "发布",
                "实践", "订阅", "经济", "人物", "健康", "科技", "文化"};
        adapter = new FixedPagerAdapter(getChildFragmentManager());
        adapter.setTitles(titles);
        fragmentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            fragmentList.add(PageFragment.newInstance(titles[i]));
        }
        adapter.setFragmentList(fragmentList);
        mainViewPager.setAdapter(adapter);
        //使用setupWithViewPager可以让TabLayout和ViewPager联动
        mainTabLayout.setupWithViewPager(mainViewPager);
        //设置TabLayout的模式（MODE_SCROLLABLE：可进行滑动）
        mainTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void initListener() {
        mainViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void bindData() {

    }

    /**
     * 重写viewPager滑动监听
     * 解决滑动冲突
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            ((MainActivity)getActivity()).getDragLayout().setDrag(true);
        } else {
            ((MainActivity)getActivity()).getDragLayout().setDrag(false);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
