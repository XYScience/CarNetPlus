package com.science.carnetplus.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.util.AVOSUtils;
import com.science.carnetplus.util.BottomSheetBehaviorUtils;
import com.science.carnetplus.util.CommonUtils;

/**
 * @author 幸运Science-陈土燊
 * @description 添加车辆
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/29
 */

public class AddCarActivity extends BaseActivity implements View.OnClickListener {

    private AVOSUtils mAVOSUtils;
    private CoordinatorLayout mRootLayout;
    private RelativeLayout mLayoutContent;
    private EditText mEditCarNumber;
    private RelativeLayout mLayoutCarOilNumber, mLayoutCarBrand, mLayoutCarType, mLayoutCarColor;
    private TextView mTextCarOilNumber, mTextCarBrand, mTextCarType, mTextCarColor;
    private CoordinatorLayout mCoordinatorBottom;
    private View mDarkenLayout;
    private BottomSheetBehavior mSheetBehavior;
    private View mBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_car);
        setToolbar(getString(R.string.add_car));

        mRootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        mLayoutContent = (RelativeLayout) findViewById(R.id.content_layout);
        mEditCarNumber = (EditText) findViewById(R.id.edit_car_number);
        mLayoutCarOilNumber = (RelativeLayout) findViewById(R.id.layout_car_oil_number);
        mLayoutCarBrand = (RelativeLayout) findViewById(R.id.layout_car_brand);
        mLayoutCarType = (RelativeLayout) findViewById(R.id.layout_car_type);
        mLayoutCarColor = (RelativeLayout) findViewById(R.id.layout_car_color);
        mTextCarOilNumber = (TextView) findViewById(R.id.text_car_oil_number);
        mTextCarBrand = (TextView) findViewById(R.id.text_car_brand);
        mTextCarType = (TextView) findViewById(R.id.text_car_type);
        mTextCarColor = (TextView) findViewById(R.id.text_car_color);
        mCoordinatorBottom = (CoordinatorLayout) findViewById(R.id.coordinator_bottom);
        mDarkenLayout = findViewById(R.id.darken_layout);
        mBottomSheet = findViewById(R.id.design_bottom_sheet);
        mSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        CommonUtils.materialRipple(mLayoutCarOilNumber, "#585858");
        CommonUtils.materialRipple(mLayoutCarBrand, "#585858");
        CommonUtils.materialRipple(mLayoutCarType, "#585858");
        CommonUtils.materialRipple(mLayoutCarColor, "#585858");
        mAVOSUtils = AVOSUtils.getInstance();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mLayoutCarOilNumber.setOnClickListener(this);
        mLayoutCarBrand.setOnClickListener(this);
        mLayoutCarType.setOnClickListener(this);
        mLayoutCarColor.setOnClickListener(this);

        // 点击屏幕隐藏软键盘
        hideKeyBoard(mLayoutContent, mEditCarNumber);

        // BottomSheet展开时，点击暗色屏幕收起BottomSheet
        mDarkenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // 取消滑动BottomSheet以外的地方而拖出BottomSheet
        mRootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                return true;
            }
        });

        mSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            boolean hasRequest;

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mDarkenLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                // BottomSheet展开和收起屏幕变暗变亮
                mDarkenLayout.setVisibility(View.VISIBLE);
                ViewCompat.setAlpha(mDarkenLayout, slideOffset);

                if (!hasRequest && mSheetBehavior.getPeekHeight() == 0 && slideOffset > 0) {
                    hasRequest = true;
                    BottomSheetBehaviorUtils.updateOffsets(bottomSheet);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_car_oil_number:
                break;

            case R.id.layout_car_brand:
                break;

            case R.id.layout_car_type:
                break;

            case R.id.layout_car_color:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.done:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
