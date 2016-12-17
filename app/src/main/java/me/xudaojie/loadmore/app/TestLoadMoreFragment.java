package me.xudaojie.loadmore.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.xudaojie.loadmore.LoadMoreAdapter;

/**
 * Created by xdj on 2016/12/14.
 */

public class TestLoadMoreFragment extends Fragment {
    private static final String TAG = TestLoadMoreFragment.class.getSimpleName();

    private static final int LIMIT = 10;

    private View mRootView;
    private Activity mActivity;
    private Toast mToast;

    public static TestLoadMoreFragment newInstance() {
        return new TestLoadMoreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_test_recycler, container, false);
        final RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler);
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        List<String> data = createItems(0, LIMIT);
        View loadMoreView = inflater.inflate(R.layout.view_load_more, container, false);
        final ListAdapter adapter = new ListAdapter(data, LIMIT, loadMoreView);
        // create loadMoreView
//        adapter.setLoadMoreView(loadMoreView);
        adapter.setLoadMoreListener(new LoadMoreAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore(final int page) {
                Log.w(TAG, "正在加载 page: " + page);
                if (mToast == null) {
                    mToast = Toast.makeText(mActivity, "正在加载 page: " + page, Toast.LENGTH_SHORT);
                } else {
                    mToast.setText("正在加载 page: " + page);
                }
                mToast.show();

                mRootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setLoading(false);
                        List<String> data = createItems(page, LIMIT);
                        if (data == null || data.size() < LIMIT) {
                            adapter.dataNoMore();
                        }
                        adapter.addItem(data);
                    }
                }, 1500);
            }
        });
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.w(TAG, "刷新");
//                adapter.dataLoading(); 直接修改状态的话会导致刷新和加载更多都被触发
                mRootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clearItem();
                        refreshLayout.setRefreshing(false);
                        List<String> data = createItems(0, LIMIT);
                        if (data == null || data.size() < LIMIT) {
                            adapter.dataNoMore();
                        } else {
                            adapter.dataLoading();
                        }
                        adapter.addItem(data);
                    }
                }, 1500);
            }
        });

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private List<String> createItems(int page, int limit) {
        List<String> data = new ArrayList<>();
        int count = limit;
        if (page == 5) {
            count = 5;
        }
        for (int i = 0; i < count; i++) {
            data.add(i + (page * limit) +  "");
        }
        return data;
    }

    private class ListAdapter extends LoadMoreAdapter<String, ItemViewHolder> {

        private View mLoading;
        private View mNoMore;
        private View mFail;

        public ListAdapter(List<String> data, int limit, View loadMoreView) {
            this(data, limit);
            setLoadMoreView(loadMoreView);

            mLoading = loadMoreView.findViewById(R.id.loading);
            mNoMore = loadMoreView.findViewById(R.id.no_more);
            mFail = loadMoreView.findViewById(R.id.fail);
        }

        public ListAdapter(List<String> data, int limit) {
            super(data, limit);
        }

        @Override
        public ItemViewHolder onCreateVH(LayoutInflater inflater, ViewParent parent, int viewType) {
            View view = inflater.inflate(android.R.layout.simple_list_item_1, (ViewGroup) parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindVH(ItemViewHolder holder, int position) {
            String text = getItem(position);
            holder.text1.setText(text);
        }

        @Override
        public void dataNoMore() {
            mLoading.setVisibility(View.GONE);
            mNoMore.setVisibility(View.VISIBLE);
            mFail.setVisibility(View.GONE);

            setEnableLoadMore(false);
        }

        @Override
        public void dataLoading() {
            mLoading.setVisibility(View.VISIBLE);
            mNoMore.setVisibility(View.GONE);
            mFail.setVisibility(View.GONE);

            setEnableLoadMore(true);
        }

        @Override
        public void dataLoadFail() {
            mLoading.setVisibility(View.GONE);
            mNoMore.setVisibility(View.GONE);
            mFail.setVisibility(View.VISIBLE);

            setEnableLoadMore(false);
        }

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text1;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
