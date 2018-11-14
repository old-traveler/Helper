package com.hyc.helper.base.interfaces;

import android.os.Bundle;
import android.widget.EditText;

public interface IBaseFragment {


  void goToOtherActivity(Class<?> cls, Bundle bundle, boolean isFinish);

  void showLoadingView();

  void closeLoadingView();

  void hideInputWindow();

  void showInputWindow(EditText mEditText);



}
