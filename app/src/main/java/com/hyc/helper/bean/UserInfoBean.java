package com.hyc.helper.bean;

public class UserInfoBean extends BaseRequestBean {

  /**
   * code : 200
   * data : {"studentKH":"16410100215","dep_name":"文学与新闻传播学院","class_name":"","user_id":"35545","head_pic":"/uploads/headpics/201810/1540253263.67278478.jpg","username":"江怜怜","bio":"云梦江澄的宗主夫人。","head_pic_thumb":"/uploads/headpics/201810/1540253263.67278478_thumb.jpg"}
   */

  private DataBean data;


  public DataBean getData() {
    return data;
  }

  public void setData(DataBean data) {
    this.data = data;
  }

  public static class DataBean {
    /**
     * studentKH : 16410100215
     * dep_name : 文学与新闻传播学院
     * class_name :
     * user_id : 35545
     * head_pic : /uploads/headpics/201810/1540253263.67278478.jpg
     * username : 江怜怜
     * bio : 云梦江澄的宗主夫人。
     * head_pic_thumb : /uploads/headpics/201810/1540253263.67278478_thumb.jpg
     */

    private String studentKH;
    private String dep_name;
    private String class_name;
    private String user_id;
    private String head_pic;
    private String username;
    private String bio;
    private String head_pic_thumb;

    public String getStudentKH() {
      return studentKH;
    }

    public void setStudentKH(String studentKH) {
      this.studentKH = studentKH;
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

    public String getUser_id() {
      return user_id;
    }

    public void setUser_id(String user_id) {
      this.user_id = user_id;
    }

    public String getHead_pic() {
      return head_pic;
    }

    public void setHead_pic(String head_pic) {
      this.head_pic = head_pic;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getBio() {
      return bio;
    }

    public void setBio(String bio) {
      this.bio = bio;
    }

    public String getHead_pic_thumb() {
      return head_pic_thumb;
    }

    public void setHead_pic_thumb(String head_pic_thumb) {
      this.head_pic_thumb = head_pic_thumb;
    }
  }
}
