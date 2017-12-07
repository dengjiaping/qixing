package com.qixing.widget;


import android.support.annotation.StringRes;
import android.widget.Toast;

import com.qixing.app.MyApplication;


public class Toasts {

    private Toasts() {
    }

    public static void show(CharSequence text) {
        if (text.length() < 10) {
            Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_LONG).show();
        }
    }

    public static void show(@StringRes int resId) {
        show(MyApplication.getInstance().getString(resId));
    }

}
