package com.science.carnetplus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.science.carnetplus.R;
import com.science.carnetplus.widget.OnSingleClickListener;
import com.science.carnetplus.widget.playerview.CircleImageRotateView;
import com.science.carnetplus.widget.playerview.PlayPauseView;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/20
 */

public class MusicFragment extends Fragment {

    private View mRootView;
    private CircleImageRotateView mCircleImageRotateView;
    private PlayPauseView mPlayPauseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_music, container, false);
        initVeiw();
        initListener();
        return mRootView;
    }

    private void initVeiw() {
        //圆形图片旋转
        mCircleImageRotateView = (CircleImageRotateView) mRootView.findViewById(R.id.mpv);
        //开关控制按钮（继承fab）
        mPlayPauseView = (PlayPauseView) mRootView.findViewById(R.id.playPauseView);
        mPlayPauseView.initCircleImageRotateView(mCircleImageRotateView);
        mCircleImageRotateView.setMax(100);
    }

    private void initListener() {

        mPlayPauseView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (mCircleImageRotateView.isRotating()) {
                    mPlayPauseView.playPauseStop();
                } else {
                    mPlayPauseView.playPauseStart();
                }
            }
        });
    }

}
