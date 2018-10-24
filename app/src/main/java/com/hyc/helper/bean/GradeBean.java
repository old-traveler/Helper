package com.hyc.helper.bean;

import java.util.List;

public class GradeBean extends BaseRequestBean{

  private List<GradeInfoBean> data;

  public List<GradeInfoBean> getData() {
    return data;
  }

  public void setData(List<GradeInfoBean> data) {
    this.data = data;
  }


}
