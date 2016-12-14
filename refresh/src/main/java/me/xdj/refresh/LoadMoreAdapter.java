package me.xdj.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by xdj on 2016/12/14.
 */

public abstract class LoadMoreAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private LayoutInflater mInflater;
    private ViewGroup mVHParent;

    private List<T> mData;
    private int mLimit;
    /**
     * 是否允许加载更多
     * 当没有更多时需要设置
     */
    private boolean mEnableLoadMore;
    /**
     * 是否正在加载
     */
    private boolean mLoading;
    private LoadMoreListener mLoadMoreListener;

    public LoadMoreAdapter(List<T> data, int limit) {
        mData = data;
        mLimit = limit;
        if (mLimit <= 0) {
            throw new RuntimeException("limit must > 0");
        }

        mEnableLoadMore = true;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (mVHParent == null) {
            mVHParent = parent;
        }
        return onCreateVH(mInflater, parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindVH(holder, position);

        if (mLoadMoreListener != null
                && mEnableLoadMore
                && !mLoading
                && isLastVH(position)) {
            int lastPage = (getItemCount() - 1) / mLimit;
            int nextPage = lastPage + 1;
            mLoadMoreListener.onLoadMore(nextPage);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public T getItem(int position) {
        if (mData == null) {
            return null;
        }
        return mData.get(position);
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public boolean isEnableLoadMore() {
        return mEnableLoadMore;
    }

    public void setEnableLoadMore(boolean enableLoadMore) {
        mEnableLoadMore = enableLoadMore;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public void addItem(List<T> data) {
        final int startPosition = getItemCount() - 1;
        mData.addAll(data);

        mVHParent.post(new Runnable() {
            @Override
            public void run() {
                // notify 无法在布局或滚动时调用
                notifyItemRangeChanged(startPosition, mLimit);
            }
        });

    }

    public void clearItem() {
        mData.clear();
    }

    /**
     * 是否是最后一个ViewHolder
     * @param position
     * @return
     */
    private boolean isLastVH(int position) {
        return position == getItemCount() - 1;
    }

    public abstract VH onCreateVH(LayoutInflater inflater, ViewParent parent, int viewType);

    public abstract void onBindVH(VH holder, int position);

    public interface LoadMoreListener {
        /**
         * 加载更多
         * @param page 从0开始计数
         */
        void onLoadMore(int page);
    }
}
