package com.hyc.helper.bean;

import java.util.List;

public class StatementBean extends BaseRequestBean{


  private String current_page;
  private int pageination;
  private List<StatementInfoBean> statement;


  public String getCurrent_page() {
    return current_page;
  }

  public void setCurrent_page(String current_page) {
    this.current_page = current_page;
  }

  public int getPageination() {
    return pageination;
  }

  public void setPageination(int pageination) {
    this.pageination = pageination;
  }

  public List<StatementInfoBean> getStatement() {
    return statement;
  }

  public void setStatement(List<StatementInfoBean> statement) {
    this.statement = statement;
  }

}
