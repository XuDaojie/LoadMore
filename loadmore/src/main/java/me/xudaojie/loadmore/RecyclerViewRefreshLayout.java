package me.xudaojie.loadmore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by xdj on 16/3/3.
 * RecyclerView上拉加载
 */
public class RecyclerViewRefreshLayout extends SwipeRefreshLayout {
    private static final String TAG = "RecyclerViewRefreshLayout";

    private RecyclerView mRecyclerView;
    private HeaderViewRecyclerAdapter mHeaderAdapter;
    private OnLoadListener mOnLoadListener;
    private boolean mLoading;

    public RecyclerViewRefreshLayout(Context context) {
        super(context);
    }

    public RecyclerViewRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    public void setChildView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        this.mHeaderAdapter = new HeaderViewRecyclerAdapter(recyclerView.getAdapter());

        this.mRecyclerView.setAdapter(mHeaderAdapter);
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                int itemCount = layoutManager.getItemCount();

                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastVisiblePosition == itemCount - 1 &&
                        canLoad()) {
                    if (mOnLoadListener != null && !mLoading) {
                        mLoading = true;
                        mOnLoadListener.onLoad();
                        Log.d(TAG, "onLoad");
                    }
                }

            }
        });
    }

    private boolean canLoad() {
        if (mLoading) {
            return false;
        }
        return true;
    }

    public interface OnLoadListener {
        void onLoad();
    }

}