package me.xdj.refresh.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xdj.refresh.MultiStateView;

/**
 * Created by xdj on 16/2/3.
 */
public class MultiStateFragment extends Fragment {
    private Handler mHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_state, container, false);
        final MultiStateView multiStateView = (MultiStateView) view.findViewById(R.id.multi_state_view);
        multiStateView.setViewState(MultiStateView.LOADING);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                multiStateView.setViewState(MultiStateView.FAIL);
            }
        }, 2000);
        return view;
    }
}
