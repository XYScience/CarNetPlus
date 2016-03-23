package com.science.carnetplus.widget.wheelview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.science.carnetplus.R;

import java.util.ArrayList;

/**
 * container 3 wheelView implement timePicker
 * Created by JiangPing on 2015/6/17.
 */
public class TimePicker extends LinearLayout {

    private String year, month;
    private WheelView mWheelYear;
    private WheelView mWheelMonth;
    //private WheelView mWheelDay;

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.picker_time, this);
        mWheelYear = (WheelView) findViewById(R.id.year);
        mWheelMonth = (WheelView) findViewById(R.id.month);
        //mWheelDay = (WheelView) findViewById(R.id.day);

        mWheelYear.setData(getYearData());
        mWheelMonth.setData(getMonthData());
        //mWheelDay.setData(getDayData());

        year = mWheelYear.getSelectedText();
        month = mWheelMonth.getSelectedText();
        initListener();
    }

    public void initListener() {
        mWheelYear.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String selectYear = mWheelYear.getSelectedText();
                if (selectYear == null || selectYear.equals("")) {
                    return;
                } else {
                    year = selectYear;
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mWheelMonth.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                String selectMonth = mWheelMonth.getSelectedText();
                if (selectMonth == null || selectMonth.equals("")) {
                    return;
                } else {
                    month = selectMonth;
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }

    public String getTimePicker() {
        return year + "-" + month;
    }

    private ArrayList<String> getYearData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 2016; i > 1919; i--) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getDayData() {
        //ignore condition
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }
}
