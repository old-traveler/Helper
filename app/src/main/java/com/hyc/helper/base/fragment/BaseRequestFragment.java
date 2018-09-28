package com.hyc.helper.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.LogHelper;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseRequestFragment<T extends BaseRequestBean> extends BaseFragment implements Observer<T> {

  private Disposable disposable;
  private boolean isNeedLoad = false;

  @Override public View onCreateView(@NonNull LayoutInflater inflater
      , ViewGroup container, Bundle savedInstanceState) {
    View view = super.onCreateView(inflater,container,savedInstanceState);
    if (isOnCreateRequest()){
      if (getUserVisibleHint()){
        startRequest();
      }else {
        isNeedLoad = true;
      }
    }
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    if (isNeedLoad){
      isNeedLoad = false;
      startRequest();
    }
  }

  public void startRequest(){
    if (!validationInput()){
      return;
    }
    showLoadingView();
    if (!requestDataFromDb()){
      requestDataFromApi();
    }
  }

  public void startRequestApi(){
    if (validationInput()){
      showLoadingView();
      requestDataFromApi();
    }
  }
  protected boolean requestDataFromDb(){
    return false;
  }


  protected boolean validationInput(){
    return true;
  }

  protected abstract void requestDataFromApi();

  protected abstract void onSuccessGetData(T t);

  protected void onFailGetData(Throwable e){
    ToastHelper.toast(e.getMessage());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (disposable!=null && !disposable.isDisposed()){
      disposable.dispose();
    }
  }

  @Override
  public void onSubscribe(Disposable d) {
    this.disposable = d;
  }

  @Override
  public void onNext(T t) {
    if(t == null){
      LogHelper.log("数据为空");
    } else if (t.getCode() == Constant.REQUEST_SUCCESS ){
      onSuccessGetData(t);
    } else if (t.getCode() == Constant.NEED_API_DATA) {
      disposable.dispose();
      requestDataFromApi();
    } else if (!TextUtils.isEmpty(t.getMsg())) {
      ToastHelper.toast(t.getMsg());
    } else {
      ToastHelper.toast(String.valueOf(t.getCode()));
    }
  }

  @Override
  public void onError(Throwable e) {
    onFailGetData(e);
    closeLoadingView();
  }

  @Override
  public void onComplete() {
    closeLoadingView();
  }

  public boolean isOnCreateRequest() {
    return true;
  }

}
