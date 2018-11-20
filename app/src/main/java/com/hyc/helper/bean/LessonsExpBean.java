package com.hyc.helper.bean;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LessonsExpBean extends BaseRequestBean{

  private List<DataBean> data;

  public List<DataBean> getData() {
    return data;
  }

  public void setData(List<DataBean> data) {
    this.data = data;
  }

  public static class DataBean {
    /**
     * teacher : 李欣
     * lesson : 嵌入式软件开发*专业核心课组
     * obj :
     * locate : 计通楼计206
     * weeks_no : 10
     * week : 1
     * lesson_no : 3
     * period : 2
     * real_time : 14:00-15:40
     * class : 软件工程1501;软件工程1502;
     */

    private String teacher;
    private String lesson;
    private String obj;
    private String locate;
    private String weeks_no;
    private String week;
    private String lesson_no;
    private String period;
    private String real_time;
    @SerializedName("class")
    private String classX;

    public String getTeacher() {
      return teacher;
    }

    public void setTeacher(String teacher) {
      this.teacher = teacher;
    }

    public String getLesson() {
      return lesson;
    }

    public void setLesson(String lesson) {
      this.lesson = lesson;
    }

    public String getObj() {
      return obj;
    }

    public void setObj(String obj) {
      this.obj = obj;
    }

    public String getLocate() {
      return locate;
    }

    public void setLocate(String locate) {
      this.locate = locate;
    }

    public String getWeeks_no() {
      return weeks_no;
    }

    public void setWeeks_no(String weeks_no) {
      this.weeks_no = weeks_no;
    }

    public String getWeek() {
      return week;
    }

    public void setWeek(String week) {
      this.week = week;
    }

    public String getLesson_no() {
      return lesson_no;
    }

    public void setLesson_no(String lesson_no) {
      this.lesson_no = lesson_no;
    }

    public String getPeriod() {
      return period;
    }

    public void setPeriod(String period) {
      this.period = period;
    }

    public String getReal_time() {
      return real_time;
    }

    public void setReal_time(String real_time) {
      this.real_time = real_time;
    }

    public String getClassX() {
      return classX;
    }

    public void setClassX(String classX) {
      this.classX = classX;
    }
  }
}
