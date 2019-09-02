package com.example.news.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.news.R;
import com.example.news.application.InitApp;
import com.example.news.application.OkhttpManager;
import com.example.news.common.DefineView;
import com.example.news.entity.NewsContent;
import com.example.news.entity.NewsType;
import com.example.news.fragment.base.BaseFragment;
import com.example.news.ui.NewDetailsActivity;
import com.example.news.utils.adapter.BaseAdapterHelper;
import com.example.news.utils.adapter.QuickAdapter;
import com.example.news.utils.widget.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;

/**
 * 新闻首页
 * Name: laodai
 * Time: 2019.08.08
 */
public class HomeFragment extends BaseFragment implements DefineView, OnBannerListener {

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
    private Unbinder unbinder; //空间绑定
    private View headView;
    private Banner banner;
    private List<NewsContent> contentList;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private QuickAdapter<NewsContent> adapter;

    public static HomeFragment newInstance(NewsType newsType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY, (Serializable) newsType);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_layout, container, false);
    }

    @SuppressLint("InflateParams")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        headView = LayoutInflater.from(getContext()).inflate(R.layout.home_banner_layout, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initValidata();
        initListener();
    }

    @Override
    public void initView() {
        //添加头部图片轮播
        homePullRefreshListView.addHeaderView(headView);
        //获取banner实例
        banner = headView.findViewById(R.id.home_banner);
    }

    @Override
    public void initValidata() {
        //设置页面数据加载隐藏显示
        homePullRefreshListView.setVisibility(View.GONE);
        homeFramelayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        OkhttpManager.getAsync(InitApp.ip_port + "queryAllNewContent", new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, Exception e) {
                Toast.makeText(getContext(), "加载失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) {
                /**
                 * Gson无法解析数位太长的时间格式：
                 * 解决方案：自定义和注册Gson适配器
                 */
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json1, typeOfT, context) -> new Date(json1.getAsJsonPrimitive().getAsLong()));
                Gson gson = builder.create();
                contentList = gson.fromJson(result, new TypeToken<List<NewsContent>>() {}.getType());
                bindData();
            }
        });
    }

    @Override
    public void initListener() {
        homePullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewDetailsActivity.class);
                intent.putExtra("newsId", contentList.get((int) id).getNewsId());
                getActivity().startActivity(intent);
            }
        });
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
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    helper.setText(R.id.home_news_theme, item.getNewsTheme())
                            .setText(R.id.home_news_libs, item.getNewsPress())
                            .setText(R.id.home_news_time, format.format(item.getNewsTime()))
                            .setImageResource(R.id.home_news_image, R.drawable.arrow);
                    TextView typeText = helper.getView(R.id.home_type_text);
                    typeText.setVisibility(View.VISIBLE);
                    typeText.setText(item.getRemark());
                    ImageLoader.getInstance().displayImage(InitApp.ip_images+item.getNewsPic(),
                            (ImageView) helper.getView(R.id.home_news_image), InitApp.getOptions());
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

        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add(InitApp.ip_images+"images/news/01.jpg");
        list_path.add(InitApp.ip_images+"images/news/02.jpg");
        list_path.add(InitApp.ip_images+"images/news/03.jpg");
        list_path.add(InitApp.ip_images+"images/news/04.jpg");
        list_path.add(InitApp.ip_images+"images/news/05.jpg");

        list_title.add("焦点分析 | 索尼迪士尼谈崩，蜘蛛侠退出漫威");
        list_title.add("小程序的增速，远超我们的想象");
        list_title.add("5G第一个风口，云游戏时代离我们有多远？");
        list_title.add("Uber、Airbnb之后，下一个明星独角兽在哪里？");
        list_title.add("WISE×企业智能行业峰会—智慧新“企”点");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new com.youth.banner.loader.ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //必须最后调用的方法，启动轮播图。
                .start();

    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
