package com.hyc.helper.helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.hyc.helper.HelperApplication;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SpCacheHelper {

  public static String SP_NANME = "helper";

  public static SharedPreferences.Editor getEditor(){
    return getSharedPreferences().edit();
  }

  public static SharedPreferences getSharedPreferences(){
    return HelperApplication.getContext().getSharedPreferences(SP_NANME,Context.MODE_PRIVATE);
  }

  public static void putClassIntoSp(String key,Object object){
    Observable.create(emitter -> {
      String json = new Gson().toJson(object);
      getEditor().putString(key,json).commit();
    }).subscribeOn(Schedulers.io()).subscribe();
  }

  public static <T> T getClassFromSp(String key,Class<T> cls){
    try {
      String json = getSharedPreferences().getString(key,"");
      return new Gson().fromJson(json, cls);
    } catch (Exception e) {
      return null;
    }
  }

}
