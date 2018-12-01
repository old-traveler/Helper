package com.hyc.helper.helper;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import com.hyc.helper.HelperApplication;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.ConfigureDateBean;
import java.io.File;
import java.io.IOException;

import static android.content.Context.DOWNLOAD_SERVICE;

public class UpdateAppHelper {

  private Context context;

  private BroadcastReceiver receiver;

  public void download(String url, String fileName, Context context) {
    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
    String path = Environment.getExternalStorageDirectory().getPath() + "/helper/" + fileName;
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    }
    this.context = context;
    if(downloadManager == null){
      ToastHelper.toast("下载失败，系统不支持");
      return;
    }
    long downloadId = downloadManager
        .enqueue(new DownloadManager.Request(Uri.parse(url))
            .setDestinationInExternalPublicDir("/helper/", fileName)
            .setTitle("helper")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED));
    registerDownApkListener(downloadId,path);
  }

  public void registerDownApkListener(final long downloadId, final String path) {
    IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    context.registerReceiver(receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if (downloadId == id) {
          installApk(path);
        }
      }
    }, intentFilter);
  }

  private void installApk(String path) {
    File file = new File(path);
    if (!file.exists()) {
      return;
    }
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(Intent.ACTION_VIEW);
    Uri data;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      data = FileProvider.getUriForFile(context, "com.helper.fileprovider", file);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    } else {
      data = Uri.fromFile(file);
    }

    intent.setDataAndType(data, "application/vnd.android.package-archive");
    context.startActivity(intent);
    clear();
  }

  public void clear(){
    if (context != null){
      context.unregisterReceiver(receiver);
      context = null;
    }
  }

}
