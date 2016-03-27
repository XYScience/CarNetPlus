package com.science.carnetplus.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.adapter.baseAdapter.BaseAdapter;
import com.science.carnetplus.adapter.baseAdapter.ViewHolder;
import com.science.carnetplus.util.SnackbarUtils;

import java.util.HashMap;
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
    private CircularArray<Map<String, String>> mMapList;
    private MyAdapter mMyAdapter;
    private TextView mTextTotalCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_my_cars_info);
        setToolbar(getString(R.string.my_cars));

        mTextTotalCars = (TextView) findViewById(R.id.total_cars);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MyCarsInfoActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mMyAdapter = new MyAdapter(MyCarsInfoActivity.this, mRecyclerView, getList());
        mRecyclerView.setAdapter(mMyAdapter);
    }

    private CircularArray<Map<String, String>> getList() {
        mMapList = new CircularArray<Map<String, String>>();
        for (int i = 0; i < 4; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("car_number", "粤A12345");
            map.put("car_oil_number", "90#");
            map.put("car_brand", "法拉第");
            map.put("car_model", "小轿车");
            map.put("car_color", "黑色");
            mMapList.addLast(map);
        }
        return mMapList;
    }

    @Override
    public void initData() {
        mTextTotalCars.setText("共有车辆：" + mMapList.size());
    }

    @Override
    public void initListener() {
        mMyAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SnackbarUtils.showSnackbar(view, "click:" + (position + 1));
            }
        });

        mMyAdapter.setOnLoadingListener(new BaseAdapter.OnLoadingListener() {
            @Override
            public void loading() {
                new LoadAsyncTask().execute();
            }
        });
    }

    private class LoadAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mMyAdapter.setLoadingComplete();
            int size = mMapList.size();
            for (int i = size + 1; i < size + 3; i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("car_number", "粤A12345");
                map.put("car_oil_number", "90#");
                map.put("car_brand", "法拉第");
                map.put("car_model", "小轿车");
                map.put("car_color", "黑色");
                mMapList.addLast(map);
            }
            mMyAdapter.notifyItemRangeInserted(mMapList.size() - 2, 2);
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

    private class MyAdapter extends BaseAdapter<Map<String, String>> {

        private CircularArray<Map<String, String>> list;

        public MyAdapter(Context context, RecyclerView recyclerView, CircularArray<Map<String, String>> ts) {
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
            holder.setTextView(R.id.car_number, item.get("car_number"));
            holder.setTextView(R.id.car_oil_number, item.get("car_oil_number"));
            holder.setTextView(R.id.car_brand, item.get("car_brand"));
            holder.setTextView(R.id.car_model, item.get("car_model"));
            holder.setTextView(R.id.car_color, item.get("car_color"));
        }
    }
}
