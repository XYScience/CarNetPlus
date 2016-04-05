package com.science.carnetplus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.science.carnetplus.R;
import com.science.carnetplus.adapter.MyCarAdapter;
import com.science.carnetplus.util.AVOSUtils;
import com.science.carnetplus.util.CommonDefine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 幸运Science-陈土燊
 * @description 我的车辆列表
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/24
 */

public class MyCarsInfoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabAddCar;
    private MyCarAdapter mMyAdapter;
    private TextView mTextTotalCars;
    private int carListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_my_cars_info);
        setToolbar(getString(R.string.my_cars));

        mFabAddCar = (FloatingActionButton) findViewById(R.id.fab_add_car);
        mTextTotalCars = (TextView) findViewById(R.id.total_cars);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyCarsInfoActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mMyAdapter = new MyCarAdapter(MyCarsInfoActivity.this, mRecyclerView);
        mRecyclerView.setAdapter(mMyAdapter);
    }

    @Override
    public void initData() {
        AVOSUtils.getInstance().getCarList(AVUser.getCurrentUser().getUsername(), new AVOSUtils.OnAVOSCallback() {
            @Override
            public void getAvaterListener(byte[] avatarBytes) {

            }

            @Override
            public void getUserInfoListener(List<AVObject> userInfoList) {
            }

            @Override
            public void getCarListListener(List<AVObject> carList) {
                carListSize = carList.size();
                mTextTotalCars.setText(getString(R.string.total_cars) + carListSize);
                setCarList(carList);
            }
        });
    }

    private void setCarList(List<AVObject> list) {
        CircularArray<Map<String, String>> mapList = new CircularArray<Map<String, String>>();
        if (list != null && list.size() != 0) {
            mMyAdapter.setLoadingComplete();
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(CommonDefine.CAR_NUMBER, list.get(i).getString(CommonDefine.CAR_NUMBER));
                map.put(CommonDefine.CAR_OIL_NUMBER, list.get(i).getString(CommonDefine.CAR_OIL_NUMBER));
                map.put(CommonDefine.CAR_BRAND, list.get(i).getString(CommonDefine.CAR_BRAND));
                map.put(CommonDefine.CAR_MODEL, list.get(i).getString(CommonDefine.CAR_MODEL));
                map.put(CommonDefine.CAR_COLOR, list.get(i).getString(CommonDefine.CAR_COLOR));
                mapList.addLast(map);
            }
            mMyAdapter.setList(mapList);
            mMyAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initListener() {
        mMyAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SnackbarUtils.showSnackbar(view, "click:" + (position + 1));
                mMyAdapter.notifyItemMoved(position, 0);
            }
        });

        mFabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCarsInfoActivity.this, AddCarActivity.class);
                startActivityForResult(intent, CommonDefine.INTENT_REQUSET);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == CommonDefine.INTENT_REQUSET && resultCode == RESULT_OK) {
            Map<String, String> map = new HashMap<String, String>();
            map.put(CommonDefine.CAR_NUMBER, data.getStringExtra(CommonDefine.CAR_NUMBER));
            map.put(CommonDefine.CAR_OIL_NUMBER, data.getStringExtra(CommonDefine.CAR_OIL_NUMBER));
            map.put(CommonDefine.CAR_BRAND, data.getStringExtra(CommonDefine.CAR_BRAND));
            map.put(CommonDefine.CAR_MODEL, data.getStringExtra(CommonDefine.CAR_MODEL));
            map.put(CommonDefine.CAR_COLOR, data.getStringExtra(CommonDefine.CAR_COLOR));
            mMyAdapter.setNotShowLoading();
            mMyAdapter.getList().addFirst(map);
            mMyAdapter.notifyDataSetChanged();
            mTextTotalCars.setText(getString(R.string.total_cars) + (carListSize + 1));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
