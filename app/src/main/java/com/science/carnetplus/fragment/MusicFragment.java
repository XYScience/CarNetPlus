package com.science.carnetplus.fragment;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.util.MusicPlayer;
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
    private TextView mTextTitle, mTextArtist;
    private CircleImageRotateView mCircleImageRotateView;
    private PlayPauseView mPlayPauseView;
    private ImageView mImgLast, mImgNext;
    private MusicPlayer mMusicPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_music, container, false);
        initView();
        initListener();
        return mRootView;
    }

    private void initView() {
        mTextTitle = (TextView) mRootView.findViewById(R.id.text_title);
        mTextArtist = (TextView) mRootView.findViewById(R.id.text_artist);
        //圆形图片旋转
        mCircleImageRotateView = (CircleImageRotateView) mRootView.findViewById(R.id.mpv);
        mImgLast = (ImageView) mRootView.findViewById(R.id.last);
        mImgNext = (ImageView) mRootView.findViewById(R.id.next);
        //开关控制按钮（继承fab）
        mPlayPauseView = (PlayPauseView) mRootView.findViewById(R.id.playPauseView);
        mPlayPauseView.initCircleImageRotateView(mCircleImageRotateView);

        mMusicPlayer = MusicPlayer.getMusicPlayer(getActivity());
        changeMusic();
        mCircleImageRotateView.setProgress(mMusicPlayer.getCurrentPosition());
    }

    private void initListener() {

        mMusicPlayer.setOnCompletionListener(new MusicPlayer.onCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                changeMusic();
                mCircleImageRotateView.setProgress(0);
            }
        });

        mImgLast.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mMusicPlayer.lastMusic();
                changeMusic();
                mCircleImageRotateView.setProgress(0);
            }
        });

        mImgNext.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mMusicPlayer.nextMusic();
                changeMusic();
                mCircleImageRotateView.setProgress(0);
            }
        });

        mPlayPauseView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (mMusicPlayer.getIsPlaying()) {
                    mCircleImageRotateView.stop();
                    mMusicPlayer.pauseMusic();
                } else {
                    mCircleImageRotateView.start();
                    mMusicPlayer.playMusic();
                }
            }
        });
    }

    private void changeMusic() {
        mCircleImageRotateView.setMax(mMusicPlayer.getMusicDuration());
        mCircleImageRotateView.start();
        mTextTitle.setText(mMusicPlayer.getCurrentTitle());
        mTextArtist.setText(mMusicPlayer.getCurrentArtist());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMusicPlayer.onUnbind();
    }
}
