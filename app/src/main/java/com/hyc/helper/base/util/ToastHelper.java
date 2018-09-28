package com.hyc.helper.base.util;

import android.widget.Toast;
import com.hyc.helper.HelperApplication;

public class ToastHelper {

  public static void toast(String msg){
    Toast.makeText(HelperApplication.getContext(), msg, Toast.LENGTH_SHORT).show();
  }

  public static void toast(int resId){
    toast(UiHelper.getString(resId));
  }

}
