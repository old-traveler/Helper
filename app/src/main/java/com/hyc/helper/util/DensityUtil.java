package com.hyc.helper.util;

import android.util.DisplayMetrics;
import com.hyc.helper.HelperApplication;
import com.hyc.helper.base.util.UiHelper;

public class DensityUtil {

  public static int dip2px(float dpValue) {
    final float scale = UiHelper.getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }


  public static int px2dip(float pxValue) {
    final float scale = UiHelper.getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  public static int getScreenWidth() {
    DisplayMetrics dm = HelperApplication.getContext().getResources().getDisplayMetrics();
    return dm.widthPixels;
  }

  public static int getScreenHeight() {
    DisplayMetrics dm = HelperApplication.getContext().getResources().getDisplayMetrics();
    return dm.heightPixels;
  }



}
