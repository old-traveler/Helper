package com.hyc.helper.model;

import android.annotation.SuppressLint;
import com.hyc.helper.bean.BigImageLoadRecordBean;
import com.hyc.helper.helper.DbInsertHelper;
import com.hyc.helper.helper.DbSearchHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ImageModel {

  public void saveBigImageLoadRecord(BigImageLoadRecordBean bean) {
    DbInsertHelper.insertBigImageLoadRecord(bean)
        .subscribeOn(Schedulers.io())
        .subscribe();
  }

  @SuppressLint("CheckResult")
  public void getBigImageLoadRecord(String originUrl, Consumer<BigImageLoadRecordBean> consumer,
      Consumer<Throwable> throwableConsumer) {
    DbSearchHelper.searchBigImageLoadRecord(originUrl)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(consumer, throwableConsumer);
  }
}
