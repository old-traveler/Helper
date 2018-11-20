package com.hyc.helper.bean;

import com.hyc.helper.helper.IntegerConverter;
import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CourseInfoBean  implements Serializable,Cloneable {

  public static final long serialVersionUID = 42L;


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

  @Generated(hash = 29831154)
  public CourseInfoBean(String xh, String xqj, String djj, int dsz, String qsz,
          String jsz, String name, String teacher, String room,
          List<Integer> zs) {
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

  public String getXh() {
    return xh;
  }

  public void setXh(String xh) {
    this.xh = xh;
  }

  public String getXqj() {
    return xqj;
  }

  public void setXqj(String xqj) {
    this.xqj = xqj;
  }

  public String getDjj() {
    return djj;
  }

  public void setDjj(String djj) {
    this.djj = djj;
  }

  public int getDsz() {
    return dsz;
  }

  public void setDsz(int dsz) {
    this.dsz = dsz;
  }

  public String getQsz() {
    return qsz;
  }

  public void setQsz(String qsz) {
    this.qsz = qsz;
  }

  public String getJsz() {
    return jsz;
  }

  public void setJsz(String jsz) {
    this.jsz = jsz;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTeacher() {
    return teacher;
  }

  public void setTeacher(String teacher) {
    this.teacher = teacher;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public List<Integer> getZs() {
    return zs;
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