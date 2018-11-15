package com.hyc.helper.bean;

public class UpdateApkBean extends BaseRequestBean {

  /**
   * version_code : 1
   * apk_url :
   */

  private int version_code;
  private String apk_url;

  public int getVersion_code() {
    return version_code;
  }

  public void setVersion_code(int version_code) {
    this.version_code = version_code;
  }

  public String getApk_url() {
    return apk_url;
  }

  public void setApk_url(String apk_url) {
    this.apk_url = apk_url;
  }
}
