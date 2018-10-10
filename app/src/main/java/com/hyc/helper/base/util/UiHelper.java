package com.hyc.helper.base.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.hyc.helper.HelperApplication;
import com.hyc.helper.R;
import com.hyc.helper.bean.ImageSizeBean;
import com.hyc.helper.util.DensityUtil;

public class UiHelper {

  public static View inflater(int resId, ViewGroup parent) {
    return LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
  }

  public static int[] getIntegerArrays(int resId) {
    return HelperApplication.getContext().getResources().getIntArray(resId);
  }

  public static float getTextSize(int resId) {
    return HelperApplication.getContext().getResources().getDimension(resId);
  }

  public static View inflater(Context context, int resId, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(resId, parent);
  }

  public static String getString(int resId) {
    return HelperApplication.getContext().getResources().getString(resId);
  }

  public static int getColor(int resId) {
    return HelperApplication.getContext().getResources().getColor(resId);
  }

  public static LinearLayout getLinearLayout(Context context) {
    LinearLayout linearLayout = new LinearLayout(context);
    linearLayout.setOrientation(LinearLayout.VERTICAL);
    return linearLayout;
  }

  public static DisplayMetrics getDisplayMetrics() {
    return HelperApplication.getContext().getResources().getDisplayMetrics();
  }

  public static String[] getStringArrays(int resId) {
    return HelperApplication.getContext().getResources().getStringArray(resId);
  }

  public static Drawable getDefaultPlaceholder() {
    return HelperApplication.getContext().getResources().getDrawable(R.drawable.bg_placeholder);
  }

  /**
   * 设置某个View的margin
   *
   * @param view 需要设置的view
   * @param isDp 需要设置的数值是否为DP
   * @param left 左边距
   * @param right 右边距
   * @param top 上边距
   * @param bottom 下边距
   */
  public static ViewGroup.LayoutParams setViewMargin(View view
      , boolean isDp, int left, int right, int top, int bottom) {
    if (view == null) {
      return null;
    }

    int leftPx = left;
    int rightPx = right;
    int topPx = top;
    int bottomPx = bottom;
    ViewGroup.LayoutParams params = view.getLayoutParams();
    ViewGroup.MarginLayoutParams marginParams = null;
    //获取view的margin设置参数
    if (params instanceof ViewGroup.MarginLayoutParams) {
      marginParams = (ViewGroup.MarginLayoutParams) params;
    } else {
      //不存在时创建一个新的参数
      marginParams = new ViewGroup.MarginLayoutParams(params);
    }

    //根据DP与PX转换计算值
    if (isDp) {
      leftPx = DensityUtil.dip2px(left);
      rightPx = DensityUtil.dip2px(right);
      topPx = DensityUtil.dip2px(top);
      bottomPx = DensityUtil.dip2px(bottom);
    }
    //设置margin
    marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
    view.setLayoutParams(marginParams);
    view.requestLayout();
    return marginParams;
  }

  public static void setViewSize(View view, ImageSizeBean viewSize) {
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if (params == null) {
      params = new ViewGroup.LayoutParams(viewSize.getWidth(), viewSize.getHeight());
    }
    params.width = viewSize.getWidth();
    params.height = viewSize.getHeight();
    view.setLayoutParams(params);
    view.requestLayout();
  }

  public static boolean isLongImage(ImageSizeBean bean) {
    float screenScale = DensityUtil.getScreenHeight() * 1.0f / DensityUtil.getScreenWidth();
    float imageScale = bean.getHeight() * 1.0f / bean.getWidth();
    return imageScale > screenScale;
  }
}
