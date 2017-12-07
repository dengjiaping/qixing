package com.qixing.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qixing.R;
import com.qixing.activity.DownloadListActivity;
import com.qixing.activity.DownloadManagerActivity;
import com.qixing.app.MyApplication;
import com.qixing.global.Constant;
import com.qixing.utlis.FileUtil;
import com.qixing.utlis.callback.MyDownloadListener;
import com.qixing.utlis.db.DBController;
import com.qixing.utlis.domain.MyBusinessInfLocal;
import com.qixing.utlis.domain.MyBusinessInfo;
import com.qixing.utlis.images.ImageLoaderOptions;
import com.qixing.widget.Toasts;

import java.io.File;
import java.lang.ref.SoftReference;
import java.sql.SQLException;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;
import cn.woblog.android.downloader.domain.DownloadInfo.Builder;

import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_COMPLETED;
import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_REMOVED;
import static cn.woblog.android.downloader.domain.DownloadInfo.STATUS_WAIT;

/**
 * Created by renpingqing on 17/1/19.
 */
public class DownloadListAdapter extends
        BaseRecyclerViewAdapter<MyBusinessInfo, DownloadListAdapter.ViewHolder> {

    private static final String TAG = "DownloadListAdapter";
    private final Context context;
    private final DownloadManager downloadManager;
    private DBController dbController;

    public DownloadListAdapter(Context context) {
        super(context);
        this.context = context;
        downloadManager = DownloadService.getDownloadManager(context.getApplicationContext());
        try {
            dbController = DBController.getInstance(context.getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_download_info, parent, false));
    }

    @Override
    public void onBindViewHolder(DownloadListAdapter.ViewHolder holder, final int position) {
        holder.bindData(getData(position), position, context);

        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_icon;
        private final ImageView img_play;
        private final TextView tv_path;
        private final TextView tv_size;
        private final TextView tv_status;
        private final ProgressBar pb;
        private final TextView tv_name;
        private final TextView bt_action;
        private DownloadInfo downloadInfo;
        private File dir;

        public ViewHolder(View view) {
            super(view);
            itemView.setClickable(true);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            img_play = (ImageView) view.findViewById(R.id.download_info_img_play);
            tv_size = (TextView) view.findViewById(R.id.tv_size);
            tv_status = (TextView) view.findViewById(R.id.tv_status);
            pb = (ProgressBar) view.findViewById(R.id.pb);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            bt_action = (TextView) view.findViewById(R.id.bt_action);
            tv_path = (TextView) view.findViewById(R.id.tv_downpath);
        }

        @SuppressWarnings("unchecked")
        public void bindData(final MyBusinessInfo data, int position, final Context context) {
//      Glide.with(context).load(data.getIcon()).into(iv_icon);
            if (!TextUtils.isEmpty(data.getIcon())) {
                if (data.getIcon().startsWith("http")) {
                    ImageLoader.getInstance().displayImage(data.getIcon(), iv_icon, ImageLoaderOptions.getOptions());
                } else {
                    ImageLoader.getInstance().displayImage(Constant.BASE_URL_IMG + data.getIcon(), iv_icon, ImageLoaderOptions.getOptions());
                }
            }
            tv_name.setText(data.getName());

            // Get download task status
            downloadInfo = downloadManager.getDownloadById(data.getUrl().hashCode());

            // Set a download listener
            if (downloadInfo != null) {
                downloadInfo
                        .setDownloadListener(new MyDownloadListener(new SoftReference(ViewHolder.this)) {
                            //  Call interval about one second
                            @Override
                            public void onRefresh() {
                                if (getUserTag() != null && getUserTag().get() != null) {
                                    ViewHolder viewHolder = (ViewHolder) getUserTag().get();
                                    viewHolder.refresh();
                                }
                            }
                        });
            }

            refresh();

//      Download button
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
                        if (".mp4".equals(MyApplication.getInstance().getDown_fileMk())) {
                            dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "com.qixing/video");
                        } else if (".pdf".equals(MyApplication.getInstance().getDown_fileMk())) {
                            dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "com.qixing/pdf");
                        }
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        String path = dir.getAbsolutePath().concat("/").concat(data.getName() + MyApplication.getInstance().getDown_fileMk());
                        tv_path.setText(path);
                        downloadInfo = new Builder().setUrl(data.getUrl())
                                .setPath(path)
                                .build();
                        downloadInfo
                                .setDownloadListener(new MyDownloadListener(new SoftReference(ViewHolder.this)) {

                                    @Override
                                    public void onRefresh() {
                                        notifyDownloadStatus();

                                        if (getUserTag() != null && getUserTag().get() != null) {
                                            ViewHolder viewHolder = (ViewHolder) getUserTag().get();
                                            viewHolder.refresh();
                                        }
                                    }
                                });
                        downloadManager.download(downloadInfo);

                        //save extra info to my database.
                        MyBusinessInfLocal myBusinessInfLocal = new MyBusinessInfLocal(
                                data.getUrl().hashCode(), data.getName(), data.getIcon(), data.getUrl());
                        try {
                            dbController.createOrUpdateMyDownloadInfo(myBusinessInfLocal);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            img_play.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (".mp4".equals(MyApplication.getInstance().getDown_fileMk())) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        File file=new File(downloadInfo.getPath());
                        if (file.exists()){
                            intent.setDataAndType(Uri.parse(downloadInfo.getPath()), "video/*");
                            context.startActivity(intent);
                        }else{
                            Toasts.show("视频文件已损坏,建议重新缓存视频后播放");
                        }
                    } else if (".pdf".equals(MyApplication.getInstance().getDown_fileMk())) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        File file=new File(downloadInfo.getPath());
                        if(file.exists()){
                            intent.setDataAndType(Uri.parse(downloadInfo.getPath()), "application/pdf");
                            context.startActivity(intent);
                        }else{
                            Toasts.show("文档文件已损坏,建议重新缓存文档后阅读");
                        }
                    }

                }
            });
        }

        private void notifyDownloadStatus() {
            if (downloadInfo.getStatus() == STATUS_REMOVED) {
                try {
                    dbController.deleteMyDownloadInfo(downloadInfo.getUri().hashCode());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

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
                        tv_status.setText("暂停下载");
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
                        Intent intent = new Intent(context, DownloadManagerActivity.class);
                        intent.putExtra("tag", "PlayVideoActivity");
                        context.startActivity(intent);
                        if (context instanceof DownloadListActivity) {// DownloadListActivity.class.isInstance(context)
                            DownloadListActivity Activity = (DownloadListActivity) context;
                            Activity.finish();
                        }
                        break;
                    case STATUS_COMPLETED:
                        bt_action.setText("移除");
                        try {
                            pb.setProgress((int) (downloadInfo.getProgress() * 100.0 / downloadInfo.getSize()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pb.setVisibility(View.GONE);
                        tv_size.setText(FileUtil
                                .formatFileSize(downloadInfo.getSize()));
                        img_play.setVisibility(View.VISIBLE);
                        tv_status.setText("下载成功");
                        MyApplication.getInstance().setIs_Down("success");
                        break;
                    case STATUS_REMOVED:
                        tv_size.setText("");
                        pb.setVisibility(View.VISIBLE);
                        pb.setProgress(0);
                        bt_action.setText("下载");
                        tv_status.setText("");
                        tv_path.setText("");
                        img_play.setVisibility(View.GONE);
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
}
