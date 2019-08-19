package com.example.news;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.adapter.LeftItemAdapter;
import com.example.news.application.InitApp;
import com.example.news.common.DefineView;
import com.example.news.ui.base.BaseActivity;
import com.example.news.widget.DragLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements DefineView {

    @BindView(R.id.iv_bottom)
    ImageView ivBottom;
    @BindView(R.id.left_menu_username)
    TextView leftMenuUsername;
    @BindView(R.id.left_menu_represent)
    TextView leftMenuRepresent;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.left_item_recyclerview)
    RecyclerView leftItemRecyclerview;
    @BindView(R.id.top_bar_icon)
    ImageView topBarIcon;
    @BindView(R.id.top_bar_title)
    TextView topBarTitle;
    @BindView(R.id.top_bar_search_btn)
    Button topBarSearchBtn;
    @BindView(R.id.bar_layout)
    RelativeLayout barLayout;
    @BindView(R.id.drag_Layout)
    DragLayout dragLayout;

    public DragLayout getDragLayout() {
        return dragLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化沉浸式状态栏
        setStatusBar();
        LinearLayoutManager manager = new LinearLayoutManager(InitApp.getInstance());
        leftItemRecyclerview.setLayoutManager(manager);
        leftItemRecyclerview.setAdapter(new LeftItemAdapter(this));
    }

    @OnClick({R.id.top_bar_icon, R.id.top_bar_search_btn, R.id.drag_Layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_bar_icon:
                dragLayout.open();
                break;
            case R.id.top_bar_search_btn:
                break;
            case R.id.drag_Layout:
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initValidata() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void bindData() {
//        RequestBody requestBody = new FormBody.Builder();
    }

}
