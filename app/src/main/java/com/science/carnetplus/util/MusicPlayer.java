package com.science.carnetplus.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.science.carnetplus.bean.MusicInfo;
import com.science.carnetplus.service.MusicService;

import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/5/8
 */
public class MusicPlayer {

    private static volatile MusicPlayer sMusicPlayer = null;
    private Context mContext;
    private MusicService mMusicService;
    public onCompletionListener mOnCompletionListener = null;

    public interface onCompletionListener {
        void onCompletion(MediaPlayer mp);
    }

    public void setOnCompletionListener(onCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    private MusicPlayer(Context context) {
        mContext = context.getApplicationContext();
    }

    public static MusicPlayer getMusicPlayer(Context context) {
        if (sMusicPlayer == null) {
            synchronized (MusicPlayer.class) {
                if (sMusicPlayer == null) {
                    sMusicPlayer = new MusicPlayer(context);
                }
            }
        }
        return sMusicPlayer;
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicService = ((MusicService.MusicBinder) service).getMusicService();
            mMusicService.onStartMusic();
            mMusicService.setOnCompletionListener(new MusicService.onCompletionListener() {
                @Override
                public void onCompletion(final MediaPlayer mp) {
                    if (mOnCompletionListener != null) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mOnCompletionListener.onCompletion(mp);
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
        }
    };

    public void startMusicService() {
        if (mMusicService == null) {
            Intent intent = new Intent("com.science.carnetplus.MUSIC_ACTION");
            mContext.bindService(intent, mServiceConnection, mContext.BIND_AUTO_CREATE);
        }
    }

    public void nextMusic() {
        if (mMusicService != null) {
            mMusicService.nextMusic();
        }
    }

    public void lastMusic() {
        if (mMusicService != null) {
            mMusicService.lastMusic();
        }
    }

    public void playMusic() {
        if (mMusicService != null) {
            mMusicService.playMusic();
        }
    }

    public void pauseMusic() {
        if (mMusicService != null) {
            mMusicService.pauseMusic();
        }
    }

    public int getMusicDuration() {
        if (mMusicService != null) {
            return mMusicService.getMusicDuration();
        } else {
            return 0;
        }
    }

    public int getCurrentPosition() {
        if (mMusicService != null) {
            return mMusicService.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public boolean getIsPlaying() {
        return mMusicService.getIsPlaying();
    }

    public String getCurrentTitle() {
        return mMusicService.getCurrentTitle();
    }

    public String getCurrentArtist() {
        return mMusicService.getCurrentArtist();
    }

    public void onUnbind() {
        if (mMusicService != null) {
            mContext.unbindService(mServiceConnection);
        }
    }

    public List<MusicInfo> getMusic() {
        return mMusicService.getMusic();
    }
}
