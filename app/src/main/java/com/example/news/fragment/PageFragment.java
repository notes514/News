package com.example.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.R;
import com.example.news.adapter.PageRecyclerAdapter;
import com.example.news.application.InitApp;
import com.example.news.application.OkhttpManager;
import com.example.news.common.DefineView;
import com.example.news.entity.NewsContent;
import com.example.news.entity.NewsType;
import com.example.news.fragment.base.BaseFragment;
import com.example.news.ui.NewDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;

public class PageFragment extends BaseFragment implements DefineView {

    private static final String KEY = "EXART";
    @BindView(R.id.page_recyclerview)
    RecyclerView pageRecyclerview;
    @BindView(R.id.loading)
    LinearLayout loading;
    @BindView(R.id.empty)
    LinearLayout empty;
    @BindView(R.id.error)
    LinearLayout error;
    @BindView(R.id.home_framelayout)
    FrameLayout homeFramelayout;
    @BindView(R.id.page_swipeRefreshLayout)
    SwipeRefreshLayout pageSwipeRefreshLayout;
    //控件绑定
    private Unbinder unbinder;
    private NewsType newsType;
    private LinearLayoutManager layoutManager; //线性布局管理器
    private PageRecyclerAdapter adapter; //适配器
    private List<NewsContent> contentList; //新闻内容数据
    private int lastItem; //记录列表的数量值
    private boolean isMore = true; //解决上来加载重复的bug

    public static PageFragment newInstance(NewsType newsType) {
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
        Bundle build = getArguments();
        if (build != null) {
            newsType = (NewsType) build.getSerializable(KEY);
        }
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
        //设置页面数据加载隐藏显示
        pageRecyclerview.setVisibility(View.GONE);
        homeFramelayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        //设置swipeRefreshLayout的进度条的背景颜色
        pageSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.color_white);
        //设置进度条颜色
        pageSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置进度条的偏移量
        pageSwipeRefreshLayout.setProgressViewOffset(false, 0, 80);

        //设置线性布局管理器
        layoutManager = new LinearLayoutManager(getContext());
        //设置布局垂直显示
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        //设置适配器布局管理器
        pageRecyclerview.setLayoutManager(layoutManager);
        //设置分割线
        //创建适配器
        if (newsType.getTypeName().equals("视频")) {
            adapter = new PageRecyclerAdapter(getActivity(), 1);
            queryByNewsContentId(newsType.getTypeId());
            Log.d(InitApp.TAG, "typeId: "+newsType.getTypeId());
        } else {
            adapter = new PageRecyclerAdapter(getActivity(), 0);
            queryByNewsContentId(newsType.getTypeId());
            Log.d(InitApp.TAG, "typeId: "+newsType.getTypeId());
        }

    }

    @Override
    public void initListener() {
        //实现下拉刷新接口监听
        pageSwipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            if (pageSwipeRefreshLayout.isRefreshing()) {
                pageSwipeRefreshLayout.setRefreshing(false);
            }
            Toast.makeText(getContext(), "刷新成功！", Toast.LENGTH_SHORT).show();
        }, 3000));

        pageRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) { //事件发生的状态
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastItem + 1) == layoutManager.getItemCount()) {
                    if (isMore) {
                        isMore = false;
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
                                List<NewsContent> contents = gson.fromJson(result, new TypeToken<List<NewsContent>>() {
                                }.getType());
                                //添加数据
                                contentList.addAll(contents);
                                //刷新适配器
                                adapter.notifyDataSetChanged();
                                isMore = true;
                                Toast.makeText(getActivity(), "加载成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //记录布局管理器滚动之后的位置 索引从0开始
                lastItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        //点击Item监听
        adapter.setOnItemClickListener((view, object) -> {
            NewsContent content = (NewsContent) object;
            Toast.makeText(getActivity(), content.getNewsTheme(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), NewDetailsActivity.class);
            intent.putExtra("newsId", content.getNewsId());
            getActivity().startActivity(intent);
        });

    }

    @Override
    public void bindData() {
        if (contentList != null) {
            pageRecyclerview.setVisibility(View.VISIBLE);
            homeFramelayout.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            error.setVisibility(View.GONE);

            //添加数据
            adapter.setContentList(contentList);
            pageRecyclerview.setAdapter(adapter);
        } else {
            pageRecyclerview.setVisibility(View.GONE);
            homeFramelayout.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 新闻内容请求
     * @param typeId 新闻类型编号
     */
    private void queryByNewsContentId(int typeId){
        OkhttpManager.getAsync(InitApp.ip_port + "queryByNewsContentId?typeId=" + typeId, new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, Exception e) {
                Toast.makeText(getContext(), "加载失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void requestSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    /**
                     * Gson无法解析数位太长的时间格式：
                     * 解决方案：自定义和注册Gson适配器
                     */
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json1, typeOfT, context) -> new Date(json1.getAsJsonPrimitive().getAsLong()));
                    Gson gson = builder.create();
                    contentList = gson.fromJson(json.getString("ROWS_DETAIL"), new TypeToken<List<NewsContent>>(){}.getType());
                    bindData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
