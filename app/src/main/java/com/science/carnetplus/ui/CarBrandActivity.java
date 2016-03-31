package com.science.carnetplus.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.science.carnetplus.R;
import com.science.carnetplus.adapter.baseAdapter.BaseAdapter;
import com.science.carnetplus.adapter.baseAdapter.ViewHolder;
import com.science.carnetplus.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description 汽车品牌
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/1
 */

public class CarBrandActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private CarBrandAdapter mCarBrandAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_car_brand);
        setToolbar(getString(R.string.cars_brand));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mCarBrandAdapter = new CarBrandAdapter(this, mRecyclerView, getCarOilNum());
        mRecyclerView.setAdapter(mCarBrandAdapter);
    }

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

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mCarBrandAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showMessage(CarBrandActivity.this, "position:" + position);
            }
        });
    }

    class CarBrandAdapter extends BaseAdapter<List<String>> {

        private CircularArray<List<String>> mArray;

        public CarBrandAdapter(Context context, RecyclerView recyclerView, CircularArray<List<String>> ts) {
            super(context, recyclerView, ts);
            mArray = ts;
        }

        @Override
        public int onCreateNormalViewLayoutID(int viewType) {
            return R.layout.item_bottom_dialog_car;
        }

        @Override
        public void onBindNormalViewHolder(ViewHolder holder, int position) {
            List<String> list = mArray.get(position);
            holder.setTextView(R.id.text_car, list.get(position));
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
