package com.science.carnetplus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.adapter.CarOilNumberAdapter;
import com.science.carnetplus.util.AVOSUtils;
import com.science.carnetplus.util.CommonDefine;
import com.science.carnetplus.util.CommonUtils;
import com.science.carnetplus.util.SnackbarUtils;
import com.science.carnetplus.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description 添加车辆
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/29
 */

public class AddCarActivity extends BaseActivity implements View.OnClickListener {

    private AVOSUtils mAVOSUtils;
    private CoordinatorLayout mRootLayout, mCoordinatorSnackBar;
    private RelativeLayout mLayoutContent;
    private EditText mEditCarNumber;
    private RelativeLayout mLayoutCarOilNumber, mLayoutCarBrand, mLayoutCarType, mLayoutCarColor;
    private TextView mTextCarOilNumber, mTextCarBrand, mTextCarType, mTextCarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_car);
        setToolbar(getString(R.string.add_car));

        mRootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        mCoordinatorSnackBar = (CoordinatorLayout) findViewById(R.id.coordinator_snackbar);
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
    }

    @Override
    public void onClick(View v) {
        CommonUtils.hideKeyboard(mEditCarNumber, AddCarActivity.this);
        switch (v.getId()) {
            case R.id.layout_car_oil_number:
                carDialog(0);
                break;

            case R.id.layout_car_brand:
                Intent intent = new Intent(AddCarActivity.this, CarBrandActivity.class);
                startActivityForResult(intent, CommonDefine.INTENT_REQUSET);
                break;

            case R.id.layout_car_type:
                carDialog(1);
                break;

            case R.id.layout_car_color:
                carDialog(2);
                break;
        }
    }

    CircularArray<List<String>> array = null;

    /**
     * 底部dialog
     *
     * @param i
     */
    private void carDialog(final int i) {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_car, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_bottom_car);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        if (i == 0) {
            array = getCarOilNum();
        } else if (i == 1) {
            array = getCarType();
        } else {
            array = getCarColor();
        }
        CarOilNumberAdapter adapter = new CarOilNumberAdapter(AddCarActivity.this, recyclerView, array);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (i == 0) {
                    mTextCarOilNumber.setText(array.get(position).get(position));
                } else if (i == 1) {
                    mTextCarType.setText(array.get(position).get(position));
                } else {
                    mTextCarColor.setText(array.get(position).get(position));
                }
            }
        });
        ImageView scrollMore = (ImageView) view.findViewById(R.id.img_scroll_more);
        scrollMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showMessage(AddCarActivity.this, "上下滑动查看更多！");
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 油号列表
     *
     * @return
     */
    private CircularArray<List<String>> getCarOilNum() {
        CircularArray<List<String>> array = new CircularArray<>();
        List<String> list = new ArrayList<>();
        list.add("90#");
        array.addLast(list);
        list.add("92#");
        array.addLast(list);
        list.add("93#");
        array.addLast(list);
        list.add("95#");
        array.addLast(list);
        list.add("97#");
        array.addLast(list);
        list.add("98#");
        array.addLast(list);
        list.add("柴油");
        array.addLast(list);
        return array;
    }

    /**
     * 车型
     *
     * @return
     */
    private CircularArray<List<String>> getCarType() {
        CircularArray<List<String>> array = new CircularArray<>();
        List<String> list = new ArrayList<>();
        list.add("轿车");
        array.addLast(list);
        list.add("SUV");
        array.addLast(list);
        list.add("小客车");
        array.addLast(list);
        list.add("跑车");
        array.addLast(list);
        list.add("面包车");
        array.addLast(list);
        list.add("大卡车");
        array.addLast(list);
        list.add("大客车");
        array.addLast(list);
        list.add("工程车");
        array.addLast(list);
        return array;
    }

    /**
     * 汽车颜色
     *
     * @return
     */
    private CircularArray<List<String>> getCarColor() {
        CircularArray<List<String>> array = new CircularArray<>();
        List<String> list = new ArrayList<>();
        list.add("黑色");
        array.addLast(list);
        list.add("白色");
        array.addLast(list);
        list.add("银灰色");
        array.addLast(list);
        list.add("红色");
        array.addLast(list);
        list.add("蓝色");
        array.addLast(list);
        list.add("黄色");
        array.addLast(list);
        list.add("其他颜色");
        array.addLast(list);
        return array;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == CommonDefine.INTENT_REQUSET && resultCode == RESULT_OK) {
            mTextCarBrand.setText(data.getStringExtra(CommonDefine.CAR_BRAND));
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
                addCarDone();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addCarDone() {
        CommonUtils.hideKeyboard(mEditCarNumber, AddCarActivity.this);
        if (CommonUtils.isCarnumberNO(mEditCarNumber.getText().toString())) {

        } else {
            SnackbarUtils.showSnackbar(mCoordinatorSnackBar, getString(R.string.car_number_please_enter_correct));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
