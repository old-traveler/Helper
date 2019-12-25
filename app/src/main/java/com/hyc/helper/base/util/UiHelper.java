package com.hyc.helper.base.util;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hyc.helper.HelperApplication;
import com.hyc.helper.R;
import com.hyc.helper.activity.WebActivity;
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

  public static String getString(int resId, int value) {
    String str = getString(resId);
    return String.format(str, value);
  }

  public static int getColor(int resId) {
    return HelperApplication.getContext().getResources().getColor(resId);
  }


  public static DisplayMetrics getDisplayMetrics() {
    return HelperApplication.getContext().getResources().getDisplayMetrics();
  }

  public static String[] getStringArrays(int resId) {
    return HelperApplication.getContext().getResources().getStringArray(resId);
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
    ViewGroup.MarginLayoutParams marginParams;
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

  public static SpannableStringBuilder getWebLinkStyle(CharSequence text, Context context) {
    if (text instanceof Spannable) {
      int end = text.length();
      Spannable sp = (Spannable) text;
      URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
      SpannableStringBuilder style = new SpannableStringBuilder(text);
      style.clearSpans();
      for (URLSpan urlSpan : urls) {
        ClickableSpan myURLSpan = new ClickableSpan() {
          @Override
          public void onClick(@NonNull View view) {
            if (urlSpan.getURL().startsWith("http")) {
              WebActivity.startWebBrowsing(context, urlSpan.getURL(), "");
            } else {
              String number = urlSpan.getURL();
              if (number.contains(":")) {
                number = number.split(":")[1];
              }
              showBottomSheetDialog(context, number);
            }
          }
        };
        style.setSpan(myURLSpan, sp.getSpanStart(urlSpan),
            sp.getSpanEnd(urlSpan),
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
      }
      return style;
    }
    return null;
  }

  public static void showBottomSheetDialog(Context context, final String number) {
    BottomSheetDialog dialog = new BottomSheetDialog(context);
    @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom, null);
    TextView tvTitle = dialogView.findViewById(R.id.tv_title);
    tvTitle.setText(String.format("%s\n可能是一个电话号码或者其他联系方式，你可以", number));
    TextView tvCall = dialogView.findViewById(R.id.tv_call);
    tvCall.setOnClickListener(view -> {
      Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
      context.startActivity(dialIntent);
      dialog.dismiss();
    });
    TextView tvCopty = dialogView.findViewById(R.id.tv_copy);
    tvCopty.setOnClickListener(view -> {
      ClipboardManager copy =
          (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      assert copy != null;
      copy.setText(number);
      dialog.dismiss();
      ToastHelper.toast("已复制到剪切板");
    });
    TextView tvCancel = dialogView.findViewById(R.id.tv_cancel);
    tvCancel.setOnClickListener(view -> dialog.dismiss());
    dialog.setContentView(dialogView);
    dialog.show();
  }

  public static void initLinkTextView(TextView textView,Context context){
    SpannableStringBuilder spannableStringBuilder = getWebLinkStyle(textView.getText(),context);
    if (spannableStringBuilder != null){
      textView.setText(spannableStringBuilder);
    }
  }
}
