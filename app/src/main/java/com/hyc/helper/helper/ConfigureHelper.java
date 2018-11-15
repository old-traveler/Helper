package com.hyc.helper.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.hyc.helper.bean.ConfigureDateBean;

public class ConfigureHelper {

  public static void init(ConfigureDateBean configureDateBean) {
    if (configureDateBean != null){
      SpCacheHelper.putString("school_date",configureDateBean.getDate());
    }
    initSchoolDate();
  }

  public static String getDateOfSchool(){
    return SpCacheHelper.getString("school_date");
  }

  public static void initSchoolDate(){
    String date = getDateOfSchool();
    if (!TextUtils.isEmpty(date)){
      DateHelper.DATE_OF_SCHOOL = getDateOfSchool();
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
