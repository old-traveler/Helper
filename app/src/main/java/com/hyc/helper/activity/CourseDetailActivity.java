package com.hyc.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.CourseDataViewHolder;
import com.hyc.helper.adapter.viewholder.ShowInfoViewHolder;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.bean.CourseInfoBean;
import com.hyc.helper.bean.InfoEntity;
import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends BaseActivity {

  @BindView(R.id.rv_date)
  RecyclerView rvDate;
  @BindView(R.id.rv_course_info)
  RecyclerView rvCourseInfo;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_course_detail;
  }

  public static void startCourseDetail(Context context, @NonNull CourseInfoBean courseInfoBean) {
    Bundle bundle = new Bundle();
    bundle.putSerializable("data", courseInfoBean);
    Intent intent = new Intent(context, CourseDetailActivity.class);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBarTitle(R.string.course_detail);
    CourseInfoBean bean = (CourseInfoBean) bundle.getSerializable("data");
    initCourseDateInfo(bean);
    if (bean != null){
      initCourseInfo(bean);
    }
  }

  private void initCourseInfo(CourseInfoBean bean) {
    List<InfoEntity> list = new ArrayList<>();
    list.add(new InfoEntity("课程名称", bean.getName()));
    list.add(new InfoEntity("上课时间", String.format("周%s %s %s节", bean.getXqj(), bean.getDjj(),
        String.valueOf(Integer.valueOf(bean.getDjj()) + 1))));
    list.add(new InfoEntity("上课教室", bean.getRoom()));
    list.add(new InfoEntity("授课教师", bean.getTeacher()));
    rvCourseInfo.setLayoutManager(new GridLayoutManager(this, 2));
    rvCourseInfo.setAdapter(
        new BaseRecycleAdapter<>(list, R.layout.layout_bottom_info, ShowInfoViewHolder.class));
  }

  private void initCourseDateInfo(CourseInfoBean bean) {
    List<Boolean> data = new ArrayList<>(20);
    for (int i = 0; i < 20; i++) {
      data.add(false);
    }
    for (Integer integer : bean.getZs()) {
      data.set(integer - 1, true);
    }
    rvDate.setLayoutManager(new GridLayoutManager(this, 5));
    rvDate.setAdapter(
        new BaseRecycleAdapter<>(data, R.layout.item_course_date, CourseDataViewHolder.class));
  }
}
