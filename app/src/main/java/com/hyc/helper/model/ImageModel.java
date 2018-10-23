package com.hyc.helper.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import com.hyc.helper.bean.BigImageLoadRecordBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.DbInsertHelper;
import com.hyc.helper.helper.DbSearchHelper;
import com.hyc.helper.helper.FileHelper;
import com.hyc.helper.helper.UploadImageObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
