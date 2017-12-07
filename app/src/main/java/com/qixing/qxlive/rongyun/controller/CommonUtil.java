package com.qixing.qxlive.rongyun.controller;


import com.qixing.app.MyApplication;

public class CommonUtil {

    public static int dip2px(float dpValue) {
        float scale = MyApplication.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
