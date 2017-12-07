package com.qixing.utlis.listener;

import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/11/20.
 */
public class AutoLoadListener implements AbsListView.OnScrollListener {
    public interface AutoLoadCallBack {
        void execute();
        void refresh();
    }

    private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;
    private AutoLoadCallBack mCallback;

    public AutoLoadListener(AutoLoadCallBack callback) {
        this.mCallback = callback;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            //滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View v = (View) view.getChildAt(view.getChildCount() - 1);
                int[] location = new int[2];
                v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                int y = location[1];

                if (view.getLastVisiblePosition() != getLastVisiblePosition && lastVisiblePositionY != y)//第一次拖至底部
                {
//                    Toast.makeText(view.getContext(), "已经拖动至底部，再次拖动即可翻页", Toast.LENGTH_SHORT).show();
                    getLastVisiblePosition = view.getLastVisiblePosition();
                    lastVisiblePositionY = y;
                    mCallback.execute();
                    return;
                }
//                else if (view.getLastVisiblePosition() == getLastVisiblePosition && lastVisiblePositionY == y)//第二次拖至底部
//                {
//                }
            }
            //未滚动到底部，第二次拖至底部都初始化
            getLastVisiblePosition = 0;
            lastVisiblePositionY = 0;

        }else{
            mCallback.refresh();
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
