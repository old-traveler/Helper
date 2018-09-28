package com.hyc.helper.bean;

import java.util.List;

public class CourseBean extends BaseRequestBean {


  private List<CourseInfoBean> data;

  public List<CourseInfoBean> getData() {
    return data;
  }

  public void setData(List<CourseInfoBean> data) {
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }


}
