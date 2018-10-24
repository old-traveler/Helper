package com.hyc.helper.net;

import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.ExamBean;
import com.hyc.helper.bean.FindPeopleBean;
import com.hyc.helper.bean.GoodsDetailBean;
import com.hyc.helper.bean.GradeBean;
import com.hyc.helper.bean.ImageUploadBean;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.bean.PowerBean;
import com.hyc.helper.bean.SecondHandBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.UserBean;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface NetApiService {

  String baseUrl = "https://api.huthelper.cn/api/v3/";

  /**
   * 登陆
   * @param number 学号
   * @param password 密码
   * @return
   */
  @GET("get/loginWx/{number}/{password}/0")
  Observable<UserBean> login(@Path("number") String number, @Path("password") String password);

  /**
   * 查询课程表
   * @param number 学号
   * @param code   code
   * @return
   */
  @GET("get/schedule/{number}/{code}")
  Observable<CourseBean> getSchedule(@Path("number") String number, @Path("code") String code);

  /**
   * 查询校园说说
   * @param number 学号
   * @param page  页号
   * @return
   */
  @GET("statement/statement/{number}/{page}")
  Observable<StatementBean> getStatement(@Path("number") String number, @Path("page") int page);

  /**
   * 赞说说
   * @param number 学号
   * @param code code
   * @param statementId 说说id
   * @return
   */
  @GET("statement/like/{number}/{code}/{statement_id}")
  Observable<BaseRequestBean> likeStatement(
      @Path("number") String number, @Path("code") String code,
      @Path("statement_id") String statementId);

  /**
   * 查询二手市场
   * @param page 页号
   * @return
   */
  @GET("trade/goods/{page}/0")
  Observable<SecondHandBean> getSecondHandMaker(@Path("page") int page);

  /**
   * 获取二手商品详情
   * @param number 学号
   * @param code   code
   * @param goodsId 商品id
   * @return
   */
  @GET("trade/details/{number}/{code}/{goodsId}")
  Observable<GoodsDetailBean> getGoodsDetailInfo(
      @Path("number") String number, @Path("code") String code, @Path("goodsId") String goodsId);

  /**
   * 查询失物招领
   * @param page 页号
   * @return
   */
  @GET("loses/goods/{page}/0")
  Observable<LostBean> getLostAndFind(@Path("page") int page);

  /**
   * 获取应用配置信息，静态Api
   * @return
   */
  @GET("https://raw.githubusercontent.com/old-traveler/Helper/master/img/love.json")
  Observable<ConfigureBean> getConfigure();

  /**
   * 评论说说
   * @param number
   * @param code
   * @param comment 评论
   * @param moment_id  说说id
   * @return
   */
  @FormUrlEncoded
  @POST("statement/comment/{number}/{code}")
  Observable<BaseRequestBean> commentStatement(@Path("number") String number,
      @Path("code") String code, @Field("comment") String comment,
      @Field("moment_id") String moment_id);

  /**
   * 发布说说
   * @param number 学号
   * @param code
   * @param content 内容
   * @param userId
   * @param hidden 照片
   * @return
   */
  @FormUrlEncoded
  @POST("statement/create/{number}/{code}")
  Observable<BaseRequestBean> publishStatement(@Path("number") String number,
      @Path("code") String code, @Field("content") String content, @Field("user_id") String userId,
      @Field("hidden") String hidden);

  /**
   * 上传照片
   * @param number
   * @param code
   * @param env  sha1验证码
   * @param type 上传类型
   * @param file 照片
   * @return
   */
  @Multipart
  @POST("https://pic.htmln.com/api/v3/upload/images/{number}/{code}/{env}/{type}")
  Observable<ImageUploadBean> uploadImage(@Path("number") String number, @Path("code") String code,
      @Path("env") String env, @Path("type") String type, @Part
      MultipartBody.Part file);

  /**
   * 删除说说
   * @param number
   * @param code
   * @param momend_id
   * @return
   */
  @GET("statement/deleteWechat/{number}/{code}/{momend_id}")
  Observable<BaseRequestBean> deleteStatement(@Path("number") String number,
      @Path("code") String code, @Path("momend_id") String momend_id);

  /**
   * 找人
   * @param number
   * @param code
   * @param env
   * @param name
   * @return
   */
  @FormUrlEncoded
  @POST("im/get_students/{number}/{code}/{env}")
  Observable<FindPeopleBean> findPeople(@Path("number") String number, @Path("code") String code,
      @Path("env") String env, @Field("name") String name);

  /**
   * 获取某人发布的说说
   * @param number
   * @param page
   * @param userId
   * @return
   */
  @GET("statement/statement/{number}/{page}/{user_id}")
  Observable<StatementBean> getUserStatement(@Path("number") String number, @Path("page") int page,
      @Path("user_id") String userId);

  /**
   * 获取某人发布的二手
   * @param number
   * @param code
   * @param page
   * @param userId
   * @return
   */
  @GET("trade/own/{number}/{code}/{page}/{user_id}")
  Observable<SecondHandBean> getUserSecondMaker(@Path("number") String number,
      @Path("code") String code, @Path("page") int page, @Path("user_id") String userId);

  /**
   * 获取默认发布的失物招领
   * @param number
   * @param code
   * @param page
   * @param userId
   * @return
   */
  @GET("loses/own/{number}/{code}/{page}/{user_id}")
  Observable<LostBean> getUserLost(@Path("number") String number, @Path("code") String code,
      @Path("page") int page, @Path("user_id") String userId);

  /**
   * 更新用户名
   * @param number
   * @param code
   * @param username
   * @return
   */
  @FormUrlEncoded
  @POST("set/profile/{number}/{code}")
  Observable<BaseRequestBean> updateUsername(@Path("number") String number,
      @Path("code") String code, @Field("username") String username);

  /**
   * 更新用户签名
   * @param number
   * @param code
   * @param bio
   * @return
   */
  @FormUrlEncoded
  @POST("set/profile/{number}/{code}")
  Observable<BaseRequestBean> updateUserBio(@Path("number") String number,
      @Path("code") String code, @Field("bio") String bio);

  /**
   * 获取排名信息
   * @param number
   * @param code
   * @return
   */
  @GET("Get/rank/{number}/{code}")
  Observable<BaseRequestBean> getRankInfo(@Path("number") String number, @Path("code") String code);

  /**
   * 获取所有成绩
   * @param number
   * @param code
   * @return
   */
  @GET("Get/score/{number}/{code}")
  Observable<GradeBean> getGradeInfo(@Path("number") String number, @Path("code") String code);

  /**
   * 获取考试计划信息
   * @param number
   * @param code
   * @return
   */
  @GET("home/examination_wechat/{number}/{code}")
  Observable<ExamBean> getExam(@Path("number") String number,
      @Path("code") String code);

  /**
   * 查询电费
   * @param part
   * @param locate
   * @param room
   * @param number
   * @param code
   * @param enc
   * @return
   */
  @GET("get/power_e/{part}/{locate}/{room}/{number}/{code}/{enc}")
  Observable<PowerBean> getPowerInfo(@Path("part") String part, @Path("locate") String locate,
      @Path("room") String room, @Path("number") String number, @Path("code") String code,
      @Path("enc") String enc);


  //@GET("home/lessonsExp_Wechat/{number}/{code}")


}
