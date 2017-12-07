package com.qixing.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.qixing.R;
import com.qixing.app.MyApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class TimeButton extends Button {
    private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private String textafter = "秒后重新获取";
    private String textbefore = "点击获取验证码";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private OnClickListener mOnclickListener;
    public Timer t;
    public TimerTask tt;
    private long time;
    public boolean is_ok=false;
    Map<String, Long> map = new HashMap<String, Long>();

    public TimeButton(Context context) {
        super(context);
       // setOnClickListener(this);

    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
       // setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText(time / 1000 + textafter);
            TimeButton.this.setTextColor(getResources().getColor(R.color.gray_6c));

            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                TimeButton.this.setText(textbefore);
                clearTimer();
                TimeButton.this.setTextColor(getResources().getColor(R.color.gray_66));
            }
        };
    };

    private void initTimer() {
        time = lenght;
        t = new Timer();
        tt = new TimerTask() {

            @Override
            public void run() {
                Log.e("yung", time / 1000 + "");
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }


    public void StartTimer(){
        initTimer();
            this.setText(time / 1000 + textafter);
            this.setEnabled(false);
            t.schedule(tt, 0, 1000);

    }

//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        if (l instanceof TimeButton) {
//            super.setOnClickListener(l);
//        } else
//            this.mOnclickListener = l;
//    }
//
//    @Override
//    public void onClick(View v) {
//
//
//
//
//
//            if (mOnclickListener != null)
//                mOnclickListener.onClick(v);
//            initTimer();
//            this.setText(time / 1000 + textafter);
//            this.setEnabled(false);
//            t.schedule(tt, 0, 1000);
//
//
//
//
//
//        // t.scheduleAtFixedRate(task, delay, period);
//    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (MyApplication.TimeMap == null)
            MyApplication.TimeMap = new HashMap<String, Long>();
        MyApplication.TimeMap.put(TIME, time);
        MyApplication.TimeMap.put(CTIME, System.currentTimeMillis());
        clearTimer();
        Log.e("yung", "onDestroy");
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate(Bundle bundle) {

        if (MyApplication.TimeMap == null)
            return;
        if (MyApplication.TimeMap.size() <= 0)// 这里表示没有上次未完成的计时
            return;
        long time = System.currentTimeMillis() - MyApplication.TimeMap.get(CTIME)
                - MyApplication.TimeMap.get(TIME);
        MyApplication.TimeMap.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            this.setText(time + textafter);
            this.setEnabled(false);
        }
    }

    /** * 设置计时时候显示的文本 */
    public TimeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /** * 设置点击之前的文本 */
    public TimeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }

    /**
     * 设置到计时长度
     *
     * @param lenght
     *            时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }
	/*

*
*/
}