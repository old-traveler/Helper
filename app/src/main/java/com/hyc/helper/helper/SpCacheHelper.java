package com.hyc.helper.helper;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.hyc.helper.HelperApplication;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SpCacheHelper {

  private static SharedPreferences.Editor getEditor() {
    return getSharedPreferences().edit();
  }

  private static SharedPreferences getSharedPreferences() {
    return HelperApplication.getContext().getSharedPreferences("helper", Context.MODE_PRIVATE);
  }

  public static void putClassIntoSp(String key, Object object) {
    Observable.create(emitter -> {
      String json = new Gson().toJson(object);
      getEditor().putString(key, json).commit();
    }).subscribeOn(Schedulers.io()).subscribe();
  }

  public static void deleteClassFromSp(String key){
    getEditor().clear().commit();
  }

  public static <T> T getClassFromSp(String key, Class<T> cls) {
    try {
      String json = getSharedPreferences().getString(key, "");
      return new Gson().fromJson(json, cls);
    } catch (Exception e) {
      return null;
    }
  }

  public static void putBoolean(String key, boolean data) {
    getEditor().putBoolean(key, data).commit();
  }

  public static boolean getBoolean(String key) {
    return getSharedPreferences().getBoolean(key, false);
  }
}
