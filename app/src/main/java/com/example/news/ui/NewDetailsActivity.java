package com.example.news.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.R;
import com.example.news.application.InitApp;
import com.example.news.application.OkhttpManager;
import com.example.news.common.DefineView;
import com.example.news.entity.NewsContent;
import com.example.news.entity.NewsDetails;
import com.example.news.ui.base.BaseActivity;
import com.example.news.widget.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

/**
 * 新闻内容详情类
 * Name：laodai
 * Time：2019.08.25
 */
public class NewDetailsActivity extends BaseActivity implements DefineView {

    @BindView(R.id.top_bar_back_btn)
    Button topBarBackBtn;
    @BindView(R.id.top_bar_title)
    TextView topBarTitle;
    @BindView(R.id.top_bar_typeface_btn)
    Button topBarTypefaceBtn;
    @BindView(R.id.top_bar_share_btn)
    Button topBarShareBtn;
    @BindView(R.id.detalis_content_relative)
    RelativeLayout detalisContentRelative;
    @BindView(R.id.foot_comment_btn)
    Button footCommentBtn;
    @BindView(R.id.foot_collection_btn)
    Button footCollectionBtn;
    @BindView(R.id.foot_share_btn)
    Button footShareBtn;
    @BindView(R.id.foot_news_content_edit)
    EditText footNewsContentEdit;
    @BindView(R.id.detalis_news_title_text)
    TextView detalisNewsTitleText;
    @BindView(R.id.details_user_image)
    CircleImageView detailsUserImage;
    @BindView(R.id.details_user_text)
    TextView detailsUserText;
    @BindView(R.id.details_time_text)
    TextView detailsTimeText;
    @BindView(R.id.details_content_image)
    ImageView detailsContentImage;
    @BindView(R.id.details_webview)
    WebView detailsWebview;
    @BindView(R.id.details_framelayout)
    FrameLayout detailsFramelayout;
    //数据
    private NewsContent newsContent;
    private NewsDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);
        ButterKnife.bind(this);
        setStatusBar(); //设置沉浸式状态栏
        initView();
        initValidata();
        initListener();
    }

    @Override
    public void initView() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initValidata() {
        detailsWebview.setWebChromeClient(new MyWebChromeClient());
        detailsWebview.setWebViewClient(new MyWebViewClient());
        WebSettings settings = detailsWebview.getSettings();
        settings.setJavaScriptEnabled(true); //开启JavaScript
        settings.setDomStorageEnabled(true); //开启DOM
        settings.setDefaultTextEncodingName("utf-8"); //设置编码
        //web 页面处理
        settings.setAllowFileAccess(true); //支持文件流
        //提高网页加载速度，暂时阻塞图片加载，然后网页加载好了，再进行加载图片
        settings.setBlockNetworkImage(true);
        //开启缓存机制
        settings.setAppCacheEnabled(true);
        //获取新闻ID
        int typeId = this.getIntent().getIntExtra("newsId", 0);
        queryByNewsDetailsId(typeId);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void bindData() {
        detalisNewsTitleText.setText(newsContent.getNewsTheme());
        ImageLoader.getInstance().displayImage(InitApp.ip_images+newsContent.getNewsReleasePic(), detailsUserImage, InitApp.getOptions());
        detailsUserText.setText(newsContent.getNewsPress());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        detailsTimeText.setText(format.format(newsContent.getNewsTime()));
        ImageLoader.getInstance().displayImage(InitApp.ip_images+newsContent.getNewsPic(), detailsContentImage, InitApp.getOptions());
        detailsWebview.loadUrl("https://36kr.com/p/5239678");
    }

    private void queryByNewsDetailsId(int newId) {
        OkhttpManager.getAsync(InitApp.ip_port + "queryByNewsDetailsId?newsId=" + newId, new OkhttpManager.DataCallBack() {
            @Override
            public void requestFailure(Request request, Exception e) {
                Toast.makeText(NewDetailsActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
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
                    newsContent = gson.fromJson(json.getString("ROWS_DETAIL_NEWSCONTENT"), NewsContent.class);
                    details = gson.fromJson(json.getString("ROWS_DETAIL"), NewsDetails.class);
                    Log.d(InitApp.TAG, "newsContent: "+ newsContent.getNewsTheme());
                    bindData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.top_bar_back_btn, R.id.top_bar_typeface_btn, R.id.top_bar_share_btn, R.id.foot_comment_btn, R.id.foot_collection_btn, R.id.foot_share_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.top_bar_back_btn:
                NewDetailsActivity.this.finish();
                break;
            case R.id.top_bar_typeface_btn:
                Toast.makeText(this, "点击了字体！！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_bar_share_btn:
                Toast.makeText(this, "点击了分享！！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.foot_comment_btn:
                Toast.makeText(this, "点击了评论！！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.foot_collection_btn:
                Toast.makeText(this, "点击了收藏！！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.foot_share_btn:
                Toast.makeText(this, "点击了分享！！！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.d(InitApp.TAG,"加载进度发生变化:"+newProgress);
        }
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("zttjiangqq","网页开始加载:"+url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("zttjiangqq","网页加载完成..."+url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d("zttjiangqq","拦截到URL信息为:"+request.toString());
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d("zttjiangqq","加载的资源:"+url);
        }
    }

}
