package com.hyc.helper.bean;

import com.hyc.helper.helper.IntegerConverter;
import java.util.List;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CourseInfoBean {
  /**
   * xh : 15408300210
   * xqj : 1
   * djj : 3
   * dsz : 1
   * qsz : 1
   * jsz : 13
   * name : 软件项目管理
   * teacher : 唐承亮
   * room : 公共120
   * zs : [1,3,5,7,9,11,13]
   */

  private String xh;
  private String xqj;
  private String djj;
  private int dsz;
  private String qsz;
  private String jsz;
  private String name;
  private String teacher;
  private String room;
  @Convert(columnType = String.class, converter = IntegerConverter.class)
  private List<Integer> zs;

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
}