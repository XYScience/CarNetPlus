package com.science.carnetplus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.util.CircularArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.science.carnetplus.R;
import com.science.carnetplus.adapter.MusicListAdapter;
import com.science.carnetplus.bean.MusicInfo;
import com.science.carnetplus.fragment.MusicFragment;
import com.science.carnetplus.util.MusicPlayer;

import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description 音乐列表
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/5/10
 */
public class MusicListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private MusicListAdapter mMusicListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_music_list);
        setToolbar(getString(R.string.nav_music));

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MusicListActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mMusicListAdapter = new MusicListAdapter(MusicListActivity.this, mRecyclerView);
        mRecyclerView.setAdapter(mMusicListAdapter);
    }

    @Override
    public void initData() {
        List<MusicInfo> musicInfos = MusicPlayer.getMusicPlayer(this).getMusic();
        CircularArray<MusicInfo> circularArray = new CircularArray<>();
        for (MusicInfo musicInfo : musicInfos) {
            circularArray.addLast(musicInfo);
        }
        mMusicListAdapter.setList(circularArray);
    }

    @Override
    public void initListener() {
        mMusicListAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicInfo musicInfo = mMusicListAdapter.getList().get(position);
                Intent intent = new Intent();
                intent.putExtra(MusicFragment.MUSIC_INFO, musicInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
