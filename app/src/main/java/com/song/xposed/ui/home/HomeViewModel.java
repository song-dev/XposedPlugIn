package com.song.xposed.ui.home;

import com.song.xposed.beans.ApplicationBean;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by chensongsong on 2020/10/13.
 */
public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<ApplicationBean>> recyclerView;

    public HomeViewModel() {
        recyclerView = new MutableLiveData<>();
    }

    public LiveData<List<ApplicationBean>> getRecyclerView() {
        return recyclerView;
    }

    public void setValue(List<ApplicationBean> list) {
        recyclerView.setValue(list);
    }
}