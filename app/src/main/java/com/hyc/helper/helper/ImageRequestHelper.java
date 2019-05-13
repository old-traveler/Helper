package com.hyc.helper.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.model.ImageModel;
import io.reactivex.disposables.Disposable;
import java.io.File;

public class ImageRequestHelper {

  public static Disposable loadImage(Context context, String url, ImageView imageView) {
    loadImageByUrl(context, url, imageView);
    return new ImageModel().getBigImageLoadRecord(url,
        bean -> {
          if (bean != null && FileHelper.fileIsExist(bean.getFilePath())) {
            Glide.with(context)
                .load(new File(bean.getFilePath()))
                .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
                .into(imageView);
          }
        }, throwable ->{

        });
  }

  public static void loadNotCropImage(Context context, String url, ImageView imageView) {
    if (url.endsWith("?")) {
      url = url.substring(0, url.length() - 2);
    }

    Glide.with(context)
        .asDrawable()
        .load(Constant.BASE_IMAGE_URL + url)
        .into(new SimpleTarget<Drawable>() {
          @Override
          public void onResourceReady(@NonNull Drawable resource,
              @Nullable Transition<? super Drawable> transition) {
            imageView.setImageDrawable(resource);
          }
        });
  }

  public static void loadImageByUrl(Context context, String url, ImageView imageView) {
    if (url.endsWith("?")) {
      url = url.substring(0, url.length() - 2);
    }

    Glide.with(context)
        .load(Constant.BASE_IMAGE_URL + url)
        .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
        .into(imageView);
  }

  public static void loadOtherImage(Context context, String url, ImageView imageView) {
    if (url.equals(imageView.getTag(R.id.data))){
      return;
    }
    imageView.setTag(R.id.data,url);
    Glide.with(context)
        .load(url)
        .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
        .into(imageView);
  }

  public static void loadImage(Context context, Uri uri, ImageView imageView) {
    Glide.with(context)
        .load(uri)
        .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
        .into(imageView);
  }

  public static void loadImage(Context context, int resId, ImageView imageView) {
    Glide.with(context)
        .load(resId)
        .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
        .into(imageView);
  }

  public static void loadHeadImage(Context context, String url, ImageView imageView) {
    if (TextUtils.isEmpty(url)) {
      return;
    }
    if (url.equals(imageView.getTag(R.id.data))){
      return;
    }
    imageView.setTag(R.id.data,url);
    Glide.with(context)
        .load(Constant.BASE_IMAGE_URL + url)
        .apply(new RequestOptions().circleCrop().placeholder(R.drawable.bg_chat_placeholder))
        .into(imageView);
  }

  public static void loadBigHeadImage(Context context, String url, ImageView imageView) {
    url = url.replace("_thumb", "");
    loadHeadImage(context, url, imageView);
  }

  public static void loadImageAsFile(Context context, String url, SimpleTarget<File> simpleTarget) {
    if (url.endsWith("?")) {
      url = url.substring(0, url.length() - 2);
    }
    Glide.with(context)
        .asFile()
        .load(Constant.BASE_IMAGE_URL + url)
        .into(simpleTarget);
  }

  public static void loadOtherImageAsFile(Context context, String url,
      SimpleTarget<File> simpleTarget) {
    Glide.with(context)
        .asFile()
        .load(url)
        .into(simpleTarget);
  }

  public static void loadOralImageAsFile(Context context, String url,
      SimpleTarget<File> simpleTarget) {
    url = url.replace("_thumb", "");
    loadImageAsFile(context, url, simpleTarget);
  }

  public static void loadGifFromFile(Context context, File file, ImageView imageView) {
    Glide.with(context)
        .asGif()
        .load(file)
        .into(imageView);
  }
}
