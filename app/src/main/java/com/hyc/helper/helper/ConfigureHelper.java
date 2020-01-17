package com.hyc.helper.helper;

import android.text.TextUtils;
import com.hyc.helper.BuildConfig;
import com.hyc.helper.bean.ConfigureDateBean;

public class ConfigureHelper {

  public static void init(ConfigureDateBean configureDateBean) {
    if (configureDateBean != null) {
      SpCacheHelper.putString("school_date", configureDateBean.getDate());
    }
    initSchoolDate();
  }

  public static String getDateOfSchool() {
    return SpCacheHelper.getString("school_date");
  }

  public static void initSchoolDate() {
    String date = getDateOfSchool();
    if (!TextUtils.isEmpty(date)) {
      DateHelper.DATE_OF_SCHOOL = getDateOfSchool();
    }
  }

  /**
   * get App versionCode
   */
  public static int getVersionCode() {
    return BuildConfig.VERSION_CODE;
  }

  public static String getVersionName() {
    return BuildConfig.VERSION_NAME;
  }
}
