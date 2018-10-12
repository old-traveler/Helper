package com.hyc.helper.bean;

import java.util.List;

public class FindPeopleBean {

  /**
   * code : 200
   * data : [{"id":"24838","head_pic":"","dep_name":"计算机学院","class_name":"软件工程1502","last_use":"2018-10-11 00:05:16","head_pic_thumb":"/uploads/headpics/201709/1506212345.711471082_thumb.png"}]
   */

  private int code;
  private List<DataBean> data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public List<DataBean> getData() {
    return data;
  }

  public void setData(List<DataBean> data) {
    this.data = data;
  }

  public static class DataBean {
    /**
     * id : 24838
     * head_pic :
     * dep_name : 计算机学院
     * class_name : 软件工程1502
     * last_use : 2018-10-11 00:05:16
     * head_pic_thumb : /uploads/headpics/201709/1506212345.711471082_thumb.png
     */

    private String id;
    private String head_pic;
    private String dep_name;
    private String class_name;
    private String last_use;
    private String head_pic_thumb;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getHead_pic() {
      return head_pic;
    }

    public void setHead_pic(String head_pic) {
      this.head_pic = head_pic;
    }

    public String getDep_name() {
      return dep_name;
    }

    public void setDep_name(String dep_name) {
      this.dep_name = dep_name;
    }

    public String getClass_name() {
      return class_name;
    }

    public void setClass_name(String class_name) {
      this.class_name = class_name;
    }

    public String getLast_use() {
      return last_use;
    }

    public void setLast_use(String last_use) {
      this.last_use = last_use;
    }

    public String getHead_pic_thumb() {
      return head_pic_thumb;
    }

    public void setHead_pic_thumb(String head_pic_thumb) {
      this.head_pic_thumb = head_pic_thumb;
    }
  }
}
