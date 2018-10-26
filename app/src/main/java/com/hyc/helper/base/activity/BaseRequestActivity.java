package com.hyc.helper.base.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.hyc.helper.R;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.LogHelper;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.Objects;

public abstract class BaseRequestActivity<T extends BaseRequestBean> extends BaseActivity
    implements Observer<T> {

  private Disposable disposable;

  private TextView tvLoadFail;

  private boolean atCreateRequest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    atCreateRequest = isOnCreateRequest();
    tvLoadFail = findViewById(R.id.tv_fail_tip);
    if (tvLoadFail != null){
      tvLoadFail.setVisibility(View.GONE);
      tvLoadFail.setOnClickListener(view -> startRequest());
    }
    if (atCreateRequest) {
      startRequest();
    }
  }

  public void startRequest() {
    if (!validationInput()) {
      return;
    }
    showLoadingView();
    if (!requestDataFromDb()) {
      requestDataFromApi();
    }
  }

  public void startRequestApi() {
    if (validationInput()) {
      showLoadingView();
      requestDataFromApi();
    }
  }

  protected boolean requestDataFromDb() {
    return false;
  }

  protected boolean validationInput() {
    return true;
  }

  protected abstract void requestDataFromApi();

  protected abstract void onSuccessGetData(T t);

  protected void onFailGetData(Throwable e) {
    if (atCreateRequest){
      tvLoadFail.setVisibility(View.VISIBLE);
    }
    ToastHelper.toast(e.getMessage());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    dispose();
  }

  public void dispose() {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  @Override
  public void onSubscribe(Disposable d) {
    this.disposable = d;
  }

  @Override
  public void onNext(T t) {
    if (t == null) {
      LogHelper.log("数据为空");
    } else if (t.getCode() == Constant.REQUEST_SUCCESS) {
      if (tvLoadFail != null){
        tvLoadFail.setVisibility(View.GONE);
      }
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
