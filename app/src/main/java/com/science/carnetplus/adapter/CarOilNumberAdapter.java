package com.science.carnetplus.adapter;

import android.content.Context;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.RecyclerView;

import com.science.carnetplus.R;
import com.science.carnetplus.adapter.baseAdapter.BaseAdapter;
import com.science.carnetplus.adapter.baseAdapter.ViewHolder;

import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/31
 */

public class CarOilNumberAdapter extends BaseAdapter<List<String>> {

    private CircularArray<List<String>> mArray;

    public CarOilNumberAdapter(Context context, RecyclerView recyclerView, CircularArray<List<String>> ts) {
        super(context, recyclerView, ts);
        mArray = ts;
    }

    @Override
    public int onCreateNormalViewLayoutID(int viewType) {
        return R.layout.item_bottom_dialog_car;
    }

    @Override
    public void onBindNormalViewHolder(ViewHolder holder, final int position) {
        List<String> list = mArray.get(position);
        holder.setTextView(R.id.text_car, list.get(position));
    }
}
