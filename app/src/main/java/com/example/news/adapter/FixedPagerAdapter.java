package com.example.news.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.news.entity.NewsType;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager Fragment自定义适配器，其中管理每个页面(Fragment集合)和Tab显示标题
 * Name: laodai
 * Time：2019.08.03
 */
public class FixedPagerAdapter extends FragmentStatePagerAdapter {

    private List<NewsType> typesTitle;
    private List<Fragment> fragmentList = new ArrayList<>();

    public FixedPagerAdapter(FragmentManager fm) {
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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return typesTitle.get(position).getTypeName();
    }

    /**
     * 重写 实例化项
     * @param container 容器
     * @param position  索引
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) super.instantiateItem(container, position);
        } catch (Exception e){
            e.printStackTrace();
        }
        return fragment;
    }

    /**
     * 销毁
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    public void setTypesTitle(List<NewsType> typesTitle) {
        this.typesTitle = typesTitle;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = fragmentList;
    }
}
