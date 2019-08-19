package com.example.news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.adapter.FixedPagerAdapter;
import com.example.news.application.InitApp;
import com.example.news.application.OkhttpManager;
import com.example.news.common.DefineView;
import com.example.news.entity.NewsType;
import com.example.news.fragment.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
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
    private List<NewsType> newsTypes = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                bindData();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_info_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initValidata();
        initListener();
        bindData();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initValidata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String message = OkhttpManager.getSync(InitApp.ip_port+"queryNewsType").body().string();
                    List<NewsType> typeList = new Gson().fromJson(message, new TypeToken<List<NewsType>>(){}.getType());
                    for (NewsType type : typeList) {
                        Log.d(InitApp.TAG, "数值为：" + type.getTypeName());
                        newsTypes.add(type);
                    }
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void initListener() {
        mainViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void bindData() {
        adapter = new FixedPagerAdapter(getChildFragmentManager());
        adapter.setTypesTitle(newsTypes);
        fragmentList = new ArrayList<>();
        for (int i = 0; i < newsTypes.size(); i++) {
            BaseFragment fragment = null;
            if (i == 0) fragment = HomeFragment.newInstance(newsTypes.get(i));
            else fragment = PageFragment.newInstance(newsTypes.get(i));
            fragmentList.add(fragment);
        }
        adapter.setFragmentList(fragmentList);
        mainViewPager.setAdapter(adapter);
        //使用setupWithViewPager可以让TabLayout和ViewPager联动
        mainTabLayout.setupWithViewPager(mainViewPager);
        //设置TabLayout的模式（MODE_SCROLLABLE：可进行滑动）
        mainTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
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
