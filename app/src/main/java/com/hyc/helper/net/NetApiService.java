package com.hyc.helper.net;

import butterknife.Optional;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.GoodsDetailBean;
import com.hyc.helper.bean.ImageUploadBean;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.bean.SecondHandBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.UserBean;
import io.reactivex.Observable;
import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NetApiService {

  String baseUrl = "https://api.huthelper.cn/api/v3/";

  @GET("get/loginWx/{number}/{password}/0")
  Observable<UserBean> login(@Path("number") String number, @Path("password") String password);

  @GET("get/schedule/{number}/{code}")
  Observable<CourseBean> getSchedule(@Path("number") String number, @Path("code") String code);

  @GET("statement/statement/{number}/{page}")
  Observable<StatementBean> getStatement(@Path("number") String number, @Path("page") int page);

  @GET("statement/like/{number}/{code}/{statement_id}")
  Observable<BaseRequestBean> likeStatement(
      @Path("number") String number, @Path("code") String code,
      @Path("statement_id") String statementId);

  @GET("trade/goods/{page}/0")
  Observable<SecondHandBean> getSecondHandMaker(@Path("page") int page);

  @GET("trade/details/{number}/{code}/{goodsId}")
  Observable<GoodsDetailBean> getGoodsDetailInfo(
      @Path("number") String number, @Path("code") String code, @Path("goodsId") String goodsId);

  @GET("loses/goods/{page}/0")
  Observable<LostBean> getLostAndFind(@Path("page") int page);

  @GET("https://raw.githubusercontent.com/old-traveler/Helper/master/img/love.json")
  Observable<ConfigureBean> getConfigure();

  @FormUrlEncoded
  @POST("statement/comment/{number}/{code}")
  Observable<BaseRequestBean> commentStatement(@Path("number") String number,
      @Path("code") String code, @Field("comment") String comment,
      @Field("moment_id") String moment_id);

  @FormUrlEncoded
  @POST("statement/create/{number}/{code}")
  Observable<BaseRequestBean> publishStatement(@Path("number") String number,
      @Path("code") String code, @Field("content") String content, @Field("user_id") String userId,
      @Field("hidden") String hidden);

  @Multipart
  @POST("https://pic.htmln.com/api/v3/upload/images/{number}/{code}/{env}/0")
  Observable<ImageUploadBean> uploadImage(@Path("number") String number, @Path("code") String code,
      @Path("env") String env, @Part
      MultipartBody.Part file);

  @GET("statement/deleteWechat/{number}/{code}/{momend_id}")
  Observable<BaseRequestBean> deleteStatement(@Path("number") String number,
      @Path("code") String code, @Path("momend_id") String momend_id);

  //考试计划 home/examination_wechat/学号/code
}
