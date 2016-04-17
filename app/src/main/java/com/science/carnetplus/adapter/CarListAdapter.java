package com.science.carnetplus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.util.CommonDefine;
import com.science.carnetplus.widget.LabelView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 幸运Science-陈土燊
 * @description 我的汽车列表适配器
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/31
 */

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String, String>> mMapList;

    public CarListAdapter(Context context) {
        mContext = context;
        mMapList = new ArrayList<Map<String, String>>();
    }

    public List<Map<String, String>> getMapList() {
        return mMapList;
    }

    public void setMapList(List<Map<String, String>> mapList) {
        mMapList = mapList;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_cars, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.carNumber.setText(mMapList.get(position).get(CommonDefine.CAR_NUMBER));
        holder.carOilNumber.setText(mMapList.get(position).get(CommonDefine.CAR_OIL_NUMBER));
        holder.carBrand.setText(mMapList.get(position).get(CommonDefine.CAR_BRAND));
        holder.carType.setText(mMapList.get(position).get(CommonDefine.CAR_TYPE));
        holder.carColor.setText(mMapList.get(position).get(CommonDefine.CAR_COLOR));
        if ("0".equals(mMapList.get(position).get(CommonDefine.CAR_DEFAULT))) {
            holder.mLabelView.setVisibility(View.VISIBLE);
        } else {
            holder.mLabelView.setVisibility(View.INVISIBLE);
        }
        if (mOnItemClickListener != null) {
            holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMapList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout mItemLayout;
        private TextView carNumber, carOilNumber, carBrand, carType, carColor;
        private LabelView mLabelView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemLayout = (FrameLayout) itemView.findViewById(R.id.item_layout);
            carNumber = (TextView) itemView.findViewById(R.id.car_number);
            carOilNumber = (TextView) itemView.findViewById(R.id.car_oil_number);
            carBrand = (TextView) itemView.findViewById(R.id.car_brand);
            carType = (TextView) itemView.findViewById(R.id.car_type);
            carColor = (TextView) itemView.findViewById(R.id.car_color);
            mLabelView = (LabelView) itemView.findViewById(R.id.labelView);
        }
    }
}