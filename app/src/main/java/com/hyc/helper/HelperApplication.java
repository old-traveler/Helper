package com.hyc.helper;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import com.hyc.helper.helper.DaoHelper;

public class HelperApplication extends Application {

  @SuppressLint("StaticFieldLeak")
  private static Context mContext;

  @Override
  public void onCreate() {
    super.onCreate();
    mContext = getApplicationContext();
    DaoHelper.getDefault();
  }

  public static Context getContext() {
    return mContext;
  }
}
