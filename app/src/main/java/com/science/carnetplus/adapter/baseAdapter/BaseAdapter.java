package com.science.carnetplus.adapter.baseAdapter;

import android.content.Context;
import android.support.v4.util.CircularArray;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.widget.materialProgress.LoadingView;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/27
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.RVHolder> {

    private static final String TAG = "BaseAdapter";

    private Context mContext;
    //是否正在加载
    public boolean mIsLoading = false;
    //正常条目
    private static final int TYPE_NORMAL_ITEM = 0;
    //加载条目
    private static final int TYPE_LOADING_ITEM = 1;
    //加载viewHolder
    private LoadingViewHolder mLoadingViewHolder;
    //数据集
    private CircularArray<T> mTs;
    //首次进入
    private boolean mFirstEnter = true;
    private RecyclerView mRecyclerView;

    public BaseAdapter(Context context, RecyclerView recyclerView, CircularArray<T> ts) {
        mContext = context;
        mTs = ts;
        mRecyclerView = recyclerView;
        //notifyLoading();
    }

    private OnLoadingListener mOnLoadingListener;

    /**
     * 加载更多接口
     */
    public interface OnLoadingListener {
        void loading();
    }

    /**
     * 设置监听接口
     *
     * @param onLoadingListener onLoadingListener
     */
    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        setScrollListener(mRecyclerView);
        mOnLoadingListener = onLoadingListener;
    }

    /**
     * 加载完成
     */
    public void setLoadingComplete() {
        if (mTs.size() > 0 && mTs.getLast() == null) {
            mIsLoading = false;
            mTs.removeFromEnd(1);
            notifyItemRemoved(mTs.size() - 1);
            if (mLoadingViewHolder != null) {
                mLoadingViewHolder.loadingProgress.stopAnimation();
            }
        }
    }

    /**
     * 没有更多数据
     */
    public void setLoadingNoMore() {
        mIsLoading = false;
        if (mLoadingViewHolder != null) {
            mLoadingViewHolder.loadingProgress.setVisibility(View.GONE);
            mLoadingViewHolder.loadingProgress.stopAnimation();
            mLoadingViewHolder.tvLoading.setText(mContext.getString(R.string.load_complete));
        }
    }

    /**
     * 加载失败
     */
    public void setLoadingError() {
        if (mLoadingViewHolder != null) {
            mIsLoading = false;
            mLoadingViewHolder.loadingProgress.setVisibility(View.GONE);
            mLoadingViewHolder.loadingProgress.stopAnimation();
            mLoadingViewHolder.tvLoading.setText(mContext.getString(R.string.load_error_click_again));

            mLoadingViewHolder.tvLoading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadingListener != null) {
                        mIsLoading = true;
                        mLoadingViewHolder.loadingProgress.setVisibility(View.VISIBLE);
                        mLoadingViewHolder.loadingProgress.startAnimation();
                        mLoadingViewHolder.tvLoading.setText(mContext.getString(R.string.loading));

                        mOnLoadingListener.loading();
                    }
                }
            });
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    private boolean canScrollDown(RecyclerView recyclerView) {
        return ViewCompat.canScrollVertically(recyclerView, 1);
    }

    /**
     * 显示加载
     */
    private void notifyLoading() {
        if (mTs.size() != 0 && mTs.getLast() != null) {
            mTs.addLast(null);
            notifyItemInserted(mTs.size() - 1);
        }
    }

    /**
     * 监听滚动事件
     *
     * @param recyclerView recycleView
     */
    private void setScrollListener(RecyclerView recyclerView) {
        if (recyclerView == null) {
            Log.e(TAG, ">>>>>>>>>>>>>recycleView 为空");
            return;
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                if (!canScrollDown(recyclerView)) {

                    //首次进入不加载
                    if (!mIsLoading && !mFirstEnter) {

                        notifyLoading();

                        mIsLoading = true;

                        if (mLoadingViewHolder != null) {
                            mLoadingViewHolder.loadingProgress.setVisibility(View.VISIBLE);
                            mLoadingViewHolder.loadingProgress.startAnimation();
                            mLoadingViewHolder.tvLoading.setText(mContext.getString(R.string.loading));
                        }

                        if (mOnLoadingListener != null) {
                            mOnLoadingListener.loading();
                        }
                    }
//                }

                if (mFirstEnter) {
                    mFirstEnter = false;
                }
            }
        });
    }

    /**
     * 创建 normal layout
     */
    public abstract int onCreateNormalViewLayoutID(int viewType);

    /**
     * 绑定viewHolder
     *
     * @param holder   viewHolder
     * @param position position
     */
    public abstract void onBindNormalViewHolder(ViewHolder holder, int position);


    /**
     * 加载布局
     */
    private class LoadingViewHolder extends RVHolder {
        public LoadingView loadingProgress;
        public TextView tvLoading;

        public LoadingViewHolder(View view) {
            super(view);
            loadingProgress = (LoadingView) view.findViewById(R.id.loading_progress);
            tvLoading = (TextView) view.findViewById(R.id.text_loading);
        }
    }

    @Override
    public void onViewRecycled(final RVHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemViewType(int position) {
        T t = mTs.get(position);
        if (t == null) {
            return TYPE_LOADING_ITEM;
        } else {
            return TYPE_NORMAL_ITEM;
        }
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(onCreateNormalViewLayoutID(viewType), parent, false);
            return new RVHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_layout, parent, false);
            mLoadingViewHolder = new LoadingViewHolder(view);
            return mLoadingViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_NORMAL_ITEM) {
            onBindNormalViewHolder(holder.getViewHolder(), position);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                    }
                });
            }
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return mTs.size();
    }

    private AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public static class RVHolder extends RecyclerView.ViewHolder {

        private ViewHolder viewHolder;

        public RVHolder(View itemView) {
            super(itemView);
            viewHolder = ViewHolder.getViewHolder(itemView);
        }

        public ViewHolder getViewHolder() {
            return viewHolder;
        }

    }
}
