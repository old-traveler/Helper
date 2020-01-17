package com.hyc.helper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class BigImageLoadRecordBean {
  @Id
  private String originUrl;

  private String filePath;

  @Generated(hash = 1177118743)
  public BigImageLoadRecordBean(String originUrl, String filePath) {
    this.originUrl = originUrl;
    this.filePath = filePath;
  }

  @Generated(hash = 949636533)
  public BigImageLoadRecordBean() {
  }

  public String getOriginUrl() {
    return this.originUrl;
  }

  public void setOriginUrl(String originUrl) {
    this.originUrl = originUrl;
  }

  public String getFilePath() {
    return this.filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
}
