package com.hyc.helper.helper;

import android.text.TextUtils;
import android.widget.EditText;
import com.hyc.helper.base.util.ToastHelper;

public class ValidationHelper {

  public static boolean isNotInputEmpty(EditText editText, String msg) {
    if (TextUtils.isEmpty(editText.getText().toString())) {
      ToastHelper.toast(msg);
      return false;
    }
    return true;
  }
}
