package com.science.carnetplus.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.science.carnetplus.util.MyLogger;

/**
 * @author 幸运Science-陈土燊
 * @description RecyclerView Bug：IndexOutOfBoundsException: Inconsistency detected.
 * Invalid view holder adapter的解决方案
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/5
 */

public class WrapContentLinearLayoutManager extends LinearLayoutManager {

    public WrapContentLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            MyLogger.e(e);
        }
    }
}
