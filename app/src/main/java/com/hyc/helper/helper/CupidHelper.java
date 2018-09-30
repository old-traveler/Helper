package com.hyc.helper.helper;

import com.hyc.helper.bean.ConfigureBean;

public class CupidHelper {

  public static String cupid(ConfigureBean configureBean,String number){
    if (configureBean!=null
        && !configureBean.isDeal()
        && configureBean.getNumber().equals(number)){
      configureBean.setDeal(true);
      SpCacheHelper.putClassIntoSp(Constant.SP_CONFIG,configureBean);
      return configureBean.getType();
    }else{
      return "0";
    }
  }
}
