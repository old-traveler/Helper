package com.hyc.helper;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Environment;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.DaoHelper;
import com.hyc.helper.helper.MessageHandler;
import com.hyc.helper.im.ConnectManager;
import com.hyc.helper.util.RxBus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class HelperApplication extends Application {

  @SuppressLint("StaticFieldLeak")
  private static Context mContext;

  private static boolean netAvailable;

  @Override
  public void onCreate() {
    super.onCreate();
    mContext = getApplicationContext();
    if (getApplicationInfo().packageName.equals(getMyProcessName())) {
      BmobIM.init(this);
      BmobIM.registerDefaultMessageHandler(new MessageHandler());
      ConnectManager.getDefault();
    }
    DaoHelper.getDefault();
    registerNetListener();
  }

  public static Context getContext() {
    return mContext;
  }

  public static boolean isNetAvailable() {
    return netAvailable;
  }

  private void registerNetListener() {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager != null) {
      connectivityManager.requestNetwork(new NetworkRequest
          .Builder().build(), new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
          super.onAvailable(network);
          netAvailable = true;
          RxBus.getDefault().post(new MessageEvent<>(Constant.EventType.NET_AVAILABLE, null));
        }

        @Override
        public void onLost(Network network) {
          super.onLost(network);
          netAvailable = false;
        }
      });
    }
  }

  /**
   * 获取当前运行的进程名
   */
  public static String getMyProcessName() {
    try {
      File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
      BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
      String processName = mBufferedReader.readLine().trim();
      mBufferedReader.close();
      return processName;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
