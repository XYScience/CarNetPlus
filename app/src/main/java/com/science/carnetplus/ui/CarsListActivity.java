package com.science.carnetplus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.science.carnetplus.R;
import com.science.carnetplus.adapter.CarListAdapter;
import com.science.carnetplus.util.AVOSUtils;
import com.science.carnetplus.util.BottomSheetBehaviorUtils;
import com.science.carnetplus.util.CommonDefine;

import java.util.ArrayList;
import java.util.Collections;
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

public class CarsListActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mFabAddCar;
    private CarListAdapter mMyAdapter;
    private TextView mTextTotalCars;
    private int carListSize;
    private AVOSUtils mAVOSUtils;
    private CoordinatorLayout mRootLayout;
    private View mDarkenLayout;
    private BottomSheetBehavior mSheetBehavior;
    private View mBottomSheet;
    private TextView mTextBottomSheetTitle, mTextSetCarDefault, mTextModify, mTextDelete, mTextCancel;
    private int mPosition;

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
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CarsListActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mMyAdapter = new CarListAdapter(CarsListActivity.this);
        mRecyclerView.setAdapter(mMyAdapter);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_blue,
                R.color.swipe_red,
                R.color.swipe_green,
                R.color.swipe_yellow);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        mRootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        mDarkenLayout = findViewById(R.id.darken_layout);
        mBottomSheet = findViewById(R.id.design_bottom_sheet);
        mSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mTextBottomSheetTitle = (TextView) findViewById(R.id.bottom_sheet_title);
        mTextSetCarDefault = (TextView) findViewById(R.id.text_set_car_default);
        mTextModify = (TextView) findViewById(R.id.text_modify);
        mTextDelete = (TextView) findViewById(R.id.text_delete);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
        mAVOSUtils = AVOSUtils.getInstance();
    }

    @Override
    public void initData() {
        getCarList();
    }

    private void getCarList() {
        mAVOSUtils.getCarList(AVUser.getCurrentUser().getUsername(), new AVOSUtils.OnAVOSCallback() {
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

        List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
        if (list != null && list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(CommonDefine.CAR_NUMBER, list.get(i).getString(CommonDefine.CAR_NUMBER));
                map.put(CommonDefine.CAR_OIL_NUMBER, list.get(i).getString(CommonDefine.CAR_OIL_NUMBER));
                map.put(CommonDefine.CAR_BRAND, list.get(i).getString(CommonDefine.CAR_BRAND));
                map.put(CommonDefine.CAR_TYPE, list.get(i).getString(CommonDefine.CAR_TYPE));
                map.put(CommonDefine.CAR_COLOR, list.get(i).getString(CommonDefine.CAR_COLOR));
                map.put(CommonDefine.CAR_DEFAULT, list.get(i).getString(CommonDefine.CAR_DEFAULT));
                mapList.add(map);
            }
            mMyAdapter.setMapList(mapList);
            mMyAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void initListener() {
        mTextSetCarDefault.setOnClickListener(this);
        mTextModify.setOnClickListener(this);
        mTextDelete.setOnClickListener(this);
        mTextCancel.setOnClickListener(this);

        mMyAdapter.setOnItemClickListener(new CarListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mPosition = position;
                mTextBottomSheetTitle.setText(mMyAdapter.getMapList().get(position).get(CommonDefine.CAR_NUMBER));
                mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        mFabAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarsListActivity.this, AddCarActivity.class);
                intent.putExtra(CommonDefine.INTENT_ACTIVITY, CommonDefine.INTENT_REQUSET);
                startActivityForResult(intent, CommonDefine.INTENT_REQUSET);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCarList();
            }
        });

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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mAVOSUtils.setNoDefaultCar(AVUser.getCurrentUser().getUsername());
            } else if (msg.what == 1) {
                mAVOSUtils.setDefaultCar(AVUser.getCurrentUser().getUsername().toString(),
                        mMyAdapter.getMapList().get(0).get(CommonDefine.CAR_NUMBER), "0");
            } else if (msg.what == 2) {
                getCarList();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_set_car_default:
                setCarDefault();
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.text_modify:
                modifyCar();
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.text_delete:
                deleteCar();
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.text_cancel:
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    /**
     * 设置为默认车型
     */
    private void setCarDefault() {
        int size = mMyAdapter.getMapList().size();
        if (size > 1) {
            for (int i = 0; i < size; i++) {
                if ("0".equals(mMyAdapter.getMapList().get(i).get(CommonDefine.CAR_DEFAULT))) {
                    mMyAdapter.getMapList().get(i).put(CommonDefine.CAR_DEFAULT, "-1");
                }
            }
            mMyAdapter.getMapList().get(mPosition).put(CommonDefine.CAR_DEFAULT, "0");
            Collections.swap(mMyAdapter.getMapList(), mPosition, 0);
            mMyAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(0);

            mHandler.sendEmptyMessageDelayed(0, 0);
            mHandler.sendEmptyMessageDelayed(1, 500);
        }
    }

    /**
     * 删除车辆
     */
    private void deleteCar() {
        String carNumber = mMyAdapter.getMapList().get(mPosition).get(CommonDefine.CAR_NUMBER);
        mMyAdapter.getMapList().remove(mPosition);
        mMyAdapter.notifyDataSetChanged();
        mAVOSUtils.deleteCar(AVUser.getCurrentUser().getUsername().toString(), carNumber);
        mTextTotalCars.setText(getString(R.string.total_cars) + (carListSize - 1));
    }

    /**
     * 修改车辆
     */
    private void modifyCar() {
        Intent intent = new Intent(CarsListActivity.this, AddCarActivity.class);
        intent.putExtra(CommonDefine.CAR_NUMBER, mMyAdapter.getMapList().get(mPosition).get(CommonDefine.CAR_NUMBER));
        intent.putExtra(CommonDefine.CAR_OIL_NUMBER, mMyAdapter.getMapList().get(mPosition).get(CommonDefine.CAR_OIL_NUMBER));
        intent.putExtra(CommonDefine.CAR_BRAND, mMyAdapter.getMapList().get(mPosition).get(CommonDefine.CAR_BRAND));
        intent.putExtra(CommonDefine.CAR_TYPE, mMyAdapter.getMapList().get(mPosition).get(CommonDefine.CAR_TYPE));
        intent.putExtra(CommonDefine.CAR_COLOR, mMyAdapter.getMapList().get(mPosition).get(CommonDefine.CAR_COLOR));
        intent.putExtra(CommonDefine.INTENT_ACTIVITY, CommonDefine.INTENT_REQUSET_2);
        startActivityForResult(intent, CommonDefine.INTENT_REQUSET_2);
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
            map.put(CommonDefine.CAR_TYPE, data.getStringExtra(CommonDefine.CAR_TYPE));
            map.put(CommonDefine.CAR_COLOR, data.getStringExtra(CommonDefine.CAR_COLOR));
            map.put(CommonDefine.CAR_DEFAULT, data.getStringExtra(CommonDefine.CAR_DEFAULT));

            int size = mMyAdapter.getMapList().size();
            if (size > 1) {
                for (int i = 0; i < size; i++) {
                    if ("0".equals(mMyAdapter.getMapList().get(i).get(CommonDefine.CAR_DEFAULT))) {
                        mMyAdapter.getMapList().get(i).put(CommonDefine.CAR_DEFAULT, "-1");
                    }
                }
            }
            mMyAdapter.getMapList().add(0, map);
            mMyAdapter.notifyDataSetChanged();
            mTextTotalCars.setText(getString(R.string.total_cars) + (carListSize + 1));
        } else if (requestCode == CommonDefine.INTENT_REQUSET_2 && resultCode == RESULT_OK) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
            mHandler.sendEmptyMessageDelayed(2, 500);
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
