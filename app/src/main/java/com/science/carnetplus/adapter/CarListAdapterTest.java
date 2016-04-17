package com.science.carnetplus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.science.carnetplus.R;
import com.science.carnetplus.adapter.baseAdapter.BaseAdapter;
import com.science.carnetplus.adapter.baseAdapter.ViewHolder;
import com.science.carnetplus.util.CommonDefine;
import com.science.carnetplus.widget.LabelView;

import java.util.Map;

/**
 * @author 幸运Science-陈土燊
 * @description 我的汽车列表适配器
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/31
 */

public class CarListAdapterTest extends BaseAdapter<Map<String, String>> {

    private LabelView mLabelView;
    private Map<Integer, Boolean> isSelect;

    public CarListAdapterTest(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    public void initSelect() {
        for (int i = 0; i < getList().size(); i++) {
            getIsSelect().put(i, false);
        }
    }

    public Map<Integer, Boolean> getIsSelect() {
        return isSelect;
    }

    @Override
    public int onCreateNormalViewLayoutID(int viewType) {
        return R.layout.item_my_cars;
    }

    @Override
    public void onBindNormalViewHolder(ViewHolder holder, final int position) {
        Map<String, String> item = getList().get(position);
        mLabelView = holder.getLabelView(R.id.labelView);
        if ("0".equals(item.get(CommonDefine.CAR_DEFAULT))) {
            mLabelView.setVisibility(View.VISIBLE);
        } else {
            mLabelView.setVisibility(View.INVISIBLE);
        }
        holder.setTextView(R.id.car_number, item.get(CommonDefine.CAR_NUMBER));
        holder.setTextView(R.id.car_oil_number, item.get(CommonDefine.CAR_OIL_NUMBER));
        holder.setTextView(R.id.car_brand, item.get(CommonDefine.CAR_BRAND));
        holder.setTextView(R.id.car_type, item.get(CommonDefine.CAR_TYPE));
        holder.setTextView(R.id.car_color, item.get(CommonDefine.CAR_COLOR));
    }
}