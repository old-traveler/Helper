package com.hyc.helper.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.interfaces.IBaseFragment;

public abstract class BaseFragment extends Fragment implements View.OnClickListener ,IBaseFragment {

  protected BaseActivity mBaseActivity;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBaseActivity = (BaseActivity) getActivity();
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater
      , ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutId(),container,false);
    initLayoutView(view);
    return view;
  }

  protected abstract void initLayoutView(View view);

  protected abstract int getLayoutId();

  @Override public void onClick(View view) {

  }

  @Override public void goToOtherActivity(Class<?> cls,boolean isFinish) {
    mBaseActivity.goToOtherActivity(cls,isFinish);
  }

  @Override public void goToOtherActivity(Class<?> cls, Bundle bundle,boolean isFinish) {
    mBaseActivity.goToOtherActivity(cls,bundle,isFinish);
  }

  @Override public void goToOtherActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
    mBaseActivity.goToOtherActivityForResult(cls,bundle,requestCode);
  }

  @Override public void backForResult(Class<?> cls, Bundle bundle, int resultCode) {
    mBaseActivity.backForResult(cls,bundle,resultCode);
  }

  @Override public void showLoadingView() {
    mBaseActivity.showLoadingView();
  }

  @Override public void closeLoadingView() {
    mBaseActivity.closeLoadingView();
  }

  @Override public void hideInputWindow() {
    mBaseActivity.hideInputWindow();
  }

  @Override public void showInputWindow(EditText mEditText) {
    mBaseActivity.showInputWindow(mEditText);
  }
}
