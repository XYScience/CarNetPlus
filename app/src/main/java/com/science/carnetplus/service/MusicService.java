package com.science.carnetplus.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.science.carnetplus.bean.MusicInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/5/4
 */
public class MusicService extends Service {

    private MediaPlayer mMediaPlayer;
    private List<MusicInfo> mMusicList;
    private int currentListItem = 0;
    public static final int START_MUSIC = 0;
    public static final String MUSIC_SERVICE = "music_service";

    public interface onCompletionListener {
        void onCompletion(MediaPlayer mp);
    }

    public onCompletionListener mOnCompletionListener = null;

    public void setOnCompletionListener(onCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    @Override
    public void onCreate() {
        mMediaPlayer = new MediaPlayer();
        super.onCreate();
    }

    public void onStartMusic() {
        if (mMusicList != null && mMusicList.size() != 0) {
            startMusic(mMusicList.get(0).getUrl());
        } else {
            getMusicList();
        }
    }

    /**
     * 获取音乐列表
     *
     * @return
     */
    public void getMusicList() {
        mMusicList = new ArrayList<>();
        final Cursor cursor = this.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < cursor.getCount(); i++) {
                    MusicInfo musicInfo = new MusicInfo();
                    cursor.moveToNext();
                    long id = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
                    String title = cursor.getString((cursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
                    String artist = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
                    long duration = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
                    long size = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
                    String url = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
                    int isMusic = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
                    if (isMusic != 0) { // 只把音乐添加到集合当中
                        musicInfo.setId(id);
                        musicInfo.setTitle(title);
                        musicInfo.setArtist(artist);
                        musicInfo.setDuration(duration);
                        musicInfo.setSize(size);
                        musicInfo.setUrl(url);
                        mMusicList.add(musicInfo);
                    }
                }
                startMusic(mMusicList.get(0).getUrl());
            }
        }).start();
    }

    /**
     * 播放音乐
     *
     * @param path
     */
    public void startMusic(String path) {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mp);
                    }
                    nextMusic();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下一首
     */
    public void nextMusic() {
        if (mMusicList != null && mMusicList.size() != 0) {
            if (++currentListItem >= mMusicList.size()) {
                currentListItem = 0;
            }
            startMusic(mMusicList.get(currentListItem).getUrl());
        }
    }

    /**
     * 上一首
     */
    public void lastMusic() {
        if (mMusicList != null && mMusicList.size() != 0) {
            if (currentListItem == 0) {
                currentListItem = mMusicList.size() - 1;
                startMusic(mMusicList.get(currentListItem).getUrl());
            } else {
                startMusic(mMusicList.get(--currentListItem).getUrl());
            }
        }
    }

    /**
     * 播放音乐
     */
    public void playMusic() {
        mMediaPlayer.start();
    }

    /**
     * 暂停音乐
     */
    public void pauseMusic() {
        mMediaPlayer.pause();
    }

    /**
     * 得到音乐时长
     *
     * @return
     */
    public int getMusicDuration() {
        return mMediaPlayer.getDuration() / 1000;
    }

    /**
     * 得到音乐当前进度
     *
     * @return
     */
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition() / 1000;
    }

    public boolean getIsPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public String getCurrentTitle() {
        return mMusicList.get(currentListItem).getTitle();
    }

    public String getCurrentArtist() {
        return mMusicList.get(currentListItem).getArtist();
    }

    public List<MusicInfo> getMusic() {
        return mMusicList;
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    public class MusicBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }
}
