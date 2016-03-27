package com.science.carnetplus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.util.CommonUtils;
import com.science.carnetplus.widget.FABToolbar.FABToolbarLayout;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/20
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    private View mRootView;
    public FABToolbarLayout mFABToolbarLayout;
    private FloatingActionButton mFABToolbarButton;
    private TextView mTextAddOil, mTextCar4s, mTextCarWash, mTextCarPark;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        initListener();
        return mRootView;
    }

    private void initView() {
        mFABToolbarLayout = (FABToolbarLayout) mRootView.findViewById(R.id.fabtoolbar);
        mFABToolbarButton = (FloatingActionButton) mRootView.findViewById(R.id.fabtoolbar_fab);
        mTextAddOil = (TextView) mRootView.findViewById(R.id.text_add_oil);
        mTextCar4s = (TextView) mRootView.findViewById(R.id.text_car_4s);
        mTextCarWash = (TextView) mRootView.findViewById(R.id.text_car_wash);
        mTextCarPark = (TextView) mRootView.findViewById(R.id.text_car_park);
        CommonUtils.materialRipple(mTextAddOil, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCar4s, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCarWash, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCarPark, "#ffffff", 0.3f);
    }

    private void initListener() {
        mTextAddOil.setOnClickListener(this);
        mTextCar4s.setOnClickListener(this);
        mTextCarWash.setOnClickListener(this);
        mTextCarPark.setOnClickListener(this);

        mFABToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFABToolbarLayout.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_add_oil:
                break;
            case R.id.text_car_4s:
                break;
            case R.id.text_car_wash:
                break;
            case R.id.text_car_park:
                break;
        }
    }
}
