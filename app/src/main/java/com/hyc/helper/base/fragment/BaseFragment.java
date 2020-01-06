package com.hyc.helper.base.fragment;

import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.interfaces.IBaseFragment;
import com.hyc.helper.helper.DisposableManager;
import com.hyc.helper.util.parrot.Parrot;
import io.reactivex.disposables.Disposable;

import static android.content.Context.INPUT_METHOD_SERVICE;

public abstract class BaseFragment extends Fragment implements View.OnClickListener, IBaseFragment {

  protected BaseActivity mBaseActivity;

  private DisposableManager disposableManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBaseActivity = (BaseActivity) getActivity();
    if (getArguments() != null) {
      Parrot.initParam(getArguments(), this);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater
      , ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(getLayoutId(), container, false);
    initLayoutView(view);
    return view;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mBaseActivity = null;
    cancelAllDisposable();
  }

  protected void cancelAllDisposable() {
    if (disposableManager != null) {
      disposableManager.cancelAllDisposable();
    }
  }

  public void addDisposable(Disposable disposable) {
    if (disposableManager == null) {
      disposableManager = new DisposableManager();
    }
    disposableManager.addDisposable(disposable);
  }

  protected abstract void initLayoutView(View view);

  protected abstract int getLayoutId();

  @Override
  public void onClick(View view) {

  }

  public void goToOtherActivity(Class<?> cls, boolean isFinish) {
    mBaseActivity.goToOtherActivity(cls, isFinish);
  }

  @Override
  public void goToOtherActivity(Class<?> cls, Bundle bundle, boolean isFinish) {
    mBaseActivity.goToOtherActivity(cls, bundle, isFinish);
  }

  public void goToOtherActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
    mBaseActivity.goToOtherActivityForResult(cls, bundle, requestCode);
  }

  public void goToOtherActivityForResult(Class<?> cls, int requestCode) {
    mBaseActivity.goToOtherActivityForResult(cls, requestCode);
  }

  public void backForResult(Class<?> cls, Bundle bundle, int resultCode) {
    mBaseActivity.backForResult(cls, bundle, resultCode);
  }

  @Override
  public void showLoadingView() {
    mBaseActivity.showLoadingView();
  }

  @Override
  public void closeLoadingView() {
    mBaseActivity.closeLoadingView();
  }

  @Override
  public void hideInputWindow() {
    View focus = getView() == null ? null : getView().findFocus();
    if (focus != null && getActivity() != null) {
      IBinder focusBinder = focus.getWindowToken();
      InputMethodManager manager =
          (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
      if (focusBinder != null && manager != null) {
        manager.hideSoftInputFromWindow(focusBinder, InputMethodManager.HIDE_NOT_ALWAYS);
      }
    }
  }

  @Override
  public void showInputWindow(EditText mEditText) {
    mBaseActivity.showInputWindow(mEditText);
  }
}
