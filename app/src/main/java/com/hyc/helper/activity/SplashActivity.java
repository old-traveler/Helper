package com.hyc.helper.activity;

import android.os.Handler;
import android.os.Bundle;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.helper.ConfigureHelper;
import com.hyc.helper.helper.RequestHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseRequestActivity<ConfigureBean> {

  private Handler handler = new Handler();
  private Runnable callback;


  @Override
  protected int getContentViewId() {
    return R.layout.activity_splash;
  }

  @Override
  protected void requestDataFromApi() {
    RequestHelper.getRequestApi().getConfigure()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this);
    callback = this::finish;
    handler.postDelayed(callback, 5000);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    handler.removeCallbacks(callback);
  }

  @Override
  protected void onSuccessGetData(ConfigureBean configureBean) {
    ConfigureHelper.init(configureBean);
    finish();
  }

  @Override
  protected void onFailGetData(Throwable e) {
    super.onFailGetData(e);
    ConfigureHelper.init(null);
    finish();
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {

  }

  @Override
  public void showLoadingView() {

  }

  @Override
  public void closeLoadingView() {

  }
}
