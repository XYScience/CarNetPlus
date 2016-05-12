package com.science.carnetplus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.science.carnetplus.R;
import com.science.carnetplus.adapter.baseAdapter.BaseAdapter;
import com.science.carnetplus.adapter.baseAdapter.ViewHolder;
import com.science.carnetplus.bean.MusicInfo;
import com.science.carnetplus.util.CommonUtils;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/5/11
 */
public class MusicListAdapter extends BaseAdapter<MusicInfo> {

    public MusicListAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public int onCreateNormalViewLayoutID(int viewType) {
        return R.layout.item_music_info;
    }

    @Override
    public void onBindNormalViewHolder(ViewHolder holder, int position) {
        MusicInfo musicInfos = getList().get(position);
        holder.setTextView(R.id.text_title, musicInfos.getTitle());
        holder.setTextView(R.id.text_artist, musicInfos.getArtist());
        holder.setTextView(R.id.text_duration, CommonUtils.Format(musicInfos.getDuration()));
    }
}
