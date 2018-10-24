package com.hyc.helper.bean;

public class PowerBean extends BaseRequestBean{

  /**
   * code : 200
   * electricity : 93.17
   * balance : 54.69
   * time : 2018-10-24 11:49:59
   */

  private String electricity;
  private String balance;
  private String time;


  public String getElectricity() {
    return electricity;
  }

  public void setElectricity(String electricity) {
    this.electricity = electricity;
  }

  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
