package com.hyc.helper.bean;

import android.support.annotation.NonNull;

public class CalendarBean extends BaseRequestBean implements Comparable<CalendarBean> {

  /**
   * name : CFA
   * date : 2018.12.01
   * days : 12
   */

  private String name;
  private String date;
  private int days;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getDays() {
    return days;
  }

  public void setDays(int days) {
    this.days = days;
  }

  @Override
  public int compareTo(@NonNull CalendarBean calendarBean) {
    return Math.abs(days) - Math.abs(calendarBean.getDays());
  }
}
