package com.song.xposed.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class FloatingActionMenuBehavior extends CoordinatorLayout.Behavior<com.github.clans.fab.FloatingActionMenu> {

    public FloatingActionMenuBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, com.github.clans.fab.FloatingActionMenu child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, com.github.clans.fab.FloatingActionMenu child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, com.github.clans.fab.FloatingActionMenu child, View dependency) {
        float translationY = Math.max(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        super.onDependentViewRemoved(parent, child, dependency);
    }
}