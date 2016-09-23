package me.xdj.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by xdj on 16/1/23.
 * ListView 上拉加载
 */
public class ListViewRefreshLayout extends SwipeRefreshLayout {
    private Context mContext;
    private LayoutInflater mInflater;

    private ListView mList;
    private OnLoadListener mLoadListener;
    private boolean mLoading;

    private LoadingState mState = LoadingState.NORMAL;
    private View mLoadingView;
    private View mEmptyView;
    private View mFooterView;

    public ListViewRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);

        // Custom attribute
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListViewRefreshLayout);

        // Preview
        if (isInEditMode()) {
            //setBackgroundResource(android.R.color.holo_red_light);
        }
    }

    public ListViewRefreshLayout(Context context) {
        super(context);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO 解决ListView和SwipeRefreshLayout的冲突
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (canLoad() && !mLoading && mState != LoadingState.EMPTY) {
                    Log.d("onLoad", "dispatchTouchEvent");
                    mLoading = true;
                    mLoadListener.onLoad();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setChildView(ListView listView) {
        this.mList = listView;
        this.mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE &&
                        canLoad() && !mLoading && mState != LoadingState.EMPTY) {
                    Log.d("onLoad", "onScrollStateChanged");
                    mLoading = true;
                    mLoadListener.onLoad();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        setLoading();
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mLoadListener = listener;
    }

    /**
     * 设置当前加载状态
     * @param loading true 正在加载; false 取消或完成加载
     */
    public void setLoading(boolean loading) {
        this.mLoading = loading;

        if (this.mLoading) {
            setLoading();
        }
    }

    public boolean getLoading() {
        return mLoading;
    }

    private boolean canLoad() {
        if (mList == null) {
            //View view = getChildAt(1);
            //throw new NullPointerException("mList is null, please call setChildView(ListView listView)");
        }
        if (mState == LoadingState.EMPTY) {
            return false;
        }
        // 最后一个View显示后则进行加载
        if (mList.getAdapter().getCount() > 0 &&
                mList.getLastVisiblePosition() == mList.getAdapter().getCount() - 1) {
            return true;
        }

        return false;
    }

    public void setLoading() {
        if (mLoadingView == null) {
            mLoadingView = mInflater.inflate(R.layout.view_loading_refresh, null, false);
        }
        if (mState != LoadingState.LOADING) {

            if (mList.getFooterViewsCount() > 0) {
                mList.removeFooterView(mFooterView);
            }
            mList.addFooterView(mLoadingView, null, false);
            mState = LoadingState.LOADING;
            mFooterView = mLoadingView;
            mLoading = true;
        }
    }

    public void setEmpty() {
        if (mEmptyView == null) {
            mEmptyView = mInflater.inflate(R.layout.view_empty_refresh, null, false);
        }
        if (mState != LoadingState.EMPTY) {
            if (mList.getFooterViewsCount() > 0) {
                mList.removeFooterView(mFooterView);
            }
            mList.addFooterView(mEmptyView, null, false);
            mState = LoadingState.EMPTY;
            mFooterView = mEmptyView;
        }
    }

    public void setFail() {
    }

    /**
     * 实现上拉加载
     */
    public interface OnLoadListener {
        void onLoad();
    }
}
