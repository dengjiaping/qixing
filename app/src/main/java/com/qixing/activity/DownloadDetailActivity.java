package com.qixing.activity;

import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_COMPLETED;
import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_REMOVED;
import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_WAIT;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qixing.R;
import com.qixing.app.AppManager;
import com.qixing.app.BaseActivity;
import com.qixing.app.DownloadBaseActivity;
import com.qixing.app.MyApplication;
import com.qixing.utlis.FileUtil;
import com.qixing.utlis.callback.MyDownloadListener;
import com.qixing.utlis.domain.MyBusinessInfo;
import com.qixing.view.titlebar.BGATitlebar;

import java.io.File;
import java.lang.ref.SoftReference;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;


/**
 * Created by ren on 2015/4/27 0027.
 */
public class DownloadDetailActivity extends BaseActivity {

  public static final String DATA = "DATA";
  private DownloadManager downloadManager;
  private DownloadInfo downloadInfo;

  private ImageView iv_icon;
  private TextView tv_size;
  private TextView tv_status;
  private ProgressBar pb;
  private TextView tv_name;
  private TextView bt_action;
  private MyBusinessInfo data;

  private File dir;
  private BGATitlebar mTitlebar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_download_detail);

    initBackListener(getWindow().getDecorView().findViewById(android.R.id.content));

    initView();
    initData();
  }


  protected void initView() {
    mTitlebar= (BGATitlebar) findViewById(R.id.mTitleBar);
    mTitlebar.setTitleText("下载详情");
    mTitlebar.setDelegate(new BGATitlebar.BGATitlebarDelegate(){
      @Override
      public void onClickLeftCtv() {
        super.onClickLeftCtv();
        AppManager.getAppManager().finishActivity();
      }
    });

    iv_icon = (ImageView) findViewById(R.id.iv_icon);
    tv_size = (TextView) findViewById(R.id.tv_size);
    tv_status = (TextView) findViewById(R.id.tv_status);
    pb = (ProgressBar) findViewById(R.id.pb);
    tv_name = (TextView) findViewById(R.id.tv_name);
    bt_action = (TextView) findViewById(R.id.bt_action);
  }

  @SuppressWarnings("unchecked")
  protected void initData() {
    data = (MyBusinessInfo) getIntent().getSerializableExtra(DATA);
    Glide.with(this).load(data.getIcon()).into(iv_icon);

    downloadManager = DownloadService.getDownloadManager(getApplicationContext());

    downloadInfo = downloadManager.getDownloadById(data.getUrl().hashCode());

    if (downloadInfo != null) {
      downloadInfo
          .setDownloadListener(new MyDownloadListener(new SoftReference(null)) {

            @Override
            public void onRefresh() {
              refresh();
            }
          });
    }

    refresh();

    bt_action.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (downloadInfo != null) {

          switch (downloadInfo.getStatus()) {
            case DownloadInfo.STATUS_NONE:
            case DownloadInfo.STATUS_PAUSED:
            case DownloadInfo.STATUS_ERROR:

              //resume downloadInfo
              downloadManager.resume(downloadInfo);
              break;

            case DownloadInfo.STATUS_DOWNLOADING:
            case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
            case STATUS_WAIT:
              //pause downloadInfo
              downloadManager.pause(downloadInfo);
              break;
            case DownloadInfo.STATUS_COMPLETED:
              downloadManager.remove(downloadInfo);
              break;
          }
        } else {
//            Create new download task
          if(".mp4".equals(MyApplication.getInstance().getDown_fileMk())) {
            dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "qixing/video");
          }else if(".pdf".equals(MyApplication.getInstance().getDown_fileMk())){
            dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"qixing/pdf");
          }
          if (!dir.exists()) {
            dir.mkdirs();
          }
          String path = dir.getAbsolutePath().concat("/").concat(data.getName()+MyApplication.getInstance().getDown_fileMk());
          downloadInfo = new DownloadInfo.Builder().setUrl(data.getUrl())
              .setPath(path)
              .build();
          downloadInfo
              .setDownloadListener(new MyDownloadListener(new SoftReference(null)) {

                @Override
                public void onRefresh() {
                  refresh();
                }
              });
          downloadManager.download(downloadInfo);
        }
      }
    });
  }

  private void refresh() {
    if (downloadInfo == null) {
      tv_size.setText("");
      pb.setProgress(0);
      bt_action.setText("下载");
      tv_status.setText("");
    } else {
      switch (downloadInfo.getStatus()) {
        case DownloadInfo.STATUS_NONE:
          bt_action.setText("下载");
          tv_status.setText("");
          MyApplication.getInstance().setIs_Down("not_download");
          break;
        case DownloadInfo.STATUS_PAUSED:
        case DownloadInfo.STATUS_ERROR:
          bt_action.setText("继续");
          tv_status.setText("暂停");
          try {
            pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
          } catch (Exception e) {
            e.printStackTrace();
          }
          tv_size.setText(FileUtil.formatFileSize(downloadInfo.getProgress()) + "/" + FileUtil
              .formatFileSize(downloadInfo.getSize()));
          MyApplication.getInstance().setIs_Down("fail");
          break;

        case DownloadInfo.STATUS_DOWNLOADING:
        case DownloadInfo.STATUS_PREPARE_DOWNLOAD:
          bt_action.setText("暂停");
          try {
            pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
          } catch (Exception e) {
            e.printStackTrace();
          }
          tv_size.setText(FileUtil.formatFileSize(downloadInfo.getProgress()) + "/" + FileUtil
              .formatFileSize(downloadInfo.getSize()));
          tv_status.setText("正在下载...");
          MyApplication.getInstance().setIs_Down("downloading");
          break;
        case STATUS_COMPLETED:
          bt_action.setText("移除");
          try {
            pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
          } catch (Exception e) {
            e.printStackTrace();
          }
          tv_size.setText(FileUtil.formatFileSize(downloadInfo.getProgress()) + "/" + FileUtil
              .formatFileSize(downloadInfo.getSize()));
          tv_status.setText("下载成功");
          MyApplication.getInstance().setIs_Down("success");
          break;
        case STATUS_REMOVED:
          tv_size.setText("");
          pb.setProgress(0);
          bt_action.setText("下载");
          tv_status.setText("");
          MyApplication.getInstance().setIs_Down("not_download");
          break;
        case STATUS_WAIT:
          tv_size.setText("");
          pb.setProgress(0);
          bt_action.setText("暂停");
          tv_status.setText("等待下载...");
          MyApplication.getInstance().setIs_Down("wait_download");
          break;
      }

    }
  }

}
