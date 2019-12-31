package com.hyc.helper.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.FileHelper;
import com.hyc.helper.helper.RequestHelper;
import com.hyc.helper.helper.UploadImageObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import top.zibin.luban.Luban;

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

  public void getPersonalStatement(String number,int page,String userId,Observer<StatementBean> observer){
    RequestHelper.getRequestApi().getUserStatement(number,page,userId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  @SuppressLint("CheckResult")
  public void publishStatement(UserBean userBean, String content, List<String> images,
      Observer<BaseRequestBean> observer) {
    if (images.size() == 0) {
      publishStatement(userBean, content, observer);
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
            publishStatement(userBean, content, image, observer);
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
        .subscribe(files -> FileHelper.uploadImage(userBean, "0", files, uploadBeanObserver),
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

  public Observable<BaseRequestBean> deleteStatement(UserBean userBean, String momend_id) {
    return RequestHelper.getRequestApi()
        .deleteStatement(userBean.getData().getStudentKH(), userBean.getRemember_code_app(),
            momend_id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Observable<StatementBean> fetchInteractiveStatement(String number,String code ,int page){
    return RequestHelper.getRequestApi()
        .fetchInteractiveStatement(number,code,page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
