package com.hyc.helper.model;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.hyc.helper.HelperApplication;
import com.hyc.helper.activity.MainActivity;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.ImageUploadBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.FileHelper;
import com.hyc.helper.helper.RequestHelper;
import com.hyc.helper.helper.UploadImageObserver;
import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class StatementModel {

  public void getStatementByPage(int page, String number, Observer<StatementBean> observer) {
    RequestHelper.getRequestApi().getStatement(number, page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public Disposable likeStatement(UserBean userBean, String statementId) {
    return RequestHelper.getRequestApi().likeStatement(
        userBean.getData().getStudentKH(), userBean.getRemember_code_app(), statementId)
        .subscribeOn(Schedulers.io())
        .subscribe();
  }

  public Disposable commentStatement(String number, String code, String comment, String monment_id,
      Consumer<BaseRequestBean> consumer) {
    return RequestHelper.getRequestApi().commentStatement(number, code, comment, monment_id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(consumer);
  }

  @SuppressLint("CheckResult")
  public void publishStatement(UserBean userBean, String content, List<String> images,
      Observer<BaseRequestBean> observer) {
    if (images.size() == 0) {
      publishStatement(userBean, content, observer);
      return;
    }
    List<Uri> imageUri = new ArrayList<>(images.size());
    for (String image : images) {
      imageUri.add(Uri.parse(image));
    }
    UploadImageObserver uploadBeanObserver = new UploadImageObserver(images.size(),
        new UploadImageObserver.OnUploadImageListener() {
          @Override
          public void onSuccess(String image) {
            publishStatement(userBean, content, image, observer);
          }

          @Override
          public void onFailure(Throwable e) {
            observer.onError(e);
          }
        });
    Flowable.just(imageUri)
        .observeOn(Schedulers.io())
        .map(list -> Luban.with(HelperApplication.getContext())
            .load(imageUri)
            .ignoreBy(100)
            .filter(path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")))
            .get())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(files -> FileHelper.uploadImage(userBean, files, uploadBeanObserver),
            observer::onError);
  }

  public void publishStatement(UserBean userBean, String content,
      Observer<BaseRequestBean> observer) {
    publishStatement(userBean, content, "", observer);
  }

  private void publishStatement(UserBean userBean, String content, String hidden,
      Observer<BaseRequestBean> observer) {
    RequestHelper.getRequestApi()
        .publishStatement(userBean.getData().getStudentKH(), userBean.getRemember_code_app(),
            content,
            String.valueOf(userBean.getData().getUser_id()), hidden)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }
}
