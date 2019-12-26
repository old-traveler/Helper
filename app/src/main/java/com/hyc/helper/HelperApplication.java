package com.hyc.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import cn.bmob.newim.BmobIM;
import com.hyc.cuckoo_lib.CuckooPermission;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.cuckoo.PermissionRefuseListener;
import com.hyc.helper.cuckoo.RxPermissionApplicant;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.DaoHelper;
import com.hyc.helper.helper.MessageHandler;
import com.hyc.helper.im.ConnectManager;
import com.hyc.helper.util.RxBus;
import io.flutter.app.FlutterApplication;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class HelperApplication extends FlutterApplication {

  @SuppressLint("StaticFieldLeak")
  private static Context mContext;

  private static boolean netAvailable;

  @Override
  public void onCreate() {
    super.onCreate();
    mContext = getApplicationContext();
    CuckooPermission.init(this, new RxPermissionApplicant(), new PermissionRefuseListener());
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
