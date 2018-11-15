package com.hyc.helper.helper;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import com.hyc.helper.bean.ConfigureDateBean;
import java.io.File;
import java.io.IOException;

import static android.content.Context.DOWNLOAD_SERVICE;

public class UpdateAppHelper {

  //private static String checkUpdate(ConfigureDateBean configureBean) {
  //    if (configureBean != null && !TextUtils.isEmpty(configureBean.getUpdate())) {
  //      return configureBean.getUpdate();
  //    }
  //    return null;
  //}

  public static void download(String url, String fileName, Context context) {
    DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
    String path = Environment.getExternalStorageDirectory().getPath() + "/helper/" + fileName;
    File file = new File(path);
    if (file.exists()) {
      file.delete();
    } else {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    LogHelper.log(url + "   ");
    long downloadId = downloadManager
        .enqueue(new DownloadManager.Request(Uri.parse(url))
            .setDestinationInExternalPublicDir("/helper/", fileName)
            .setTitle("helper")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED));
  }
}
