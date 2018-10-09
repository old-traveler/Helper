package com.hyc.helper.bean;

public class ImageUploadBean extends BaseRequestBean {

  /**
   * data : /uploads/moments/201810/1539074120.81427707_thumb.jpg
   * data_original : /uploads/moments/201810/1539074120.81427707.jpg
   */

  private String data;
  private String data_original;

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getData_original() {
    return data_original;
  }

  public void setData_original(String data_original) {
    this.data_original = data_original;
  }
}
