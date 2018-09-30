package com.hyc.helper.bean;

import java.io.Serializable;
import java.util.List;

public class ConfigureBean extends BaseRequestBean implements Serializable {

  /**
   * code : 200
   * number : 15414000219
   * type : 1
   * content : hello world
   * image : ["http://img.zcool.cn/community/0117e2571b8b246ac72538120dd8a4.jpg@1280w_1l_2o_100sh.jpg","http://img.zcool.cn/community/0117e2571b8b246ac72538120dd8a4.jpg@1280w_1l_2o_100sh.jpg"]
   * song : http://bmob-cdn-12662.b0.upaiyun.com/2017/07/15/12896d5db1c04df2b0d088a0ca2c94a6.amr
   * video : http://bmob-cdn-12662.b0.upaiyun.com/2018/09/29/3986da31409fb40180949736f5782207.mp4
   */

  private boolean isDeal;
  private String update_time;
  private String number;
  private String date;
  private String type;
  private String content;
  private String song;
  private String video;
  private String update;
  private List<String> image;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }


  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getSong() {
    return song;
  }

  public void setSong(String song) {
    this.song = song;
  }

  public String getVideo() {
    return video;
  }

  public void setVideo(String video) {
    this.video = video;
  }

  public List<String> getImage() {
    return image;
  }

  public void setImage(List<String> image) {
    this.image = image;
  }

  public String getUpdate() {
    return update;
  }

  public void setUpdate(String update) {
    this.update = update;
  }

  public String getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(String update_time) {
    this.update_time = update_time;
  }

  public boolean isDeal() {
    return isDeal;
  }

  public void setDeal(boolean deal) {
    isDeal = deal;
  }
}
