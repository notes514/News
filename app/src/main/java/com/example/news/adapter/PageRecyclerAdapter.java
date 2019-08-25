package com.example.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.application.InitApp;
import com.example.news.entity.NewsContent;
import com.example.news.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOT = 1;
    private Context context;
    private LayoutInflater inflater;
    private int type;
    private OnItemClickListener onItemClickListener;
    private List<NewsContent> contentList;

    public PageRecyclerAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            if (type == 0) {
                View view = inflater.inflate(R.layout.home_news_item_layout, parent, false);
                view.setOnClickListener(this::onClick);
                PViewHolder holder = new PViewHolder(view);
                return holder;
            } else if (type == 1) {
                View view = inflater.inflate(R.layout.page_news_tv_item_layout, parent, false);
                view.setOnClickListener(this::onClick);
                TViewHolder holder = new TViewHolder(view);
                return holder;
            }
        } else if (viewType == TYPE_FOOT) {
            View view = inflater.inflate(R.layout.load_more_footview_layout, parent, false);
            FViewHolder holder = new FViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsContent content = contentList.get(position);
        if (holder instanceof PViewHolder) {
            //显示普通列表
            holder.itemView.setTag(content);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            ((PViewHolder) holder).homeNewsTheme.setText(content.getNewsTheme());
            ((PViewHolder) holder).homeNewsLibs.setText(content.getNewsPress());
            ((PViewHolder) holder).homeNewsTime.setText(format.format(content.getNewsTime()));
            ImageLoader.getInstance().displayImage(InitApp.ip_images+content.getNewsPic(),
                    ((PViewHolder) holder).homeNewsImage, InitApp.getOptions());
        } else if (holder instanceof TViewHolder) {
            //显示视频列表
            holder.itemView.setTag(content);
            ImageLoader.getInstance().displayImage(InitApp.ip_images+content.getNewsPic(), ((TViewHolder) holder).pageTvImageBack, InitApp.getOptions());
            ((TViewHolder) holder).pageTvTextTitle.setText(content.getNewsTheme());
            ((TViewHolder) holder).pageTvTextTime.setText("03:00");
            ImageLoader.getInstance().displayImage(InitApp.ip_images+content.getNewsReleasePic(), ((TViewHolder) holder).pageTvImageReleaseHead, InitApp.getOptions());
            ((TViewHolder) holder).pageTvTextReleaseName.setText(content.getNewsPress());
        } else if (holder instanceof FViewHolder) {
            //显示上拉加载更多布局
        }
    }

    @Override
    public int getItemCount() {
        return contentList == null ? 0 : contentList.size(); //三元运算符
    }

    /**
     * 重写区分加载布局类型
     * @param position 每列的一个索引
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //如果索引指定到可见列表页面的最后一项，则返回FOOT布局
        if (position + 1 == getItemCount()) {
            return TYPE_FOOT;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, view.getTag());
        }
    }

    //普通ViewHolder
    class PViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.home_news_theme)
        TextView homeNewsTheme;
        @BindView(R.id.home_news_libs)
        TextView homeNewsLibs;
        @BindView(R.id.home_news_time)
        TextView homeNewsTime;
        @BindView(R.id.home_news_image)
        ImageView homeNewsImage;

        public PViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //视频列表ViewHolder
    class TViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.page_tv_image_back)
        ImageView pageTvImageBack;
        @BindView(R.id.page_tv_image_play)
        ImageView pageTvImagePlay;
        @BindView(R.id.page_tv_text_title)
        TextView pageTvTextTitle;
        @BindView(R.id.page_tv_text_time)
        TextView pageTvTextTime;
        @BindView(R.id.page_tv_image_release_head)
        CircleImageView pageTvImageReleaseHead;
        @BindView(R.id.page_tv_text_release_name)
        TextView pageTvTextReleaseName;
        @BindView(R.id.page_tv_image_fabulous)
        ImageView pageTvImageFabulous;
        @BindView(R.id.page_tv_text_fabulous_number)
        TextView pageTvTextFabulousNumber;
        @BindView(R.id.page_tv_image_comment)
        ImageView pageTvImageComment;
        @BindView(R.id.page_tv_text_comment)
        TextView pageTvTextComment;
        @BindView(R.id.page_tv_image_share)
        ImageView pageTvImageShare;

        public TViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //上拉加载更多子布局
    class FViewHolder extends RecyclerView.ViewHolder {

        public FViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    //******************Item 添加类OnItemClickListener 时间监听方法*******************
    public interface OnItemClickListener {
        /**
         * 当内部的Item发生点击的时候 调用Item点击回调方法
         * @param view    点击的View
         * @param object  回调的数据
         */
        void onItemClick(View view, Object object);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 添加设置新闻数据
     * @param contentList
     */
    public void setContentList(List<NewsContent> contentList) {
        this.contentList = contentList;
    }
}
