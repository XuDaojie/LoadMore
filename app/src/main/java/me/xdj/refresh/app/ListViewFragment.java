package me.xdj.refresh.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xdj on 16/1/22.
 */
public class ListViewFragment extends Fragment {
    private List<String> mData = new ArrayList<>();
    private ListView mList;
    private ArrayAdapter mAdapter;
    private int mPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mList = (ListView) view.findViewById(R.id.container);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mData);
        mList.setAdapter(mAdapter);
        loadData(mPage);
        return view;
    }

    private void loadData(int page) {
        while (mData.size() < (page + 1) * 15) {
            mData.add(mData.size() + "");
        }
        mAdapter.notifyDataSetChanged();
        ++mPage;
    }
}
