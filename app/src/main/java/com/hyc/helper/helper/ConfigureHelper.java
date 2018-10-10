package com.hyc.helper.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.model.ConfigModel;

public class ConfigureHelper {

  public static void init(ConfigureBean configureBean) {
    ConfigModel configModel = new ConfigModel();
    ConfigureBean preConfigure = configModel.getConfigInfo();
    if (preConfigure == null) {
      if (configureBean != null) {
        DateHelper.DATE_OF_SCHOOL = configureBean.getDate();
        configModel.setConfigInfo(configureBean);
      }
    } else {
      if (configureBean == null) {
        DateHelper.DATE_OF_SCHOOL = preConfigure.getDate();
      } else {
        long preUpdateTime = Long.parseLong(preConfigure.getUpdate_time());
        long curUpdateTime = Long.parseLong(configureBean.getUpdate_time());
        if (curUpdateTime > preUpdateTime) {
          configModel.setConfigInfo(configureBean);
          DateHelper.DATE_OF_SCHOOL = configureBean.getDate();
        } else {
          DateHelper.DATE_OF_SCHOOL = preConfigure.getDate();
        }
      }
    }
  }

  /**
   * get App versionCode
   */
  public static int getVersionCode(Context context) {
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo;
    int versionCode = 1;
    try {
      packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
      versionCode = packageInfo.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionCode;
  }
}
