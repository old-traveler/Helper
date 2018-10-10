package com.hyc.helper.model;

import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.SpCacheHelper;

public class ConfigModel {

  public ConfigureBean getConfigInfo() {
    return SpCacheHelper.getClassFromSp(Constant.SP_CONFIG, ConfigureBean.class);
  }

  public void setConfigInfo(ConfigureBean configInfo) {
    SpCacheHelper.putClassIntoSp(Constant.SP_CONFIG, configInfo);
  }
}
