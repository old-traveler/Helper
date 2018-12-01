package com.hyc.helper.model;

import android.annotation.SuppressLint;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.CalendarBean;
import com.hyc.helper.bean.ExamBean;
import com.hyc.helper.bean.ExamInfoBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.DbDeleteHelper;
import com.hyc.helper.helper.DbInsertHelper;
import com.hyc.helper.helper.DbSearchHelper;
import com.hyc.helper.helper.LogHelper;
import com.hyc.helper.helper.RequestHelper;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamModel {

  public void getExam(UserBean userBean, Observer<ExamBean> observer) {
    RequestHelper.getRequestApi()
        .getExam(userBean.getData().getStudentKH(), userBean.getRemember_code_app())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  private Disposable insertExamIntoDb(List<ExamInfoBean> examInfoBeans) {
    return DbInsertHelper.insertExamInfo(examInfoBeans)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> LogHelper.log("插入数据完成"));
  }

  public Disposable refreshLocalDb(List<ExamInfoBean> examInfoBeans) {
    return DbDeleteHelper.deleteUserExamInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(aBoolean -> insertExamIntoDb(examInfoBeans));
  }

  public void getExamInfoFromCache(Observer<ExamBean> observer) {
    getExamInfoFromCache()
        .subscribe(observer);
  }

  public Observable<ExamBean> getExamInfoFromCache() {
    return DbSearchHelper.searchExamInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public Disposable deleteExamInfoFromCache() {
    return DbDeleteHelper.deleteUserExamInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe();
  }

  public List<CalendarBean> toCalendar(ExamBean examBean) {
    List<CalendarBean> calendarBeans = new ArrayList<>();
    if (examBean != null && examBean.getStatus().equals("success")) {
      for (ExamInfoBean examInfoBean : examBean.getRes().getExam()) {
        String[] temp = null;
        try {
          temp = examInfoBean.getStarttime().split(" ");
        } catch (Exception e) {
          e.printStackTrace();
        }
        if (temp == null || temp.length < 2) {
          continue;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
          date = formatter.parse(temp[0]);
        } catch (ParseException e) {
          e.printStackTrace();
        }
        if (date != null) {
          long distance = date.getTime() - System.currentTimeMillis();
          if (distance < 0) {
            continue;
          } else if (distance >= Constant.ONE_DAY_TIME) {
            CalendarBean calendarBean = new CalendarBean();
            int day = (int) (distance / Constant.ONE_DAY_TIME);
            calendarBean.setDays(-day);
            calendarBean.setDate(String.format(UiHelper.getString(R.string.day_tip),
                day));
            calendarBean.setName(examInfoBean.getCourseName());
            calendarBeans.add(calendarBean);
          } else {
            CalendarBean calendarBean = new CalendarBean();
            calendarBean.setDays(0);
            calendarBean.setName(examInfoBean.getCourseName());
            calendarBean.setDate(String.format(UiHelper.getString(R.string.hour_tip),
                (int) (distance / Constant.ONE_HOUR_TIME)));
            calendarBeans.add(calendarBean);
          }
        }
      }
    }
    return calendarBeans;
  }
}
