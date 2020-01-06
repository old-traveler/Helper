package com.hyc.helper.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.LostBean;
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

public class LostGoodsModel {

  public void getAllLostGoods(int page, Observer<LostBean> observer) {
    RequestHelper.getRequestApi().getLostAndFind(page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void getPersonalLost(String number, String code, int page, String userId,
      Observer<LostBean> observer) {
    RequestHelper.getRequestApi().getUserLost(number, code, page, userId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  @SuppressLint("CheckResult")
  public void publishLost(UserBean userBean, Map<String, String> map, List<String> images,
      Observer<BaseRequestBean> observer) {
    if (images.size() == 0) {
      publishLost(userBean, map, "", observer);
      return;
    }
    List<File> imageFile = new ArrayList<>(images.size());
    for (String image : images) {
      imageFile.add(new File(FileHelper.getFilePath((Context) observer, Uri.parse(image))));
    }
    UploadImageObserver uploadBeanObserver = new UploadImageObserver(images.size(),
        new UploadImageObserver.OnUploadImageListener() {
          @Override
          public void onSuccess(String image) {
            publishLost(userBean, map, image, observer);
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
        .subscribe(files -> FileHelper.uploadImage(userBean, "2", files, uploadBeanObserver),
            observer::onError);
  }

  private void publishLost(UserBean userBean, Map<String, String> map, String hidden,
      Observer<BaseRequestBean> observer) {
    map.put("hidden", hidden);
    RequestHelper.getRequestApi()
        .createLoses(userBean.getData().getStudentKH(), userBean.getRemember_code_app(), map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public Observable<BaseRequestBean> deleteLost(UserBean userBean, String id) {
    return RequestHelper.getRequestApi()
        .deleteLoses(userBean.getData().getStudentKH(), userBean.getRemember_code_app(), id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Observable<LostBean> searchLostAndFind(String number, String code, String keyWord,
      int page) {
    return RequestHelper.getRequestApi()
        .searchLostAndFind(number, code, page, keyWord)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
