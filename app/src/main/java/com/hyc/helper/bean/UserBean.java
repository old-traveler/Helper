package com.hyc.helper.bean;

public class UserBean extends BaseRequestBean{


  private DataBean data;
  private String remember_code_app;

  public DataBean getData() {
    return data;
  }

  public void setData(DataBean data) {
    this.data = data;
  }

  public String getRemember_code_app() {
    return remember_code_app;
  }

  public void setRemember_code_app(String remember_code_app) {
    this.remember_code_app = remember_code_app;
  }

  public static class DataBean {


    private int user_id;
    private String studentKH;
    private String school;
    private String TrueName;
    private String username;
    private String dep_name;
    private String class_name;
    private String address;
    private String active;
    private String last_use;
    private String bio;
    private String head_pic;
    private String head_pic_thumb;
    private String remember_code_app;

    public int getUser_id() {
      return user_id;
    }

    public void setUser_id(int user_id) {
      this.user_id = user_id;
    }

    public String getStudentKH() {
      return studentKH;
    }

    public void setStudentKH(String studentKH) {
      this.studentKH = studentKH;
    }

    public String getSchool() {
      return school;
    }

    public void setSchool(String school) {
      this.school = school;
    }

    public String getTrueName() {
      return TrueName;
    }

    public void setTrueName(String TrueName) {
      this.TrueName = TrueName;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
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

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getActive() {
      return active;
    }

    public void setActive(String active) {
      this.active = active;
    }

    public String getLast_use() {
      return last_use;
    }

    public void setLast_use(String last_use) {
      this.last_use = last_use;
    }

    public String getBio() {
      return bio;
    }

    public void setBio(String bio) {
      this.bio = bio;
    }

    public String getHead_pic() {
      return head_pic;
    }

    public void setHead_pic(String head_pic) {
      this.head_pic = head_pic;
    }

    public String getHead_pic_thumb() {
      return head_pic_thumb;
    }

    public void setHead_pic_thumb(String head_pic_thumb) {
      this.head_pic_thumb = head_pic_thumb;
    }

    public String getRemember_code_app() {
      return remember_code_app;
    }

    public void setRemember_code_app(String remember_code_app) {
      this.remember_code_app = remember_code_app;
    }
  }
}
