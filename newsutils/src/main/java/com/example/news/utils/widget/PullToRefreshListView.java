package com.example.news.utils.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.news.utils.R;

public class PullToRefreshListView extends ListView implements OnScrollListener, OnClickListener {
    private static final String TAG = "PullToRefreshListViewTest";
    /**
     * 下拉状态
     * 使用十六进制对二进制进行缩写
     */
    private static final int STATE_ONE = 0x0000;
    private static final int STATE_TWO = 0x0001;
    private static final int STATE_THREE = 0x0010;

    /**
     * 列表刷新监听器
     */
    private OnRefreshListener onRefreshListener;

    /**
     * 组件滑动监听器 scroll， 当view在进行下拉滑动的时候，判断滑动的距离，
     * 如果达到可以进行刷新的临界点时候，回调当前接口中的方法。
     */
    private OnScrollListener onScrollListener;

    /**
     * 下拉刷新头部View
     */
    private LinearLayout pullToRefreshView;  //头部
    private ImageView pullHeaderImage;         //箭头
    private ProgressBar pullHeaderProgress;    //进度条
    private TextView pullHeaderTextState;      //刷新状态提示
    private TextView pullHeaderTextUpdateTime; //上次刷新时间提示

    private int refreshState; //刷新状态
    private int currentScrollState; //当前滚动状态

    private RotateAnimation flipAnimation; //翻转动画
    private RotateAnimation reverseFlipAnimation; //反翻转动画

    private int refreshViewHeight; //刷新视图高度
    private int refreshOriginalTopPadding; //原始刷新添加
    private int lastMotionY; //最后

    public PullToRefreshListView(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 数据初始化
     * @param context 上下文
     */
    private void initView(Context context) {
        //初始化动画翻转效果
        flipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        flipAnimation.setInterpolator(new LinearInterpolator());
        flipAnimation.setDuration(250);
        flipAnimation.setFillAfter(true);

        //初始化动画反翻转效果
        reverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseFlipAnimation.setInterpolator(new LinearInterpolator());
        reverseFlipAnimation.setDuration(250);
        reverseFlipAnimation.setFillAfter(true);

        pullToRefreshView = (LinearLayout) View.inflate(context, R.layout.pull_to_refresh_header_layout, null);
        pullHeaderImage = pullToRefreshView.findViewById(R.id.pull_header_image);
        pullHeaderProgress = pullToRefreshView.findViewById(R.id.pull_header_progress);
        pullHeaderTextState = pullToRefreshView.findViewById(R.id.pull_header_text_state);
        pullHeaderTextUpdateTime = pullToRefreshView.findViewById(R.id.pull_header_text_update_time);

        refreshState = STATE_ONE; //设置初始刷新状态
        pullHeaderImage.setMinimumHeight(50); //设置下拉刷新最小高度为50

        setFadingEdgeLength(0);
        setHeaderDividersEnabled(false);

        this.addHeaderView(pullToRefreshView); //把pullToRefreshView加入到ListView的头部
        super.setOnScrollListener(this);
        pullToRefreshView.setOnClickListener(this);

        pullToRefreshView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED); //宽高未指定
        refreshViewHeight = pullToRefreshView.getMeasuredHeight();
        refreshOriginalTopPadding = -refreshViewHeight;

        //初始化页眉
        resetHeaderPadding();
    }

    /**
     * 设置每次列表滚动时接收通知的侦听器。
     * @param l 滚动侦听器
     */
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        this.onScrollListener = l;
    }

    /**
     * 注册ListView下拉刷新回调接口
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    /**
     * 设置上一次更新的时间
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(CharSequence lastUpdateTime) {
        if (lastUpdateTime != null) {
            pullHeaderTextUpdateTime.setVisibility(VISIBLE);
            pullHeaderTextUpdateTime.setText(lastUpdateTime);
        } else {
            pullHeaderTextUpdateTime.setVisibility(GONE);
        }
    }

    /**
     * touch事件处理
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetY = (int) event.getY();
                int deltY = Math.round(offsetY - lastMotionY);
                lastMotionY = offsetY;

                if (getFirstVisiblePosition() == 0 && refreshState != STATE_THREE) {
                    deltY = deltY / 2;
                    refreshOriginalTopPadding += deltY;
                    if (refreshOriginalTopPadding < -refreshViewHeight) {
                        refreshOriginalTopPadding = -refreshViewHeight;
                    }
                    resetHeaderPadding();
                }
                break;
            case MotionEvent.ACTION_UP:
                //当手指抬开得时候 进行判断下拉的距离 ，如果>=临界值，那么进行刷洗，否则回归原位
                if (!isVerticalScrollBarEnabled()) {
                    setVerticalScrollBarEnabled(true);
                }
                if (getFirstVisiblePosition() == 0 && refreshState != STATE_THREE) {
                    if (pullToRefreshView.getBottom() >= refreshViewHeight && refreshState == STATE_TWO) {
                        //准备开始刷新
                        prepareForRefresh();
                    } else {
                        //终止刷新
                        resetHeader();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        currentScrollState = scrollState;
        if (onScrollListener != null) {
            onScrollListener.onScrollStateChanged(absListView, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (currentScrollState == SCROLL_STATE_TOUCH_SCROLL && refreshState != STATE_THREE) {
            if (firstVisibleItem == 0) {
                if ((pullToRefreshView.getBottom() >= refreshViewHeight) && refreshState != STATE_TWO) {
                    pullHeaderTextState.setText(R.string.pull_to_refresh_release_label_it);
                    pullHeaderImage.clearAnimation(); //关闭动画
                    pullHeaderImage.startAnimation(flipAnimation); //执行动画翻转
                    refreshState = STATE_TWO;
                } else if (pullToRefreshView.getBottom() < refreshViewHeight && refreshState != STATE_ONE) {
                    pullHeaderTextState.setText(R.string.pull_to_refresh_pull_label_it);
                    pullHeaderImage.clearAnimation();
                    pullHeaderImage.startAnimation(reverseFlipAnimation);
                    refreshState = STATE_ONE;
                }
            }
        }

        if (onScrollListener != null) {
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ");
    }

    /**
     * 设置页眉填充为原始大小
     */
    private void resetHeaderPadding() {
        pullToRefreshView.setPadding(pullToRefreshView.getPaddingLeft(),
                refreshOriginalTopPadding,
                pullToRefreshView.getPaddingRight(),
                pullToRefreshView.getPaddingBottom());
    }

    /**
     * 准备开始刷新
     */
    private void prepareForRefresh() {
        if (refreshState != STATE_THREE) {
            refreshState = STATE_THREE; //设置状态为正在刷新

            refreshOriginalTopPadding = 0; //设置原始刷新添加为0
            resetHeaderPadding(); //设置页眉填充为原始大小

            pullHeaderImage.clearAnimation(); //关闭动画
            pullHeaderImage.setVisibility(GONE); //隐藏箭头指示
            pullHeaderProgress.setVisibility(VISIBLE); //显示进度条
            pullHeaderTextState.setText(R.string.pull_to_refresh_refreshing_label_it);

            onRefresh(); //开始回调刷新
        }
    }

    /**
     * 终止刷新
     */
    private void resetHeader() {
        refreshState = STATE_ONE; //初始状态

        refreshOriginalTopPadding = -refreshViewHeight;
        resetHeaderPadding();

        pullHeaderImage.clearAnimation();
        pullHeaderImage.setVisibility(VISIBLE);
        pullHeaderProgress.setVisibility(GONE);
        pullHeaderTextState.setText(R.string.pull_to_refresh_pull_label_it);
    }

    /**
     * 开始回调刷新
     */
    @SuppressLint("LongLogTag")
    private void onRefresh() {
        Log.d(TAG, "onRefresh: ");
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    /**
     * 刷新后将列表重置为正常状态。
     */
    @SuppressLint("LongLogTag")
    public void onRefreshComplete() {
        Log.d(TAG, "onRefreshComplete");
        resetHeader();
    }

    /**
     * 列表刷新回调接口
     */
    public interface OnRefreshListener {
        /**
         * 列表刷新回调方法
         */
        void onRefresh();
    }

}
