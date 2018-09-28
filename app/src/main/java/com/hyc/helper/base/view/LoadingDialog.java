package com.hyc.helper.base.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.hyc.helper.R;

public class LoadingDialog extends Dialog {

  private LoadingDialog(Context context, int themeResId) {
    super(context, themeResId);
  }

  public static class Builder{

    private Context context;
    private String message;
    private boolean isCancelable=false;
    private boolean isCancelOutside=false;


    public Builder(Context context) {
      this.context = context;
    }

    /**
     * 设置提示信息
     * @param message
     * @return
     */

    public Builder setMessage(String message){
      this.message=message;
      return this;
    }

    /**
     * 设置是否可以按返回键取消
     * @param isCancelable
     * @return
     */

    public Builder setCancelable(boolean isCancelable){
      this.isCancelable=isCancelable;
      return this;
    }

    /**
     * 设置是否可以取消
     * @param isCancelOutside
     * @return
     */
    public Builder setCancelOutside(boolean isCancelOutside){
      this.isCancelOutside=isCancelOutside;
      return this;
    }

    public LoadingDialog create(){
      LayoutInflater inflater = LayoutInflater.from(context);
      View view=inflater.inflate(R.layout.lib_dialog_loading,null);
      LoadingDialog loadingDailog=new LoadingDialog(context, R.style.loading_dialog_style);
      loadingDailog.setContentView(view);
      loadingDailog.setCancelable(isCancelable);
      loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
      return loadingDailog;
    }
  }
}