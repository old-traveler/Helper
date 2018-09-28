package com.hyc.helper.base.interfaces;

import android.os.Bundle;
import android.widget.EditText;

public interface IBaseActivity {

  void goToOtherActivity(Class<?> cls,boolean isFinish);

  void goToOtherActivity(Class<?> cls, Bundle bundle, boolean isFinish);

  void goToOtherActivityForResult(Class<?> cls, Bundle bundle, int requestCode);

  void backForResult(Class<?> cls, Bundle bundle, int resultCode);

  void initViewWithIntentData(Bundle bundle);

  void showLoadingView();

  void closeLoadingView();

  void hideInputWindow();

  void showInputWindow(EditText mEditText);


  void showTipDialog(String content);

  void showTipDialog(String title, String content);

  void showConfirmDialog(String content);

  void showCancelDialog(String content);

}
