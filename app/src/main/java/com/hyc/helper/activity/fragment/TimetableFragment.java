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
import com.hyc.helper.base.fragment.BaseRequestFragment;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.CourseInfoBean;
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
  private CourseModel courseModel;
  private UserModel userModel;
  private boolean needRefreshDb = false;
  private TextView tvCurWeek;

  @Override
  protected void initLayoutView(View view) {
    unbinder = ButterKnife.bind(this, view);
    courseModel = new CourseModel();
    userModel = new UserModel();
    srlTimetable.setEnableRefresh(true);
    srlTimetable.setEnableLoadMore(false);
    srlTimetable.setOnRefreshListener(this);
    initTopTitle();
    initLeftTip();
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
          new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,itemHeight,0);
      textView.setLayoutParams(params);
      llLeftTip.addView(textView);
    }
  }

  private void initTopTitle() {
    String[] weeks = UiHelper.getStringArrays(R.array.weeks);
    int index = 0;
    int curDay = DateHelper.getCurDay()-1;
    for (String week : weeks) {
      TextView textView = new TextView(getContext());
      textView.setText(week);
      textView.setGravity(Gravity.CENTER);
      LinearLayout.LayoutParams params =
          new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1);
      textView.setLayoutParams(params);
      textView.setTextSize(11);
      textView.setTextColor(UiHelper.getColor(R.color.front_black));
      if (index == curDay){
        initCurWeekText(textView);
        this.tvCurWeek = textView;
      }
      llTopTitle.addView(textView,index++);
    }
  }

  private void initCurWeekText(TextView textView){
    textView.setTextSize(15);
    textView.setTextColor(UiHelper.getColor(R.color.white));
    textView.setBackgroundColor(UiHelper.getColor(R.color.colorPrimary));
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_time_table;
  }

  @Override
  protected void requestDataFromApi() {
    needRefreshDb = true;
    courseModel.getCourseFromApi(userModel.getCurUserInfo(), this);
  }

  @Override
  protected boolean requestDataFromDb() {
    courseModel.getCourseFromCache(userModel.getStudentId(), this);
    return true;
  }

  @Override
  protected void onSuccessGetData(CourseBean courseBean) {
    if (needRefreshDb) {
      needRefreshDb = false;
      courseModel.refreshLocalDb(courseBean.getData());
    }
    refreshCourseInfo(courseBean.getData());
    srlTimetable.finishRefresh();
  }

  @Override
  protected void onFailGetData(Throwable e) {
    super.onFailGetData(e);
    srlTimetable.finishRefresh();
  }

  private void refreshCourseInfo(List<CourseInfoBean> infoBeans) {
    ctlCourse.setCourseTableInfo(infoBeans, DateHelper.getCurWeek());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  public void switchWeek(int week){
    if (week == DateHelper.getCurWeek()){
      initCurWeekText(tvCurWeek);
    }else {
      tvCurWeek.setTextSize(11);
      tvCurWeek.setTextColor(UiHelper.getColor(R.color.front_black));
      tvCurWeek.setBackgroundColor(UiHelper.getColor(R.color.white));
    }
    ctlCourse.switchWeek(week);
  }

  @Override
  public void onRefresh(@NonNull RefreshLayout refreshLayout) {
    requestDataFromApi();
  }

}
