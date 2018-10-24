package com.hyc.helper.bean;

import java.util.List;

public class ExamBean extends BaseRequestBean {

  /**
   * code : 100
   * status : success
   * message : Excited
   * res : {"exam":[
   * {"KKDepNo":"15","CourseName":"EXCEL在会计中的运用","Starttime":"2018-11-20 14:00:00","EndTime":"2018-11-20 15:40:00","Week_Num":"11","isset":"0","RoomName":"公共101"},{"KKDepNo":"15","CourseName":"电子商务概论","Starttime":"2018-11-20 16:00:00","EndTime":"2018-11-20 17:40:00","Week_Num":"11","isset":"0","RoomName":"公共101"},{"KKDepNo":"15","CourseName":"通用财务软件运用","Starttime":"2018-12-11 14:00:00","EndTime":"2018-12-11 15:40:00","Week_Num":"14","isset":"0","RoomName":"公共101"},{"KKDepNo":"15","CourseName":"证券投资学","Starttime":"2019-01-02 14:00:00","EndTime":"2019-01-02 15:40:00","Week_Num":"17","isset":"0","RoomName":"公共101"},{"KKDepNo":"15","CourseName":"审计案例分析","Starttime":"2019-01-06 08:00:00","EndTime":"2019-01-06 09:40:00","Week_Num":"17","isset":"0","RoomName":"公共101"}],"cxexam":[]}
   * stuclass : 会计学1501
   * stuname : 杨静
   * count : 5
   */

  private String status;
  private String message;
  private ResBean res;
  private String stuclass;
  private String stuname;
  private int count;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ResBean getRes() {
    return res;
  }

  public void setRes(ResBean res) {
    this.res = res;
  }

  public String getStuclass() {
    return stuclass;
  }

  public void setStuclass(String stuclass) {
    this.stuclass = stuclass;
  }

  public String getStuname() {
    return stuname;
  }

  public void setStuname(String stuname) {
    this.stuname = stuname;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public static class ResBean {

    private List<ExamInfoBean> exam;
    private List<?> cxexam;

    public List<ExamInfoBean> getExam() {
      return exam;
    }

    public void setExam(List<ExamInfoBean> exam) {
      this.exam = exam;
    }

    public List<?> getCxexam() {
      return cxexam;
    }

    public void setCxexam(List<?> cxexam) {
      this.cxexam = cxexam;
    }

  }
}
