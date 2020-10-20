package com.song.xposed.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.song.xposed.DetailsActivity;
import com.song.xposed.R;
import com.song.xposed.beans.ApplicationBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chensongsong on 2020/10/13.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ApplicationBean> data;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    public void updateData(List<ApplicationBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.isEmpty(data.get(position).getTitle())) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View root = LayoutInflater.from(context).inflate(R.layout.item_adapter_applist, parent, false);
            return new HomeHolder(root);
        } else {
            View root = LayoutInflater.from(context).inflate(R.layout.item_adapter_applist_title, parent, false);
            return new TitleHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ApplicationBean bean = data.get(position);
        if (TextUtils.isEmpty(bean.getTitle())) {
            HomeHolder homeHolder = (HomeHolder) holder;
            homeHolder.icon.setImageDrawable(bean.getIcon());
            homeHolder.nameTv.setText(bean.getName());
            homeHolder.packageNameTv.setText(bean.getPackageName());
            homeHolder.root.setOnClickListener((view) -> {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(context, DetailsActivity.class);
                intent.putExtra("app", bean);
                context.startActivity(intent);
            });
        } else {
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.titleTv.setText(bean.getTitle());
        }
    }

    static class HomeHolder extends RecyclerView.ViewHolder {

        View root;
        @BindView(R.id.tv_name)
        TextView nameTv;
        @BindView(R.id.tv_package_name)
        TextView packageNameTv;
        @BindView(R.id.imageView)
        ImageView icon;

        HomeHolder(View view) {
            super(view);
            root = view;
            ButterKnife.bind(this, root);
        }
    }

    static class TitleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView titleTv;

        TitleHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
