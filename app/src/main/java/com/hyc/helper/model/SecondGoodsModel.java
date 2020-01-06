package com.hyc.helper.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.GoodsDetailBean;
import com.hyc.helper.bean.SecondHandBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.FileHelper;
import com.hyc.helper.helper.RequestHelper;
import com.hyc.helper.helper.UploadImageObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import top.zibin.luban.Luban;

public class SecondGoodsModel {

  public void getSecondMarketGoods(int page, Observer<SecondHandBean> observer) {
    RequestHelper.getRequestApi().getSecondHandMaker(page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void getPersonalMarket(String number, String code, int page, String userId,
      Observer<SecondHandBean> observer) {
    RequestHelper.getRequestApi().getUserSecondMaker(number, code, page, userId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void getGoodsDetailInfo(UserBean bean, String goodsId,
      Observer<GoodsDetailBean> observer) {
    RequestHelper.getRequestApi()
        .getGoodsDetailInfo(bean.getData().getStudentKH(), bean.getRemember_code_app(), goodsId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  @SuppressLint("CheckResult")
  public void publishMarketGoods(UserBean userBean, Map<String, String> map, List<String> images,
      Observer<BaseRequestBean> observer) {
    List<File> imageFile = new ArrayList<>(images.size());
    for (String image : images) {
      imageFile.add(new File(FileHelper.getFilePath((Context) observer, Uri.parse(image))));
    }
    UploadImageObserver uploadBeanObserver = new UploadImageObserver(images.size(),
        new UploadImageObserver.OnUploadImageListener() {
          @Override
          public void onSuccess(String image) {
            publishMarketGoods(userBean, map, image, observer);
          }

          @Override
          public void onFailure(Throwable e) {
            observer.onError(e);
          }
        });
    Flowable.just(imageFile)
        .observeOn(Schedulers.io())
        .map(uris -> Luban.with((BaseActivity) observer)
            .load(uris)
            .ignoreBy(100)
            .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
            .get())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(files -> FileHelper.uploadImage(userBean, "1", files, uploadBeanObserver),
            observer::onError);
  }

  private void publishMarketGoods(UserBean userBean, Map<String, String> map, String hidden,
      Observer<BaseRequestBean> observer) {
    map.put("hidden", hidden);
    RequestHelper.getRequestApi()
        .createTrade(userBean.getData().getStudentKH(), userBean.getRemember_code_app(), map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public Observable<BaseRequestBean> deleteGoods(UserBean userBean, String tradeId) {
    return RequestHelper.getRequestApi()
        .deleteTrade(userBean.getData().getStudentKH(), userBean.getRemember_code_app(), tradeId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Observable<SecondHandBean> searchSecondHand(String number, String code, String keyWord,
      int page) {
    return RequestHelper.getRequestApi().searchSecordGoods(number, code, page, keyWord)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
