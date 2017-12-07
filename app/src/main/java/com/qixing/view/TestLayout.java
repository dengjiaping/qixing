package com.qixing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by lenovo on 2016/11/2.
 */
public class TestLayout extends FrameLayout {
    private int mL,mT,mR,mB;
    private boolean isFist = true;

    public TestLayout(Context context) {
        super(context);
    }

    public TestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isFist){
            mL = l;
            mT=t;
            mR=r;
            mB=b;
            isFist =false;
        }
        getChildAt(0).layout(mL, mT, mR, mB);
    }
}
