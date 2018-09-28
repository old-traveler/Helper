package com.hyc.helper.net;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

  private static final int DEFAULT_TIMEOUT = 10;

  private OkHttpClient okHttpClient;

  private static RetrofitClient mInstance;

  private HashMap<String,WeakReference<Retrofit>> retrofitPool;

  public static RetrofitClient getInstance() {
    if (mInstance==null){
      mInstance = new RetrofitClient();
    }
    return mInstance;
  }

  private RetrofitClient() {
    retrofitPool = new HashMap<>();
  }

  public Retrofit getRetrofit(String baseUrl){
    WeakReference<Retrofit> weakReference = retrofitPool.get(baseUrl);
    boolean isExist = weakReference != null && weakReference.get()!=null;
    return isExist ? weakReference.get():getNewRetrofit(baseUrl);
  }

  public Retrofit getNewRetrofit(String baseUrl){
    if (okHttpClient == null){
      initOkHttpClient();
    }
    Retrofit retrofit = new Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(baseUrl)
        .build();
    retrofitPool.put(baseUrl,new WeakReference<>(retrofit));
    return retrofit;
  }

  private void initOkHttpClient() {
    okHttpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.HEADERS))
        .addInterceptor(new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .build();
  }

}
