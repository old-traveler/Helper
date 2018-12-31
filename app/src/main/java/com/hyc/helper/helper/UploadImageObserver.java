package com.hyc.helper.helper;

import android.text.TextUtils;
import com.hyc.helper.bean.ImageUploadBean;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public class UploadImageObserver implements Observer<ImageUploadBean> {

  private int size;

  private List<ImageUploadBean> list;

  private OnUploadImageListener listener;
  private Disposable disposable;

  public interface OnUploadImageListener {
    void onSuccess(String image);

    void onFailure(Throwable e);
  }

  public UploadImageObserver(int size, OnUploadImageListener imageListener) {
    this.size = size;
    list = new ArrayList<>(size);
    this.listener = imageListener;
  }

  @Override
  public void onSubscribe(Disposable d) {
    disposable = d;
  }

  @Override
  public void onNext(ImageUploadBean imageUploadBean) {
    if (imageUploadBean.code == Constant.REQUEST_SUCCESS) {
      list.add(imageUploadBean);
      if (list.size() == size) {
        listener.onSuccess(getImageUrlString());
      }
    } else {
      disposable.dispose();
      listener.onFailure(
          new Throwable(TextUtils.isEmpty(imageUploadBean.msg) ? "图片上传失败" : imageUploadBean.msg));
    }
  }

  @Override
  public void onError(Throwable e) {
    listener.onFailure(e);
    if (disposable != null) {
      disposable.dispose();
    }
  }

  @Override
  public void onComplete() {

  }

  private String getImageUrlString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (ImageUploadBean imageUploadBean : list) {
      stringBuilder.append("//");
      stringBuilder.append(imageUploadBean.getData());
    }
    return stringBuilder.toString();
  }
}
