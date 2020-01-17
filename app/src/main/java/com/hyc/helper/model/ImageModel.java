package com.hyc.helper.model;

import com.hyc.helper.bean.BigImageLoadRecordBean;
import com.hyc.helper.helper.DbInsertHelper;
import com.hyc.helper.helper.DbSearchHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ImageModel {

  public void saveBigImageLoadRecord(BigImageLoadRecordBean bean) {
    DbInsertHelper.insertBigImageLoadRecord(bean)
        .subscribeOn(Schedulers.io())
        .subscribe();
  }

  public Disposable getBigImageLoadRecord(String originUrl,
      Consumer<BigImageLoadRecordBean> consumer,
      Consumer<Throwable> throwableConsumer) {
    return DbSearchHelper.searchBigImageLoadRecord(originUrl)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(consumer, throwableConsumer);
  }
}
