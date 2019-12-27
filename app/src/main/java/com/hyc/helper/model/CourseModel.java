package com.hyc.helper.model;

import android.util.Pair;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.ClassCourseBean;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.CourseInfoBean;
import com.hyc.helper.bean.LessonsExpBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.DbDeleteHelper;
import com.hyc.helper.helper.DbInsertHelper;
import com.hyc.helper.helper.DbSearchHelper;
import com.hyc.helper.helper.LogHelper;
import com.hyc.helper.helper.RequestHelper;
import com.hyc.helper.helper.SpCacheHelper;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    getCourseFromCache(studentId)
        .subscribe(observer);
  }

  private Observable<CourseBean> getCourseFromCache(String studentId) {
    return DbSearchHelper.searchCourseInfo(studentId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Disposable insertCourseIntoDb(List<CourseInfoBean> courseInfoBeans) {
    return DbInsertHelper.insertCourseInfo(courseInfoBeans)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> LogHelper.log("插入数据完成"));
  }

  public Disposable refreshLocalDb(List<CourseInfoBean> courseInfoBeans) {
    return DbDeleteHelper.deleteUserCourseInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(aBoolean -> insertCourseIntoDb(courseInfoBeans));
  }

  public Disposable clearLocalDb() {
    return DbDeleteHelper.deleteUserCourseInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe();
  }

  public Observable<LessonsExpBean> getLessonsExpFromApi(UserBean userBean) {
    return RequestHelper.getRequestApi()
        .getLessonsExpBean(userBean.getData().getStudentKH(), userBean.getRemember_code_app())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public List<CourseInfoBean> lessonsToCourse(String studentId, LessonsExpBean lessonsExpBean) {
    List<CourseInfoBean> courseInfoBeans = new ArrayList<>();
    if (lessonsExpBean.getCode() == 200) {
      HashMap<String, CourseInfoBean> hashMap = new HashMap<>();
      for (LessonsExpBean.DataBean dataBean : lessonsExpBean.getData()) {
        String key = dataBean.getLesson() + dataBean.getWeek() + dataBean.getReal_time();
        if (hashMap.get(key) == null) {
          CourseInfoBean courseInfoBean = new CourseInfoBean();
          courseInfoBean.setRoom(dataBean.getLocate());
          courseInfoBean.setName(dataBean.getLesson());
          courseInfoBean.setXqj(dataBean.getWeek());
          courseInfoBean.setTeacher(dataBean.getTeacher());
          courseInfoBean.setDjj(dataBean.getReal_time());
          courseInfoBean.setDsz(Integer.parseInt(dataBean.getPeriod()));
          courseInfoBean.setXh(studentId);
          List<Integer> weeks = new ArrayList<>();
          weeks.add(Integer.valueOf(dataBean.getWeeks_no()));
          courseInfoBean.setZs(weeks);
          hashMap.put(key, courseInfoBean);
        } else {
          hashMap.get(key).getZs().add(Integer.valueOf(dataBean.getWeeks_no()));
        }
      }

      for (HashMap.Entry<String, CourseInfoBean> entry : hashMap.entrySet()) {
        CourseInfoBean courseInfoBean = entry.getValue();
        int hour = Integer.parseInt(courseInfoBean.getDjj().split(":")[0]);
        if (hour == 8) {
          courseInfoBean.setDjj("1");
        } else if (hour == 10) {
          courseInfoBean.setDjj("3");
        } else if (hour == 14) {
          courseInfoBean.setDjj("5");
        } else if (hour == 16) {
          courseInfoBean.setDjj("7");
        } else if (hour == 19) {
          courseInfoBean.setDjj("9");
        } else {
          throw new RuntimeException("no this time" + courseInfoBean.getDjj());
        }
        if (courseInfoBean.getDsz() == 4) {
          CourseInfoBean clone = (CourseInfoBean) courseInfoBean.clone();
          clone.setDjj(String.valueOf(Integer.parseInt(courseInfoBean.getDjj()) + 2));
          courseInfoBeans.add(clone);
        }
        courseInfoBeans.add(courseInfoBean);
      }
      return courseInfoBeans;
    }
    return courseInfoBeans;
  }

  public Observable<List<Pair<String, List<String>>>> getClassListInfo(String number, String code) {
    return RequestHelper.getRequestApi()
        .getClassesInfo(number, code)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(this::jsonToList);
  }

  public List<Pair<String, List<String>>> jsonToList(ClassCourseBean classCourseBean)
      throws JSONException {
    SpCacheHelper.putClassIntoSp("ClassCourseBean",classCourseBean);
    List<Pair<String, List<String>>> list = new ArrayList<>();
    if (classCourseBean.getCode() != 200 || classCourseBean.getData() == null) return list;
    JSONObject jsonObject = new JSONObject(classCourseBean.getData().toString());
    String[] depLists = UiHelper.getStringArrays(R.array.depLists);
    for (String depName : depLists) {
      JSONArray jsonArray = jsonObject.optJSONArray(depName);
      if (jsonArray == null || jsonArray.length() == 0) {
        continue;
      }
      List<String> className = new ArrayList<>();
      for (int i = 0; i < jsonArray.length(); i++) {
        className.add(jsonArray.getString(i));
      }
      list.add(new Pair<>(depName, className));
    }
    return list;
  }

  public Observable<CourseBean> getClassCourse(String number, String code, String className) {
    return RequestHelper.getRequestApi().getClassSchedule(number, code, className)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
