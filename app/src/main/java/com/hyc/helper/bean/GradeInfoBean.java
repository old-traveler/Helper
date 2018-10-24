package com.hyc.helper.bean;

import android.support.annotation.NonNull;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GradeInfoBean implements Comparable<GradeInfoBean>{

  /**
   * xh : 15408300210
   * xn : 2015-2016
   * xq : 2
   * kcmc : 大学英语2
   * xf : 3.5
   * cj : 73.0
   * zscj : 73.0
   * bkcj :
   * jd : 2.3
   * cxbj : 0
   * kcxz : 必修课
   * bj :
   */

  private String xh;
  private String xn;
  private String xq;
  private String kcmc;
  private String xf;
  private String cj;
  private String zscj;
  private String bkcj;
  private String jd;
  private String cxbj;
  private String kcxz;
  private String bj;

  @Generated(hash = 1180664765)
  public GradeInfoBean(String xh, String xn, String xq, String kcmc, String xf,
          String cj, String zscj, String bkcj, String jd, String cxbj,
          String kcxz, String bj) {
      this.xh = xh;
      this.xn = xn;
      this.xq = xq;
      this.kcmc = kcmc;
      this.xf = xf;
      this.cj = cj;
      this.zscj = zscj;
      this.bkcj = bkcj;
      this.jd = jd;
      this.cxbj = cxbj;
      this.kcxz = kcxz;
      this.bj = bj;
  }

  @Generated(hash = 2075784513)
  public GradeInfoBean() {
  }

  public String getXh() {
    return xh;
  }

  public void setXh(String xh) {
    this.xh = xh;
  }

  public String getXn() {
    return xn;
  }

  public void setXn(String xn) {
    this.xn = xn;
  }

  public String getXq() {
    return xq;
  }

  public void setXq(String xq) {
    this.xq = xq;
  }

  public String getKcmc() {
    return kcmc;
  }

  public void setKcmc(String kcmc) {
    this.kcmc = kcmc;
  }

  public String getXf() {
    return xf;
  }

  public void setXf(String xf) {
    this.xf = xf;
  }

  public String getCj() {
    return cj;
  }

  public void setCj(String cj) {
    this.cj = cj;
  }

  public String getZscj() {
    return zscj;
  }

  public void setZscj(String zscj) {
    this.zscj = zscj;
  }

  public String getBkcj() {
    return bkcj;
  }

  public void setBkcj(String bkcj) {
    this.bkcj = bkcj;
  }

  public String getJd() {
    return jd;
  }

  public void setJd(String jd) {
    this.jd = jd;
  }

  public String getCxbj() {
    return cxbj;
  }

  public void setCxbj(String cxbj) {
    this.cxbj = cxbj;
  }

  public String getKcxz() {
    return kcxz;
  }

  public void setKcxz(String kcxz) {
    this.kcxz = kcxz;
  }

  public String getBj() {
    return bj;
  }

  public void setBj(String bj) {
    this.bj = bj;
  }

  @Override
  public int compareTo(@NonNull GradeInfoBean gradeInfoBean) {
    int year = Integer.parseInt(gradeInfoBean.xn.substring(0,4));
    int term = Integer.parseInt(gradeInfoBean.getXq());
    int myYear = Integer.parseInt(xn.substring(0,4));
    int myTerm = Integer.parseInt(xq);
    if (myYear > year){
      return -1;
    }else if (myYear == year && myTerm >= term){
      return -1;
    }
    return 1;
  }
}
