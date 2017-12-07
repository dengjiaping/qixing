package com.qixing.app;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by ren on 2015/4/27 0027.
 */
public class DownloadBaseActivity extends AppCompatActivity {

  /**
   * Find all view.
   */
  protected void initView() {
  }

  /**
   * Set some style.
   */
  protected void initStyle() {
  }

  /**
   * Set data.
   */
  protected void initData() {

  }

  /**
   * Bind view listener.
   */
  protected void initListener() {
  }

  protected void init() {
    beforeInit();
    initView();
    initStyle();
    initData();
    initListener();
  }

  protected void beforeInit() {

  }

  @Override
  public void setContentView(int layoutResID) {
    super.setContentView(layoutResID);
    init();
  }

  @Override
  public void setContentView(View view) {
    super.setContentView(view);
    init();
  }

  @Override
  public void setContentView(View view, ViewGroup.LayoutParams params) {
    super.setContentView(view, params);
    init();
  }


  protected FragmentActivity getActivity() {
    return this;
  }


  public void toActivity(Class<?> clazz) {
    toActivity(new Intent(getActivity(), clazz));
  }

  public void toActivity(Intent intent) {
    startActivity(intent);
  }

  public void toActivityAfterFinishThis(Class<?> clazz) {
    toActivity(clazz);
    finish();
  }

  public void toActivityAfterFinishThis(Intent intent) {
    toActivity(intent);
    finish();
  }

  public void toActivityForResult(Intent intent, int requestCode) {
    startActivityForResult(intent, requestCode);
  }

  public void initBackListener(View view) {
    if (view != null) {


//            BGATitlebar mBackView = (BGATitlebar) view.findViewById(R.id.mTitleBar);
//
//            if (mBackView != null) {
//
//                mBackView.setDelegate(new BGATitlebar.BGATitlebarDelegate() {
//
//                    @Override
//                    public void onClickLeftCtv() {
//                        super.onClickLeftCtv();
//                        AppManager.getAppManager().finishActivity();
//                    }
//                });
//            }


//            btn_sure = (Button) view.findViewById(R.id.btn_sure);
//
//            if (btn_sure != null) {
//
//                btn_sure.setOnClickListener(this);
//
//            }


    }

  }

}
