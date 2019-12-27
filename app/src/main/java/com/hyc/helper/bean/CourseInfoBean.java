package com.hyc.helper.bean;

import com.google.gson.annotations.SerializedName;
import com.hyc.helper.helper.IntegerConverter;
import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CourseInfoBean  implements Serializable,Cloneable {

  public static final long serialVersionUID = 42L;


  @SerializedName("id")
  private String courseId;
  private String xh;
  private String xqj;//星期几
  private String djj;//第几节
  private int dsz;
  private String qsz;
  private String jsz;
  private String name;//名字
  private String teacher;//老师
  private String room;//教室
  @Convert(columnType = String.class, converter = IntegerConverter.class)
  private List<Integer> zs;//周数
  @Generated(hash = 1280403870)
  public CourseInfoBean(String courseId, String xh, String xqj, String djj,
          int dsz, String qsz, String jsz, String name, String teacher,
          String room, List<Integer> zs) {
      this.courseId = courseId;
      this.xh = xh;
      this.xqj = xqj;
      this.djj = djj;
      this.dsz = dsz;
      this.qsz = qsz;
      this.jsz = jsz;
      this.name = name;
      this.teacher = teacher;
      this.room = room;
      this.zs = zs;
  }
  @Generated(hash = 290827734)
  public CourseInfoBean() {
  }
  public String getCourseId() {
      return this.courseId;
  }
  public void setCourseId(String courseId) {
      this.courseId = courseId;
  }
  public String getXh() {
      return this.xh;
  }
  public void setXh(String xh) {
      this.xh = xh;
  }
  public String getXqj() {
      return this.xqj;
  }
  public void setXqj(String xqj) {
      this.xqj = xqj;
  }
  public String getDjj() {
      return this.djj;
  }
  public void setDjj(String djj) {
      this.djj = djj;
  }
  public int getDsz() {
      return this.dsz;
  }
  public void setDsz(int dsz) {
      this.dsz = dsz;
  }
  public String getQsz() {
      return this.qsz;
  }
  public void setQsz(String qsz) {
      this.qsz = qsz;
  }
  public String getJsz() {
      return this.jsz;
  }
  public void setJsz(String jsz) {
      this.jsz = jsz;
  }
  public String getName() {
      return this.name;
  }
  public void setName(String name) {
      this.name = name;
  }
  public String getTeacher() {
      return this.teacher;
  }
  public void setTeacher(String teacher) {
      this.teacher = teacher;
  }
  public String getRoom() {
      return this.room;
  }
  public void setRoom(String room) {
      this.room = room;
  }
  public List<Integer> getZs() {
      return this.zs;
  }
  public void setZs(List<Integer> zs) {
      this.zs = zs;
  }

  @Override
  public Object clone() {
    CourseInfoBean courseInfoBean = null;
    try{
      courseInfoBean = (CourseInfoBean) super.clone();
    }catch(CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return courseInfoBean;
  }

}