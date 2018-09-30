package com.hyc.helper.helper;

import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.model.ConfigModel;

public class ConfigureHelper {

  public static void init(ConfigureBean configureBean){
    ConfigModel configModel = new ConfigModel();
    ConfigureBean preConfigure = configModel.getConfigInfo();
    if (preConfigure == null){
      if (configureBean != null){
        DateHelper.DATE_OF_SCHOOL = configureBean.getDate();
        configModel.setConfigInfo(configureBean);
      }
    }else {
      if (configureBean == null){
        DateHelper.DATE_OF_SCHOOL = preConfigure.getDate();
      }else {
        long preUpdateTime = Long.parseLong(preConfigure.getUpdate_time());
        long curUpdateTime = Long.parseLong(configureBean.getUpdate_time());
        if (curUpdateTime>preUpdateTime){
          configModel.setConfigInfo(configureBean);
          DateHelper.DATE_OF_SCHOOL = configureBean.getDate();
        }else{
          DateHelper.DATE_OF_SCHOOL = preConfigure.getDate();
        }
      }
    }
  }

}
