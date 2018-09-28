package com.hyc.helper.helper;

import com.hyc.helper.net.NetApiService;
import com.hyc.helper.net.RetrofitClient;

public class RequestHelper {

  public static NetApiService getRequestApi(){
    RetrofitClient client = RetrofitClient.getInstance();
    return client.getRetrofit(NetApiService.baseUrl).create(NetApiService.class);
  }

}
