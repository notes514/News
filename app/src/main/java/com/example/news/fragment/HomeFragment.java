package com.example.news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.news.R;
import com.example.news.application.InitApp;
import com.example.news.application.OkhttpManager;
import com.example.news.common.DefineView;
import com.example.news.entity.NewsContent;
import com.example.news.entity.NewsType;
import com.example.news.fragment.base.BaseFragment;
import com.example.news.utils.adapter.BaseAdapterHelper;
import com.example.news.utils.adapter.QuickAdapter;
import com.example.news.utils.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 新闻首页
 * Name: laodai
 * Time: 2019.08.08
 */
public class HomeFragment extends BaseFragment implements DefineView {

    private static final String TAG = "HomeFragment";
    private static final String KEY = "EXART";
    @BindView(R.id.home_pull_refresh_list_view)
    PullToRefreshListView homePullRefreshListView;
    @BindView(R.id.home_framelayout)
    FrameLayout homeFramelayout;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.empty)
    LinearLayout empty;
    @BindView(R.id.error)
    LinearLayout error;
    private Unbinder unbinder;
    private List<NewsContent> contentList;
    private QuickAdapter<NewsContent> adapter;

    public static HomeFragment newInstance(NewsType newsType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, newsType);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initValidata();
        initListener();
    }

    @Override
    public void initValidata() {
        //设置页面数据加载隐藏显示
        homePullRefreshListView.setVisibility(View.GONE);
        homeFramelayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        OkhttpManager.getASync(InitApp.ip_port + "queryNewsContent?typeId=1", new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Call call, IOException e) {

            }

            @Override
            public void requestSuccess(Call call, Response response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response.body().string());
                    /**
                     * Gson无法解析数位太长的时间格式：
                     * 解决方案：自定义和注册Gson适配器
                     */
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

                        @Override
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return new Date(json.getAsJsonPrimitive().getAsLong());
                        }
                    });
                    Gson gson = builder.create();
                    contentList = gson.fromJson(json.getString("ROWS_DETAIL"), new TypeToken<List<NewsContent>>() {
                    }.getType());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bindData();
            }
        });
    }

    @Override
    public void initListener() {
    }

    @Override
    public void bindData() {
        if (contentList != null) {
            homePullRefreshListView.setVisibility(View.VISIBLE);
            homeFramelayout.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            error.setVisibility(View.GONE);

            adapter = new QuickAdapter<NewsContent>(getActivity(),
                    R.layout.home_news_item_layout, contentList) {
                @Override
                protected void convert(BaseAdapterHelper helper, NewsContent item) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Log.d(TAG, "时间: " + item.getNewsTime());
                    helper.setText(R.id.home_news_theme, item.getNewsTheme())
                            .setText(R.id.home_news_libs, item.getNewsPress())
                            .setText(R.id.home_news_time, format.format(item.getNewsTime()))
                            .setImageResource(R.id.home_news_image, R.drawable.arrow);
                }
            };
            homePullRefreshListView.setAdapter(adapter);
        } else {
            homePullRefreshListView.setVisibility(View.GONE);
            homeFramelayout.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
