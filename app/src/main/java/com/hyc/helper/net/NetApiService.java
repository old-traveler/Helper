package com.hyc.helper.net;

import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.GoodsDetailBean;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.bean.SecondHandBean;
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
  Observable<BaseRequestBean> likeStatement(
      @Path("number") String number,@Path("code") String code,@Path("statement_id") String statementId);

  @GET("trade/goods/{page}/0")
  Observable<SecondHandBean> getSecondHandMaket(@Path("page") int page);

  @GET("trade/details/{number}/{code}/{goodsId}")
  Observable<GoodsDetailBean> getGoodsDetailInfo(
      @Path("number")String number,@Path("code")String code,@Path("goodsId") String goodsId);

  @GET("loses/goods/{page}/0")
  Observable<LostBean> getLostAndFind(@Path("page")int page);

  @GET("https://raw.githubusercontent.com/old-traveler/Helper/master/img/love.json")
  Observable<ConfigureBean> getConfigure();

  // post 评论 statement/comment/学号/code   参数：comment、moment_id帖子id

  //考试计划 home/examination_wechat/学号/code



}
