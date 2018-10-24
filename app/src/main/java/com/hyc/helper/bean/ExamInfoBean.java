package com.hyc.helper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ExamInfoBean {

  /**
   * KKDepNo : 15
   * CourseName : EXCEL在会计中的运用
   * Starttime : 2018-11-20 14:00:00
   * EndTime : 2018-11-20 15:40:00
   * Week_Num : 11
   * isset : 0
   * RoomName : 公共101
   */

  private String KKDepNo;
  private String CourseName;
  private String Starttime;
  private String EndTime;
  private String Week_Num;
  private String isset;
  private String RoomName;

  @Generated(hash = 407945457)
  public ExamInfoBean(String KKDepNo, String CourseName, String Starttime,
          String EndTime, String Week_Num, String isset, String RoomName) {
      this.KKDepNo = KKDepNo;
      this.CourseName = CourseName;
      this.Starttime = Starttime;
      this.EndTime = EndTime;
      this.Week_Num = Week_Num;
      this.isset = isset;
      this.RoomName = RoomName;
  }

  @Generated(hash = 83160963)
  public ExamInfoBean() {
  }

  public String getKKDepNo() {
    return KKDepNo;
  }

  public void setKKDepNo(String KKDepNo) {
    this.KKDepNo = KKDepNo;
  }

  public String getCourseName() {
    return CourseName;
  }

  public void setCourseName(String CourseName) {
    this.CourseName = CourseName;
  }

  public String getStarttime() {
    return Starttime;
  }

  public void setStarttime(String Starttime) {
    this.Starttime = Starttime;
  }

  public String getEndTime() {
    return EndTime;
  }

  public void setEndTime(String EndTime) {
    this.EndTime = EndTime;
  }

  public String getWeek_Num() {
    return Week_Num;
  }

  public void setWeek_Num(String Week_Num) {
    this.Week_Num = Week_Num;
  }

  public String getIsset() {
    return isset;
  }

  public void setIsset(String isset) {
    this.isset = isset;
  }

  public String getRoomName() {
    return RoomName;
  }

  public void setRoomName(String RoomName) {
    this.RoomName = RoomName;
  }

}
