package com.hyc.helper.bean;

public class TextPositionBean<T> {

  private int start;

  private int end;

  private T data;

  public TextPositionBean(int start, int end, T data) {
    this.start = start;
    this.end = end;
    this.data = data;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
