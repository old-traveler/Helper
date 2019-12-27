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
      getEditor().putString(key, json).apply();
    }).subscribeOn(Schedulers.io()).subscribe();
  }

  public static void deleteClassFromSp(){
    getEditor().clear().apply();
  }

  public static <T> T getClassFromSp(String key, Class<T> cls) {
    try {
      String json = getSharedPreferences().getString(key, "");
      return new Gson().fromJson(json, cls);
    } catch (Exception e) {
      return null;
    }
  }

  public static void removeKey(String key){
    getEditor().remove(key).apply();
  }

  public static void putBoolean(String key, boolean data) {
    getEditor().putBoolean(key, data).apply();
  }

  public static void putString(String key, String data) {
    getEditor().putString(key, data).apply();
  }

  public static void putLong(String key, long data){
    getEditor().putLong(key,data).apply();
  }

  public static long getLong(String key){
    return getSharedPreferences().getLong(key,0);
  }

  public static String getString(String key){
    return getSharedPreferences().getString(key,null);
  }

  public static boolean getBoolean(String key) {
    return getSharedPreferences().getBoolean(key, false);
  }
}
