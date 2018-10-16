package com.hyc.helper.model;

import com.hyc.helper.bean.LostBean;
import com.hyc.helper.helper.RequestHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LostGoodsModel {

  public void getAllLostGoods(int page, Observer<LostBean> observer) {
    RequestHelper.getRequestApi().getLostAndFind(page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void getPersonalLost(String number,String code,int page,String userId,Observer<LostBean> observer){
    RequestHelper.getRequestApi().getUserLost(number,code,page,userId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }
}
