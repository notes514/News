package com.example.news.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.example.news.MainActivity;
import com.example.news.R;
import com.example.news.ui.base.BaseActivity;
import com.example.news.widget.CustomVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 欢迎引导类
 * Name：laodai
 * Time：2019.08.01
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.welcome_video)
    CustomVideoView welcomeVideo;
    @BindView(R.id.welcome_btn)
    Button welcomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        welcomeVideo.setVideoURI(Uri.parse("android.resource://"+this.getPackageName()+"/raw/"+R.raw.kr36));
        welcomeVideo.start();
        //结束后继续循环播放
        welcomeVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                welcomeVideo.start();
            }
        });
    }

    /**
     * App 版本号
     * @param context 上下文
     * @return
     */
    private String getAppVersionName(Context context) {
        String versionName = "";
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = packageInfo.versionName;
        if (TextUtils.isEmpty(versionName)) return "";
        return versionName;
    }

    @OnClick(R.id.welcome_btn)
    public void onViewClicked() {
        if (welcomeVideo.isPlaying()) {
            welcomeVideo.stopPlayback();
            welcomeVideo = null;
        }
        openActivity(MainActivity.class);
        this.finish();
    }
}
