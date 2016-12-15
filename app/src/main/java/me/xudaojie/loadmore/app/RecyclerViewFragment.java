package me.xudaojie.loadmore.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import me.xdj.refresh.app.R;
import me.xudaojie.loadmore.RecyclerViewRefreshLayout;

/**
 * Created by xdj on 16/3/2.
 */
public class RecyclerViewFragment extends Fragment {
    private static final String TAG = "RecyclerViewFragment";

    private Activity mContext;
    private RecyclerViewRefreshLayout mRefresh;
    private RecyclerView mContainer;
    private ContainerAdapter mAdapter;
    private Handler mHandler;
    private int mPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mContainer = (RecyclerView) view.findViewById(R.id.container);
        mRefresh = (RecyclerViewRefreshLayout) view.findViewById(R.id.refresh);
        mHandler = new Handler();

        mAdapter = new ContainerAdapter();
        mContainer.setLayoutManager(new LinearLayoutManager(mContext));
        mContainer.setAdapter(mAdapter);
        mContainer.setHasFixedSize(true); // 如果内容格式固定，会优化效果
        mRefresh.setChildView(mContainer);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(mContext, "onRefresh", Toast.LENGTH_SHORT).show();
                mPage = 0;
                loadData();
                mRefresh.setRefreshing(false);
            }
        });
        mRefresh.setOnLoadListener(new RecyclerViewRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Toast.makeText(mContext, "onLoad", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        mRefresh.setLoading(false);
                    }
                }, 2000);
            }
        });
        return view;
    }

    private void loadData() {
        if (mPage == 2) {
            return;
        }
        if (mRefresh.isLoading()) {
            mPage++;
            mAdapter.notifyDataSetChanged();
        }
    }

    class ContainerAdapter extends RecyclerView.Adapter<ContainerAdapter.ItemViewHolder> {
        @Override
        public ContainerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.text1.setText(position + "");
        }

        @Override
        public int getItemCount() {
            return (mPage + 1) * 15;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView text1;

            public ItemViewHolder(View itemView) {
                super(itemView);
                text1 = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
