package com.song.xposed.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.song.xposed.R;
import com.song.xposed.adapter.HomeAdapter;
import com.song.xposed.beans.ApplicationBean;
import com.song.xposed.infos.AppListInfo;
import com.song.xposed.utils.ThreadPoolUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HomeAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        adapter = new HomeAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        homeViewModel.getRecyclerView().observe(getViewLifecycleOwner(), list -> {
            swipeRefreshLayout.setRefreshing(false);
            adapter.updateData(list);
        });
        setSwipeRefreshLayout();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            refreshData();
        });
    }

    private void refreshData() {
        ThreadPoolUtils.getInstance().execute(() -> {
            final List<ApplicationBean> list = AppListInfo.getAppListInfo(getContext());
            mainHandler.post(() -> {
                homeViewModel.setValue(list);
            });
        });
    }

}
