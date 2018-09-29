package com.hyc.helper.model;

import com.hyc.helper.bean.LostBean;
import com.hyc.helper.helper.RequestHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LostGoodsModel {

  public void getAllLostGoods(int page,Observer<LostBean> observer){
    RequestHelper.getRequestApi().getLostAndFind(page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

}
