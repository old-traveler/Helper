package com.hyc.helper.helper;

import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.ConfigureBean;

public class ConfigureHelper {

  public static void init(ConfigureBean configureBean){
    if (configureBean == null){
      configureBean = SpCacheHelper.getClassFromSp(Constant.SP_CONFIG,ConfigureBean.class);
      if (configureBean == null){
        ToastHelper.toast("初始化失败，请打开网络后，重启应用");
        return;
      }
    }else {
      SpCacheHelper.putClassIntoSp(Constant.SP_CONFIG,configureBean);
    }
    DateHelper.DATE_OF_SCHOOL = configureBean.getDate();
  }

}
