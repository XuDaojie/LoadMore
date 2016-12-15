package me.xudaojie.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by xdj on 2016/12/14.
 */

public abstract class LoadMoreAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_LOAD_MORE = -30301;

    private LayoutInflater mInflater;
    private ViewGroup mVHParent;
    /**
     * 加载更多（loading、noMore、fail）
     */
    private View mLoadMoreView;
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
        if (limit <= 0) {
            throw new RuntimeException("limit must > 0");
        }

        mLimit = limit;
        mData = data;
        mEnableLoadMore = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            return new DefaultViewHolder(mLoadMoreView);
        }

        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (mVHParent == null) {
            mVHParent = parent;
        }
        return onCreateVH(mInflater, parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != VIEW_TYPE_LOAD_MORE) {
            onBindVH((VH) holder, position);
        }

        if (mLoadMoreListener != null
                && mEnableLoadMore
                && !mLoading
                && isLastVH(position)) {

            int itemCount = getItemCount();
            int lastPage = (itemCount - 1) / mLimit;
            if (mLoadMoreView != null) {
                lastPage = (itemCount - 2) / mLimit;
            }
            int nextPage = lastPage + 1;
            mLoading = true;
            mLoadMoreListener.onLoadMore(nextPage);
        }
    }

    @Override
    public int getItemCount() {
        int dataCount = mData == null ? 0 : mData.size();
        if (mLoadMoreView == null) {
            return dataCount;
        }
        return dataCount + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mLoadMoreView != null
                && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    public T getItem(int position) {
        if (mData == null) {
            return null;
        }
        return mData.get(position);
    }

    public void setLoadMoreView(View view) {
        mLoadMoreView = view;
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
        notifyDataSetChanged();
    }

    public View getLoadMoreView() {
        return mLoadMoreView;
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

    /**
     * 无更多数据
     */
    public abstract void dataNoMore();

    /**
     * 设置foot显示loading
     */
    public abstract void dataLoading();

    /**
     * 数据加载失败
     */
    public abstract void dataLoadFail();

    public interface LoadMoreListener {
        /**
         * 加载更多
         * @param page 从0开始计数
         */
        void onLoadMore(int page);
    }

    private static class DefaultViewHolder extends RecyclerView.ViewHolder {
        DefaultViewHolder(View itemView) {
            super(itemView);
        }
    }
}
