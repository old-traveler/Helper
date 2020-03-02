package com.hyc.helper.bean;

public class InfoEntity {

  private String title;

  private String value;

  private String userId;

  public InfoEntity(String title, String value) {
    this.title = title;
    this.value = value;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getUserId() {
    return userId;
  }

  public InfoEntity setUserId(String userId) {
    this.userId = userId;
    return this;
  }
}
