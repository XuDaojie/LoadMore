package me.xdj.refresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by xdj on 16/2/3.
 */
public class MultiStateView extends FrameLayout {

    public static final int CONTENT = 10001;
    public static final int LOADING = 10002;
    public static final int EMPTY = 10003;
    public static final int FAIL = 10004;

    private View mEmptyView;
    private View mContentView;
    private View mFailView;
    private View mLoadingView;

    private int mViewState = CONTENT;
    /**
     * 预览时状态
     */
    private int mPreviewState = CONTENT;

    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultiStateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView);
        int initView = typedArray.getInt(R.styleable.MultiStateView_initView, LOADING);
        int rIdEmpty = typedArray.getResourceId(R.styleable.MultiStateView_emptyView, -1);
        int rIdLoading = typedArray.getResourceId(R.styleable.MultiStateView_loadingView, -1);
        int rIdFail = typedArray.getResourceId(R.styleable.MultiStateView_failView, -1);

        mPreviewState = typedArray.getInt(R.styleable.MultiStateView_preview, CONTENT);

        LayoutInflater inflater = LayoutInflater.from(context);

        if (rIdEmpty != -1) {
            mEmptyView = inflater.inflate(rIdEmpty, this, false);
        } else {
            mEmptyView = inflater.inflate(R.layout.view_empty, this, false);
        }
        if (rIdFail != -1) {
            mFailView = inflater.inflate(rIdFail, this, false);
        } else {
            mFailView = inflater.inflate(R.layout.view_fail, this, false);
        }
        if (rIdLoading != -1) {
            mLoadingView = inflater.inflate(rIdLoading, this, false);
        } else {
            mLoadingView = inflater.inflate(R.layout.view_loading, this, false);
        }
        addView(mEmptyView, mEmptyView.getLayoutParams());
        addView(mFailView, mFailView.getLayoutParams());
        addView(mLoadingView, mLoadingView.getLayoutParams());

        typedArray.recycle();

        if (!isInEditMode()) {
            setViewState(initView);
        } else {
            setViewState(mPreviewState);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setViewState(mPreviewState);
    }

    @Override
    public void addView(View child) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) mContentView = child;
        super.addView(child, index, params);
    }

    /**
     * 设视图状态
     * @param state
     */
    public void setViewState(int state) {
        switch (state) {
            case CONTENT:
                if(mContentView != null) mContentView.setVisibility(VISIBLE);
                mLoadingView.setVisibility(GONE);
                mFailView.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                mViewState = CONTENT;
                break;
            case LOADING:
                if(mContentView != null) mContentView.setVisibility(GONE);
                mLoadingView.setVisibility(VISIBLE);
                mFailView.setVisibility(GONE);
                mEmptyView.setVisibility(GONE);
                mViewState = LOADING;
                break;
            case EMPTY:
                if(mContentView != null) mContentView.setVisibility(GONE);
                mLoadingView.setVisibility(GONE);
                mFailView.setVisibility(GONE);
                mEmptyView.setVisibility(VISIBLE);
                mViewState = EMPTY;
                break;
            case FAIL:
                if(mContentView != null) mContentView.setVisibility(GONE);
                mLoadingView.setVisibility(GONE);
                mFailView.setVisibility(VISIBLE);
                mEmptyView.setVisibility(GONE);
                mViewState = FAIL;
                break;
            default:
                setViewState(CONTENT);
                break;
        }
    }

    /**
     *
     */
    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }
        return view != mLoadingView && view != mFailView && view != mEmptyView;
    }
}
