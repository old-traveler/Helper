package com.hyc.helper.bean;

import android.graphics.Point;

public class CourseTableItemBean {

  private Point point;

  private CourseInfoBean infoBean;

  public CourseTableItemBean(Point point, CourseInfoBean infoBean) {
    this.point = point;
    this.infoBean = infoBean;
  }

  public Point getPoint() {
    return point;
  }

  public void setPoint(Point point) {
    this.point = point;
  }

  public CourseInfoBean getInfoBean() {
    return infoBean;
  }

  public void setInfoBean(CourseInfoBean infoBean) {
    this.infoBean = infoBean;
  }
}
