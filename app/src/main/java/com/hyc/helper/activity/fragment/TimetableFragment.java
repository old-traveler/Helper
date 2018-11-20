package com.hyc.helper.activity.fragment;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hyc.helper.R;
import com.hyc.helper.activity.CourseDetailActivity;
import com.hyc.helper.base.fragment.BaseRequestFragment;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.CourseInfoBean;
import com.hyc.helper.bean.LessonsExpBean;
import com.hyc.helper.helper.DateHelper;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.model.CourseModel;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.view.CourseTableLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

public class TimetableFragment extends BaseRequestFragment<CourseBean> implements
    OnRefreshListener {

  Unbinder unbinder;
  @BindView(R.id.srl_timetable)
  SmartRefreshLayout srlTimetable;
  @BindView(R.id.ll_top_title)
  LinearLayout llTopTitle;
  @BindView(R.id.ll_left_tip)
  LinearLayout llLeftTip;
  @BindView(R.id.ctl_course)
  CourseTableLayout ctlCourse;
  @BindView(R.id.tv_month)
  TextView tvMonth;
  private CourseModel courseModel;
  private UserModel userModel;
  private boolean needRefreshDb = false;
  private int curWeek;
  private LessonsExpBean lessonsExpBean;

  @Override
  protected void initLayoutView(View view) {
    unbinder = ButterKnife.bind(this, view);
    courseModel = new CourseModel();
    userModel = new UserModel();
    srlTimetable.setEnableRefresh(true);
    srlTimetable.setEnableLoadMore(false);
    srlTimetable.setOnRefreshListener(this);
    curWeek = DateHelper.getCurWeek();
    initTopTitle(curWeek);
    initLeftTip();
    ctlCourse.setOnItemClickListener(
        (position, infoBean) -> CourseDetailActivity.startCourseDetail(getActivity(), infoBean));
  }

  private void initLeftTip() {
    String[] section = UiHelper.getStringArrays(R.array.section);
    int itemHeight = DensityUtil.dip2px(120f);
    for (String s : section) {
      TextView textView = new TextView(getContext());
      textView.setText(s);
      textView.setGravity(Gravity.CENTER);
      textView.setTextSize(11);
      textView.setTextColor(UiHelper.getColor(R.color.front_black));
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight, 0);
      textView.setLayoutParams(params);
      llLeftTip.addView(textView);
    }
  }

  private void initTopTitle(int week) {
    while (llTopTitle.getChildCount() > 1) {
      llTopTitle.removeViewAt(0);
    }
    String[] weeks = UiHelper.getStringArrays(R.array.weeks);
    int index = 0;
    int curDay = DateHelper.getCurDay() - 1;
    int day[] = DateHelper.getCurDayOfWeek(week);
    for (int i = 0; i < 7; i++) {
      TextView textView = new TextView(getContext());
      textView.setText(String.format(UiHelper.getString(R.string.date_tip), weeks[i], day[i]));
      textView.setGravity(Gravity.CENTER);
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
      textView.setLayoutParams(params);
      textView.setTextSize(11);
      textView.setTextColor(UiHelper.getColor(R.color.front_black));
      if (index == curDay) {
        initCurWeekText(textView, week);
      }
      llTopTitle.addView(textView, index++);
    }
    tvMonth.setText(String.format(UiHelper.getString(R.string.month), day[7]));
  }

  private void initCurWeekText(TextView textView, int week) {
    if (week == DateHelper.getCurWeek()) {
      textView.setTextColor(UiHelper.getColor(R.color.white));
      textView.setBackgroundColor(UiHelper.getColor(R.color.colorPrimary));
    } else {
      textView.setTextColor(UiHelper.getColor(R.color.front_black));
      textView.setBackgroundColor(UiHelper.getColor(R.color.white));
    }
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_time_table;
  }

  @Override
  protected void requestDataFromApi() {
    needRefreshDb = true;
    addDisposable(courseModel.getLessonsExpFromApi(userModel.getCurUserInfo()).subscribe(
        lessonsExpBean -> {
          this.lessonsExpBean = lessonsExpBean;
          courseModel.getCourseFromApi(userModel.getCurUserInfo(), this);
        },
        this::onError));
  }

  @Override
  protected boolean requestDataFromDb() {
    courseModel.getCourseFromCache(userModel.getStudentId(), this);
    return true;
  }

  @Override
  protected void onSuccessGetData(CourseBean courseBean) {
    List<CourseInfoBean> courseInfoBeans = courseBean.getData();
    if (needRefreshDb) {
      needRefreshDb = false;
      courseInfoBeans.addAll(courseModel.lessonsToCourse(userModel.getStudentId(), lessonsExpBean));
      courseModel.refreshLocalDb(courseInfoBeans);
    }
    refreshCourseInfo(courseInfoBeans);
    srlTimetable.finishRefresh();
  }

  @Override
  protected void onFailGetData(Throwable e) {
    super.onFailGetData(e);
    srlTimetable.finishRefresh();
  }

  private void refreshCourseInfo(List<CourseInfoBean> infoBeans) {
    ctlCourse.setCourseTableInfo(infoBeans, curWeek);
    initTopTitle(curWeek);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  public void switchWeek(int week) {
    curWeek = week;
    initTopTitle(week);
    ctlCourse.switchWeek(week);
  }

  @Override
  public void onRefresh(@NonNull RefreshLayout refreshLayout) {
    requestDataFromApi();
  }
}
