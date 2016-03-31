package com.science.carnetplus.adapter.baseAdapter;

import android.content.Context;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.RecyclerView;

import com.science.carnetplus.R;
import com.science.carnetplus.util.CommonDefine;

import java.util.Map;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/31
 */

public class MyCarAdapter extends BaseAdapter<Map<String, String>> {

    private CircularArray<Map<String, String>> list;

    public MyCarAdapter(Context context, RecyclerView recyclerView, CircularArray<Map<String, String>> ts) {
        super(context, recyclerView, ts);
        list = ts;
    }

    @Override
    public int onCreateNormalViewLayoutID(int viewType) {
        return R.layout.item_my_cars;
    }

    @Override
    public void onBindNormalViewHolder(ViewHolder holder, final int position) {
        Map<String, String> item = list.get(position);
        holder.setTextView(R.id.number, position + 1 + "，");
        holder.setTextView(R.id.car_number, item.get(CommonDefine.CAR_NUMBER));
        holder.setTextView(R.id.car_oil_number, item.get(CommonDefine.CAR_OIL_NUMBER));
        holder.setTextView(R.id.car_brand, item.get(CommonDefine.CAR_BRAND));
        holder.setTextView(R.id.car_model, item.get(CommonDefine.CAR_MODEL));
        holder.setTextView(R.id.car_color, item.get(CommonDefine.CAR_COLOR));
    }
}