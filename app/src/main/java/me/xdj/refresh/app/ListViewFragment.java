package me.xdj.refresh.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.xdj.refresh.ListViewRefreshLayout;

/**
 * Created by xdj on 16/1/22.
 */
public class ListViewFragment extends Fragment {
    private List<String> mData = new ArrayList<>();
    private ListView mList;
    private ListViewRefreshLayout mRefreshLayout;
    private ArrayAdapter mAdapter;
    private int mPage;

    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mList = (ListView) view.findViewById(R.id.container);
        mRefreshLayout = (ListViewRefreshLayout) view.findViewById(R.id.refresh);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mData);
        mList.setAdapter(mAdapter);
        mRefreshLayout.setChildView(mList);
        loadData(mPage);
        initEvent();
        return view;
    }

    private void initEvent() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 0;
                        loadData(0);
                        mRefreshLayout.setLoading();
                        mRefreshLayout.setRefreshing(false);
                        mRefreshLayout.setLoading(false);
                    }
                }, 1000);
            }
        });
        mRefreshLayout.setOnLoadListener(new ListViewRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ListViewFragment", "loading");
                        loadData(mPage);
                    }
                }, 1000);
            }
        });
    }

    private void loadData(int page) {
        if (mPage == 3) {
            return;
        }
        if (mPage == 0 && mData.size() > 0) {
            mData.clear();
        }
        while (mData.size() < (page + 1) * 15) {
            mData.add(mData.size() + "");
            Log.d("ListViewFragment", "mData.size():" + mData.size());
        }
        mAdapter.notifyDataSetChanged();
        ++mPage;
        if (mPage == 3) {
            mRefreshLayout.setEmpty();
            Log.d("ListViewFragment", "setEmpty" + mPage);
        }
        mRefreshLayout.setLoading(false);
    }
}
