package com.hyc.helper.activity;

import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.helper.ConfigureHelper;
import com.hyc.helper.helper.RequestHelper;
import com.hyc.helper.model.UserModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.lang.ref.WeakReference;

public class SplashActivity extends BaseRequestActivity<ConfigureBean> {

  private Handler handler = new SplashHandler(new WeakReference<>(this));

  public static class SplashHandler extends Handler{
    private WeakReference<SplashActivity> weakReference;

    SplashHandler(WeakReference<SplashActivity> weakReference){
      this.weakReference = weakReference;
    }

    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (weakReference.get()!=null){
        weakReference.get().goToNextActivity(null);
      }
    }
  }


  private Message message = new Message();

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
    handler.sendMessageDelayed(message,5000);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    handler.removeMessages(1);
  }

  @Override
  protected void onSuccessGetData(ConfigureBean configureBean) {
    goToNextActivity(configureBean);
  }

  @Override
  protected void onFailGetData(Throwable e) {
    super.onFailGetData(e);
    goToNextActivity(null);
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    message.what = 1;
  }

  @Override
  public void showLoadingView() {

  }

  @Override
  public void closeLoadingView() {

  }

  public void goToNextActivity(ConfigureBean configureBean){
    ConfigureHelper.init(configureBean);
    dispose();
    handler.removeMessages(1);
    if (new UserModel().getCurUserInfo()!=null){
      goToOtherActivity(MainActivity.class,true);
    }else {
      goToOtherActivity(LoginActivity.class,true);
    }
  }

}