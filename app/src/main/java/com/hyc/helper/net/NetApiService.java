package com.hyc.helper.net;

import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.CalendarBean;
import com.hyc.helper.bean.ClassCourseBean;
import com.hyc.helper.bean.ConfigureDateBean;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.ExamBean;
import com.hyc.helper.bean.FindPeopleBean;
import com.hyc.helper.bean.GoodsDetailBean;
import com.hyc.helper.bean.GradeBean;
import com.hyc.helper.bean.ImageUploadBean;
import com.hyc.helper.bean.LessonsExpBean;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.bean.PowerBean;
import com.hyc.helper.bean.SecondHandBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.UpdateApkBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.bean.UserInfoBean;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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
   *
   * @param number 学号
   * @param password 密码
   */
  @GET("get/loginWx/{number}/{password}/0")
  Observable<UserBean> login(@Path("number") String number, @Path("password") String password);

  /**
   * 查询课程表
   *
   * @param number 学号
   * @param code code
   */
  @GET("get/schedule/{number}/{code}")
  Observable<CourseBean> getSchedule(@Path("number") String number, @Path("code") String code);

  /**
   * 查询校园说说
   *
   * @param number 学号
   * @param page 页号
   */
  @GET("statement/statement/{number}/{page}")
  Observable<StatementBean> getStatement(@Path("number") String number, @Path("page") int page);

  /**
   * 赞说说
   *
   * @param number 学号
   * @param code code
   * @param statementId 说说id
   */
  @GET("statement/like/{number}/{code}/{statement_id}")
  Observable<BaseRequestBean> likeStatement(
      @Path("number") String number, @Path("code") String code,
      @Path("statement_id") String statementId);

  /**
   * 查询二手市场
   *
   * @param page 页号
   */
  @GET("trade/goods/{page}/0")
  Observable<SecondHandBean> getSecondHandMaker(@Path("page") int page);

  /**
   * 获取二手商品详情
   *
   * @param number 学号
   * @param code code
   * @param goodsId 商品id
   */
  @GET("trade/details/{number}/{code}/{goodsId}")
  Observable<GoodsDetailBean> getGoodsDetailInfo(
      @Path("number") String number, @Path("code") String code, @Path("goodsId") String goodsId);

  /**
   * 查询失物招领
   *
   * @param page 页号
   */
  @GET("loses/goods/{page}/0")
  Observable<LostBean> getLostAndFind(@Path("page") int page);

  /**
   * 获取应用配置信息，静态Api
   */
  @GET("https://raw.githubusercontent.com/old-traveler/Helper/master/api/date.json")
  Observable<ConfigureDateBean> getConfigure();

  /**
   * 获取应用更新信息，静态Api
   */
  @GET("https://raw.githubusercontent.com/old-traveler/Helper/master/api/update.json")
  Observable<UpdateApkBean> getUpdateApkInfo();

  /**
   * 评论说说
   *
   * @param comment 评论
   * @param moment_id 说说id
   */
  @FormUrlEncoded
  @POST("statement/comment/{number}/{code}")
  Observable<BaseRequestBean> commentStatement(@Path("number") String number,
      @Path("code") String code, @Field("comment") String comment,
      @Field("moment_id") String moment_id);

  /**
   * 发布说说
   *
   * @param number 学号
   * @param content 内容
   * @param hidden 照片
   */
  @FormUrlEncoded
  @POST("statement/create/{number}/{code}")
  Observable<BaseRequestBean> publishStatement(@Path("number") String number,
      @Path("code") String code, @Field("content") String content, @Field("user_id") String userId,
      @Field("hidden") String hidden);

  /**
   * 上传照片
   *
   * @param env sha1验证码
   * @param type 上传类型
   * @param file 照片
   */
  @Multipart
  @POST("https://pic.htmln.com/api/v3/upload/images/{number}/{code}/{env}/{type}")
  Observable<ImageUploadBean> uploadImage(@Path("number") String number, @Path("code") String code,
      @Path("env") String env, @Path("type") String type, @Part
      MultipartBody.Part file);

  /**
   * 删除说说
   */
  @GET("statement/deleteWechat/{number}/{code}/{momend_id}")
  Observable<BaseRequestBean> deleteStatement(@Path("number") String number,
      @Path("code") String code, @Path("momend_id") String momend_id);

  /**
   * 找人
   */
  @FormUrlEncoded
  @POST("im/get_students/{number}/{code}/{env}")
  Observable<FindPeopleBean> findPeople(@Path("number") String number, @Path("code") String code,
      @Path("env") String env, @Field("name") String name);

  /**
   * 获取某人发布的说说
   */
  @GET("statement/statement/{number}/{page}/{user_id}")
  Observable<StatementBean> getUserStatement(@Path("number") String number, @Path("page") int page,
      @Path("user_id") String userId);

  /**
   * 获取某人发布的二手
   */
  @GET("trade/own/{number}/{code}/{page}/{user_id}")
  Observable<SecondHandBean> getUserSecondMaker(@Path("number") String number,
      @Path("code") String code, @Path("page") int page, @Path("user_id") String userId);

  /**
   * 获取默认发布的失物招领
   */
  @GET("loses/own/{number}/{code}/{page}/{user_id}")
  Observable<LostBean> getUserLost(@Path("number") String number, @Path("code") String code,
      @Path("page") int page, @Path("user_id") String userId);

  /**
   * 更新用户名
   */
  @FormUrlEncoded
  @POST("set/profile/{number}/{code}")
  Observable<BaseRequestBean> updateUsername(@Path("number") String number,
      @Path("code") String code, @Field("username") String username);

  /**
   * 更新用户签名
   */
  @FormUrlEncoded
  @POST("set/profile/{number}/{code}")
  Observable<BaseRequestBean> updateUserBio(@Path("number") String number,
      @Path("code") String code, @Field("bio") String bio);

  /**
   * 获取所有成绩
   */
  @GET("Get/score/{number}/{code}")
  Observable<GradeBean> getGradeInfo(@Path("number") String number, @Path("code") String code);

  /**
   * 获取考试计划信息
   */
  @GET("home/examination_wechat/{number}/{code}")
  Observable<ExamBean> getExam(@Path("number") String number,
      @Path("code") String code);

  /**
   * 查询电费
   */
  @GET("get/power_e/{part}/{locate}/{room}/{number}/{code}/{enc}")
  Observable<PowerBean> getPowerInfo(@Path("part") String part, @Path("locate") String locate,
      @Path("room") String room, @Path("number") String number, @Path("code") String code,
      @Path("enc") String enc);

  /**
   * 通过用户id查找用户
   */
  @GET("set/user_info/{number}/{code}/{user_id}")
  Observable<UserInfoBean> findUserbyUserId(@Path("number") String number,
      @Path("code") String code, @Path("user_id") String userId);

  @GET("home/lessonsExp/{number}/{code}")
  Observable<LessonsExpBean> getLessonsExpBean(@Path("number") String number,
      @Path("code") String code);

  /**
   * @param params tit address prize content hidden phone attr type
   */
  @FormUrlEncoded
  @POST("trade/create/{number}/{code}")
  Observable<BaseRequestBean> createTrade(@Path("number") String number, @Path("code") String code,
      @FieldMap
          Map<String, String> params);

  @GET("trade/delete/{number}/{code}/{trade_id}")
  Observable<BaseRequestBean> deleteTrade(@Path("number") String number, @Path("code") String code,
      @Path("trade_id") String tradeId);

  /**
   * @param params tit locate time content hidden phone type
   */
  @FormUrlEncoded
  @POST("Loses/create/{number}/{code}")
  Observable<BaseRequestBean> createLoses(@Path("number") String number, @Path("code") String code,
      @FieldMap
          Map<String, String> params);

  @GET("Loses/delete/{number}/{code}/{id}")
  Observable<BaseRequestBean> deleteLoses(@Path("number") String number, @Path("code") String code,
      @Path("id") String id);

  @GET("get/calendar")
  Observable<List<CalendarBean>> getCalendar();

  @GET("get/classes/{number}/{code}")
  Observable<ClassCourseBean> getClassesInfo(@Path("number") String number,
      @Path("code") String code);

  @GET("get/class_lessons/{number}/{code}/{className}")
  Observable<CourseBean> getClassSchedule(@Path("number") String number, @Path("code") String code,
      @Path("className") String className);

  /**
   * 查询与当前用户相关的说说
   *
   * @param number 当前用户学号
   * @param code 当前用户登录口令
   * @param page 当前页数
   * @return 与当前用户相关的说说信息
   */
  @GET("statement/interactive/{number}/{code}/{page}")
  Observable<StatementBean> fetchInteractiveStatement(@Path("number") String number,
      @Path("code") String code, @Path("page") int page);

  /**
   * 通过关键字搜索说说信息
   *
   * @param number 当前用户学号
   * @param code 当前用户登录口令
   * @param keyWord 搜索关键词
   * @param page 当前页数
   * @return 包含此关键字的说说信息
   */
  @GET("statement/search/{number}/{code}/{key_word}/{page}")
  Observable<StatementBean> searchStatement(@Path("number") String number,
      @Path("code") String code, @Path("key_word") String keyWord, @Path("page") int page);

  @GET("statement/fire/{number}/7/{page}")
  Observable<StatementBean> fetchFireStatement(@Path("number") String number,
      @Path("page") int page);
}
