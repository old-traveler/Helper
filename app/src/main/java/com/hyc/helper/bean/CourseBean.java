package com.hyc.helper.bean;

import java.util.List;

public class CourseBean extends BaseRequestBean {

  /**
   * code : 200
   * data : [{"xh":"15408300210","xqj":"1","djj":"3","dsz":1,"qsz":"1","jsz":"13","name":"软件项目管理","teacher":"唐承亮","room":"公共120","zs":[1,3,5,7,9,11,13]},{"xh":"15408300210","xqj":"3","djj":"3","dsz":0,"qsz":"1","jsz":"14","name":"软件项目管理","teacher":"唐承亮","room":"公共222","zs":[1,2,3,4,5,6,7,8,9,10,11,12,13,14]},{"xh":"15408300210","xqj":"3","djj":"5","dsz":0,"qsz":"1","jsz":"13","name":"嵌入式软件开发","teacher":"李欣","room":"公共223","zs":[1,2,3,4,5,6,7,8,9,10,11,12,13]},{"xh":"15408300210","xqj":"4","djj":"7","dsz":0,"qsz":"1","jsz":"14","name":"软件测试技术","teacher":"何频捷","room":"计通楼512","zs":[1,2,3,4,5,6,7,8,9,10,11,12,13,14]},{"xh":"15408300210","xqj":"5","djj":"3","dsz":0,"qsz":"1","jsz":"1","name":"面向对象系统分析与设计","teacher":"李长云","room":"公共211","zs":[1]},{"xh":"15408300210","xqj":"1","djj":"3","dsz":0,"qsz":"2","jsz":"2","name":"软件测试技术","teacher":"何频捷","room":"公共120","zs":[2]},{"xh":"15408300210","xqj":"2","djj":"1","dsz":2,"qsz":"2","jsz":"14","name":"面向对象系统分析与设计","teacher":"李长云","room":"公共202","zs":[2,4,6,8,10,12,14]},{"xh":"15408300210","xqj":"5","djj":"7","dsz":2,"qsz":"2","jsz":"12","name":"嵌入式软件开发","teacher":"李欣","room":"公共222","zs":[2,4,6,8,10,12]},{"xh":"15408300210","xqj":"5","djj":"3","dsz":0,"qsz":"3","jsz":"15","name":"面向对象系统分析与设计","teacher":"李长云","room":"公共211","zs":[3,4,5,6,7,8,9,10,11,12,13,14,15]},{"xh":"15408300210","xqj":"5","djj":"5","dsz":2,"qsz":"4","jsz":"14","name":"软件测试技术","teacher":"何频捷","room":"公共113","zs":[4,6,8,10,12,14]}]
   */

  private List<CourseInfoBean> data;

  public List<CourseInfoBean> getData() {
    return data;
  }

  public void setData(List<CourseInfoBean> data) {
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }


}
