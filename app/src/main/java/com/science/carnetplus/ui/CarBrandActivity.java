package com.science.carnetplus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.CircularArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.science.carnetplus.R;
import com.science.carnetplus.bean.CarBrand;
import com.science.carnetplus.util.CommonDefine;
import com.science.carnetplus.widget.sortListView.CharacterParser;
import com.science.carnetplus.widget.sortListView.PinyinComparator;
import com.science.carnetplus.widget.sortListView.SideBar;
import com.science.carnetplus.widget.sortListView.SortAdapter;
import com.science.carnetplus.widget.sortListView.SortModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description 汽车品牌
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/1
 */

public class CarBrandActivity extends BaseActivity {

    private ListView mListView;
    private SideBar mSideBar;
    private TextView dialog;
    private SortAdapter mSortAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> mSourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_car_brand);
        setToolbar(getString(R.string.cars_brand));

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        mSideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        mSideBar.setTextView(dialog);

        mListView = (ListView) findViewById(R.id.list_brand);
        mSourceDateList = filledData(getResources().getStringArray(R.array.brand));
        // 根据a-z进行排序源数据
        Collections.sort(mSourceDateList, pinyinComparator);
        mSortAdapter = new SortAdapter(this, mSourceDateList);
        mListView.setAdapter(mSortAdapter);
    }

    /**
     * 为ListView填充数据
     *
     * @param brand
     * @return
     */
    private List<SortModel> filledData(String[] brand) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < brand.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(brand[i]);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(brand[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 获取品牌列表
     *
     * @return
     */
    private CircularArray<List<String>> getCarBrand() {
        CircularArray<List<String>> array = new CircularArray<>();
//        List<CarBrand> carBrandList = GsonTools.gsonToList(getStrFromAssets("car_brand.json"), CarBrand.class);
        Type listType = new TypeToken<ArrayList<CarBrand>>() {}.getType();
        ArrayList<CarBrand> carBrandList = new Gson().fromJson(getStrFromAssets("car_brand.json"), listType);
        List<String> list = new ArrayList<>();
        for (CarBrand carBrand : carBrandList) {
            list.add(carBrand.getB_name());
            array.addLast(list);
        }
        return array;
    }

    /**
     * @return Json数据（String）
     * @description 通过assets文件获取json数据，这里写的十分简单，没做循环判断。
     */
    private String getStrFromAssets(String name) {
        String strData = null;
        try {
            InputStream inputStream = getAssets().open(name);
            byte buf[] = new byte[inputStream.available()];
            inputStream.read(buf);
            strData = new String(buf, "utf-8");
            strData = strData.trim();
            inputStream.close();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

        return strData;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        //设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mSortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Intent intent = new Intent();
                intent.putExtra(CommonDefine.CAR_BRAND, ((SortModel) mSortAdapter.getItem(position)).getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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
