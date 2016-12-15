package me.xudaojie.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.xdj.refresh.R;

/**
 * Created by xdj on 16/3/3.
 */
public class HeaderViewRecyclerAdapter extends SimpleRecyclerViewAdapter {

    public HeaderViewRecyclerAdapter(RecyclerView.Adapter baseAdapter) {
        super(baseAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View mLoading = inflater.inflate(R.layout.view_loading_refresh, parent, false);

        return new FooterHolder(mLoading);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }
}
