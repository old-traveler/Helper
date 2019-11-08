package com.hyc.helper.helper;

import android.util.Log;
import com.hyc.helper.BuildConfig;

public class LogHelper {

  public static final String TAG = "helperTAG";

  public static void log(String msg) {
    if (msg != null && BuildConfig.LOG_DEBUG) {
      Log.e(TAG, msg);
    }
  }
}
