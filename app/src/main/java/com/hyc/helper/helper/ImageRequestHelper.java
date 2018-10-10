package com.hyc.helper.helper;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hyc.helper.activity.PictureBrowsingActivity;
import com.hyc.helper.base.util.UiHelper;
import java.io.File;

public class ImageRequestHelper {

  public static void loadImage(Context context, String url, ImageView imageView) {
    if (url.endsWith("?")) {
      url = url.substring(0, url.length() - 2);
    }
    Glide.with(context)
        .load(Constant.BASE_IMAGE_URL + url)
        .apply(new RequestOptions().placeholder(UiHelper.getDefaultPlaceholder()))
        .into(imageView);
  }

  public static void loadImage(Context context, Uri uri, ImageView imageView) {
    Glide.with(context)
        .load(uri)
        .apply(new RequestOptions().placeholder(UiHelper.getDefaultPlaceholder()))
        .into(imageView);
  }

  public static void loadImage(Context context, int resId, ImageView imageView) {
    Glide.with(context)
        .load(resId)
        .apply(new RequestOptions().placeholder(UiHelper.getDefaultPlaceholder()))
        .into(imageView);
  }

  public static void loadHeadImage(Context context, String url, ImageView imageView) {
    if (url.endsWith("?")) {
      url = url.substring(0, url.length() - 2);
    }
    if (url.endsWith("gif")) {
      LogHelper.log(url);
    }
    Glide.with(context)
        .load(Constant.BASE_IMAGE_URL + url)
        .apply(new RequestOptions().circleCrop().placeholder(UiHelper.getDefaultPlaceholder()))
        .into(imageView);
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
