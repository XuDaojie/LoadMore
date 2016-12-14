package me.xdj.refresh.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.xdj.refresh.LoadMoreAdapter;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        List<String> data = createItems(0, LIMIT);
        final ListAdapter adapter = new ListAdapter(data, LIMIT);
        adapter.setLoadMoreListener(new LoadMoreAdapter.LoadMoreListener() {
            @Override
            public void onLoadMore(final int page) {
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
                            adapter.setEnableLoadMore(false);
                        }
                        adapter.addItem(data);
                    }
                }, 1500);
            }
        });
        recyclerView.setAdapter(adapter);
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

    class ListAdapter extends LoadMoreAdapter<String, ItemViewHolder> {

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

    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView text1;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
