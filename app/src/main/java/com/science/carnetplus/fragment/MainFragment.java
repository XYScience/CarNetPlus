package com.science.carnetplus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.science.carnetplus.R;
import com.science.carnetplus.util.CommonUtils;
import com.science.carnetplus.util.MyLogger;
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
    private FloatingActionButton mFABToolbarButton, mFabLocation;
    private TextView mTextAddOil, mTextCar4s, mTextCarWash, mTextCarPark;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    //最新一次的经纬度
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    private boolean isFirstLocation = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        initLocation();
        initListener();
        return mRootView;
    }

    private void initView() {
        mFABToolbarLayout = (FABToolbarLayout) mRootView.findViewById(R.id.fabtoolbar);
        mFABToolbarButton = (FloatingActionButton) mRootView.findViewById(R.id.fabtoolbar_fab);
        mFabLocation = (FloatingActionButton) mRootView.findViewById(R.id.fab_location);
        mTextAddOil = (TextView) mRootView.findViewById(R.id.text_add_oil);
        mTextCar4s = (TextView) mRootView.findViewById(R.id.text_car_4s);
        mTextCarWash = (TextView) mRootView.findViewById(R.id.text_car_wash);
        mTextCarPark = (TextView) mRootView.findViewById(R.id.text_car_park);
        CommonUtils.materialRipple(mTextAddOil, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCar4s, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCarWash, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCarPark, "#ffffff", 0.3f);

        // 初始化地图
        mMapView = (MapView) mRootView.findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(13.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setMyLocationEnabled(true); //开启图层定位 这段代码非常重要
        hideDefaultView();
    }

    /**
     * 隐藏默认控件
     */
    public void hideDefaultView() {
        // 隐藏百度logo
        View child1 = mMapView.getChildAt(1);
        if (child1 != null
                && (child1 instanceof ImageView || child1 instanceof ZoomControls)) {
            child1.setVisibility(View.INVISIBLE);
        }
        // 隐藏缩放控件
        mMapView.showZoomControls(false);
        // 隐藏比例尺
        mMapView.showScaleControl(false);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        option.setScanSpan(CommonDefine.LOCATION_SPAN_TIME);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于4000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        //option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        //option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        //option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mMapView == null)
                return;
            MyLogger.e(bdLocation.getAddrStr() + ":" + bdLocation.getLocationDescribe());
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            mCurrentLantitude = bdLocation.getLatitude();
            mCurrentLongitude = bdLocation.getLongitude();
            if (isFirstLocation) {
                refreshLocation();
                isFirstLocation = false;
            }
        }

    }

    private void initListener() {
        mFabLocation.setOnClickListener(this);
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
            case R.id.fab_location:
                refreshLocation();
                break;
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

    /**
     * 手动刷新地址，或者主页定位界面回到可见前台自动刷新
     */
    private void refreshLocation() {
        // 请求locationclient.requestLocation()函数，会主动触发定位SDK内部定位逻辑，等待定位回调即可
        mLocationClient.requestLocation();
        // 定位到我的位置
        LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(u);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        refreshLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }
}
