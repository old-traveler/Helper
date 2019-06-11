package com.hyc.helper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 作者: 贺宇成
 * 时间: 2019-06-11
 * 描述:
 */
@Entity
public class WebUrlBean {

  private String title;

  @Id
  private String url;

  private long collectTime;

  @Generated(hash = 814041409)
  public WebUrlBean(String title, String url, long collectTime) {
      this.title = title;
      this.url = url;
      this.collectTime = collectTime;
  }

  @Generated(hash = 1249787967)
  public WebUrlBean() {
  }

  public String getTitle() {
      return this.title;
  }

  public void setTitle(String title) {
      this.title = title;
  }

  public String getUrl() {
      return this.url;
  }

  public void setUrl(String url) {
      this.url = url;
  }

  public long getCollectTime() {
      return this.collectTime;
  }

  public void setCollectTime(long collectTime) {
      this.collectTime = collectTime;
  }

}
