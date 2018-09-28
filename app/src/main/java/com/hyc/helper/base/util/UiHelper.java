package com.hyc.helper.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.hyc.helper.HelperApplication;
import com.hyc.helper.R;
import java.lang.ref.WeakReference;

public class UiHelper {


  public static View inflater(int resId,ViewGroup parent){
    return LayoutInflater.from(parent.getContext()).inflate(resId,parent,false);
  }

  public static int[] getIntegerArrays(int resId){
    return HelperApplication.getContext().getResources().getIntArray(resId);
  }

  public static float getTextSize(int resId){
    return HelperApplication.getContext().getResources().getDimension(resId);
  }

  public static View inflater(Context context,int resId,ViewGroup parent){
    return LayoutInflater.from(context).inflate(resId,parent);
  }

  public static String getString(int resId){
    return HelperApplication.getContext().getResources().getString(resId);
  }

  public static int getColor(int resId){
    return HelperApplication.getContext().getResources().getColor(resId);
  }

  public static LinearLayout getLinearLayout(Context context){
    LinearLayout linearLayout = new LinearLayout(context);
    linearLayout.setOrientation(LinearLayout.VERTICAL);
    return linearLayout;
  }

  public static DisplayMetrics getDisplayMetrics() {
    return HelperApplication.getContext().getResources().getDisplayMetrics();
  }

  public static String[] getStringArrays(int resId){
    return HelperApplication.getContext().getResources().getStringArray(resId);
  }

  public static Drawable getDefaultPlaceholder(){
    return HelperApplication.getContext().getResources().getDrawable(R.drawable.bg_placeholder);
  }


  //public static void setViewSize(View view,ImageSizeBean viewSize) {
  //  ViewGroup.LayoutParams params = view.getLayoutParams();
  //  if (params == null){
  //    params = new ViewGroup.LayoutParams(viewSize.getWidth(),viewSize.getHeight());
  //  }
  //  params.width = viewSize.getWidth();
  //  params.height = viewSize.getHeight();
  //  view.setLayoutParams(params);
  //  view.requestLayout();
  //}
  //
  //public static boolean isLongImage(ImageSizeBean bean){
  //  float screenScale = DensityUtil.getScreenHeight()*1.0f/DensityUtil.getScreenWidth();
  //  float imageScale = bean.getHeight()*1.0f/bean.getWidth();
  //  return imageScale > screenScale;
  //}

}
