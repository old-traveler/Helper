package com.hyc.helper.model;

import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.RequestHelper;
import com.hyc.helper.helper.SpCacheHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.lang.ref.WeakReference;

public class UserModel {

  private static WeakReference<UserBean> curUserBean;

  public void login(String account, String password, Observer<UserBean> observer) {
    RequestHelper.getRequestApi().login(account, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void cacheUserInfo(UserBean userBean) {
    SpCacheHelper.putClassIntoSp("user", userBean);
    curUserBean = new WeakReference<>(userBean);
  }

  public UserBean getCurUserInfo() {
    if (curUserBean == null || curUserBean.get() == null) {
      UserBean userBean = SpCacheHelper.getClassFromSp("user", UserBean.class);
      curUserBean = new WeakReference<>(userBean);
    }
    return curUserBean.get();
  }

  public String getStudentId() {
    return getCurUserInfo().getData().getStudentKH();
  }
}
