package com.science.carnetplus.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.bean.MusicInfo;
import com.science.carnetplus.ui.MusicListActivity;
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

    public static final int INTENT_MUSIC = 0;
    public static final String MUSIC_INFO = "music_info";
    private View mRootView;
    private TextView mTextTitle, mTextArtist;
    private CircleImageRotateView mCircleImageRotateView;
    private PlayPauseView mPlayPauseView;
    private ImageView mImgLast, mImgNext;
    private MusicPlayer mMusicPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_MUSIC && resultCode == getActivity().RESULT_OK) {
            MusicInfo musicInfo = (MusicInfo) data.getSerializableExtra(MUSIC_INFO);
            mMusicPlayer.startMusic(musicInfo.getUrl());
            changeMusic();
            mCircleImageRotateView.setProgress(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMusicPlayer.onUnbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCircleImageRotateView.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCircleImageRotateView.start();
        mCircleImageRotateView.setProgress(mMusicPlayer.getCurrentPosition());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.more, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more) {
            Intent intent = new Intent(getActivity(), MusicListActivity.class);
            startActivityForResult(intent, INTENT_MUSIC);
        }
        return super.onOptionsItemSelected(item);
    }
}
