package com.hyc.helper.base.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.hyc.helper.R;

public class LoadingDialog extends Dialog {

  private LoadingDialog(Context context, int themeResId) {
    super(context, themeResId);
  }


  public static class Builder {

    private Context context;
    private boolean isCancelable = false;
    private boolean isCancelOutside = false;

    public Builder(Context context) {
      this.context = context;
    }


    /**
     * 设置是否可以按返回键取消
     */

    public Builder setCancelable(boolean isCancelable) {
      this.isCancelable = isCancelable;
      return this;
    }

    /**
     * 设置是否可以取消
     */
    public Builder setCancelOutside(boolean isCancelOutside) {
      this.isCancelOutside = isCancelOutside;
      return this;
    }

    public LoadingDialog create() {
      LayoutInflater inflater = LayoutInflater.from(context);
      @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.lib_dialog_loading, null);
      LoadingDialog loadingDialog = new LoadingDialog(context, R.style.loading_dialog_style);
      loadingDialog.setContentView(view);
      loadingDialog.setCancelable(isCancelable);
      loadingDialog.setCanceledOnTouchOutside(isCancelOutside);
      return loadingDialog;
    }
  }
}