package com.example.news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.news.R;
import com.example.news.common.DefineView;
import com.example.news.fragment.base.BaseFragment;

import java.security.Key;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PageFragment extends BaseFragment implements DefineView {

    @BindView(R.id.textView)
    TextView textView;
    private static final String KEY = "EXART";
    private Unbinder unbinder;
    private String message;

    public static PageFragment newInstance(String extra){
        Bundle bundle = new Bundle();
        bundle.putString(KEY, extra);
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
        initValidata();
        initListener();
        bindData();
    }

    @Override
    public void initValidata() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            message = bundle.getString(KEY);
        }
        if (message != null) {
            textView.setText(message);
        }
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
