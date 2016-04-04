package com.science.carnetplus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.science.carnetplus.R;
import com.science.carnetplus.adapter.baseAdapter.BaseAdapter;
import com.science.carnetplus.adapter.baseAdapter.ViewHolder;

import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description 汽车品牌列表
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/1
 */

public class CarBrandAdapter extends BaseAdapter<List<String>> {

    public CarBrandAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public int onCreateNormalViewLayoutID(int viewType) {
        return R.layout.item_car_brand;
    }

    @Override
    public void onBindNormalViewHolder(ViewHolder holder, int position) {
        holder.setTextView(R.id.text_car_brand, getList().get(position).get(position));
    }
}
