package com.example.news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.news.R;
import com.example.news.application.InitApp;
import com.example.news.application.OkhttpManager;
import com.example.news.common.DefineView;
import com.example.news.entity.NewsType;
import com.example.news.fragment.base.BaseFragment;

import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PageFragment extends BaseFragment implements DefineView {

    @BindView(R.id.textView)
    TextView textView;
    private static final String KEY = "EXART";
    private Unbinder unbinder;
    private String message;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                textView.setText(message);
            }
        }
    };

    public static PageFragment newInstance(NewsType newsType){
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, newsType);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page_fragment_layout, container, false);
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
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            message = bundle.getString(KEY);
//        }
//        if (message != null) {
//            textView.setText(message);
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    message = OkhttpManager.getSync(InitApp.ip_port + "queryNewsType").body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void bindData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
