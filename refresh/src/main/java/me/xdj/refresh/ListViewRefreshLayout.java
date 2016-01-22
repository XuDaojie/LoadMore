package me.xdj.refresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Created by xdj on 16/1/23.
 */
public class ListViewRefreshLayout extends SwipeRefreshLayout {

    public ListViewRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewRefreshLayout(Context context) {
        super(context);
    }
}
