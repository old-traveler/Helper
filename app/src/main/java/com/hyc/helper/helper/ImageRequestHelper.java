package com.hyc.helper.helper;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hyc.helper.base.util.UiHelper;

public class ImageRequestHelper {

  public static void loadImage(Context context,String url,ImageView imageView){
    if (url.endsWith("?")){
      url = url.substring(0,url.length()-2);
    }
    Glide.with(context)
        .load(Constant.BASE_IMAGE_URL+url)
        .apply(new RequestOptions().placeholder(UiHelper.getDefaultPlaceholder()))
        .into(imageView);
  }

  public static void loadHeadImage(Context context,String url,ImageView imageView){
    if (url.endsWith("?")){
      url = url.substring(0,url.length()-2);
    }
    Glide.with(context)
        .load(Constant.BASE_IMAGE_URL+url)
        .apply(new RequestOptions().circleCrop().placeholder(UiHelper.getDefaultPlaceholder()))
        .into(imageView);
  }


}
