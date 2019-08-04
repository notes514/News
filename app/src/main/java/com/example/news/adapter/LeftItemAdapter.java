package com.example.news.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.application.InitApp;
import com.example.news.entity.LeftItemMenu;
import com.example.news.utils.MenuDataUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeftItemAdapter extends RecyclerView.Adapter<LeftItemAdapter.ViewHolder> {

    private List<LeftItemMenu> leftItemMenus;
    private Context context;

    public LeftItemAdapter(Context context) {
        this.context = context;
        leftItemMenus = MenuDataUtils.getItemMenus();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.left_item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.leftMenuImage.setImageResource(leftItemMenus.get(position).getLeftIcon());
        holder.leftMenuName.setText(leftItemMenus.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(InitApp.TAG, "点击了第 "+position+" 行");
            }
        });

    }

    @Override
    public int getItemCount() {
        return leftItemMenus != null ? leftItemMenus.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.left_menu_image)
        ImageView leftMenuImage;
        @BindView(R.id.left_menu_name)
        TextView leftMenuName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
