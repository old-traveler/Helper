package com.hyc.helper.helper;

import android.util.Log;

public class LogHelper {

  public static final String TAG = "helperTAG";

  public static void log(String msg) {
    if (msg != null){
      Log.e(TAG, msg);

    }
  }
}
