package com.hyc.helper.net;

import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.UserBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetApiService {

  String baseUrl = "https://api.huthelper.cn/api/v3/";

  @GET("get/loginWx/{number}/{password}/0")
  Observable<UserBean> login(@Path("number") String number,@Path("password") String password);

  @GET("get/schedule/{number}/{code}")
  Observable<CourseBean> getSchedule(@Path("number") String number,@Path("code") String code);

  @GET("statement/statement/{number}/{page}")
  Observable<StatementBean> getStatement(@Path("number") String number,@Path("page") int page);

  @GET("statement/like/{number}/{code}/{statement_id}")
  Observable<BaseRequestBean> likeStatement(@Path("number") String number
      ,@Path("code") String code,@Path("statement_id") String statementId);

  //登陆 get/loginWx/学号/密码/0

  //课表 get/schedule/学号/code

  //校园说说 statement/statement/学号/页数

  // post 评论 statement/comment/学号/code   参数：comment、moment_id帖子id

  //考试计划 home/examination_wechat/学号/code



}
