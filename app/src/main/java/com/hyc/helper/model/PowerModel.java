package com.hyc.helper.model;

import com.hyc.helper.bean.PowerBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.RequestHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PowerModel {

  public void queryPowerInfo(String part, String locate, String room,
      UserBean.DataBean dataBean, String enc, Observer<PowerBean> observer) {
    RequestHelper.getRequestApi()
        .getPowerInfo(part, locate, room, dataBean.getStudentKH(), dataBean.getRemember_code_app(),
            enc)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }
}
