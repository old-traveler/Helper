package com.hyc.helper.model;

import android.annotation.SuppressLint;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.CourseInfoBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.DbDeleteHelper;
import com.hyc.helper.helper.DbInsertHelper;
import com.hyc.helper.helper.DbSearchHelper;
import com.hyc.helper.helper.LogHelper;
import com.hyc.helper.helper.RequestHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class CourseModel {

  public void getCourseFromApi(UserBean userBean, Observer<CourseBean> observer) {
    String number = userBean.getData().getStudentKH();
    String code = userBean.getRemember_code_app();
    RequestHelper.getRequestApi().getSchedule(number, code)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void getCourseFromCache(String studentId, Observer<CourseBean> observer) {
    DbSearchHelper.searchCourseInfo(studentId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  @SuppressLint("CheckResult")
  public void insertCourseIntoDb(List<CourseInfoBean> courseInfoBeans) {
    DbInsertHelper.insertCourseInfo(courseInfoBeans).subscribeOn(Schedulers.io())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> LogHelper.log("插入数据完成"));
  }

  @SuppressLint("CheckResult")
  public void refreshLocalDb(List<CourseInfoBean> courseInfoBeans) {
    DbDeleteHelper.deleteUserCourseInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(aBoolean -> insertCourseIntoDb(courseInfoBeans));
  }
}
