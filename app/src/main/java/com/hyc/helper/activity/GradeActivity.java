package com.hyc.helper.activity;

import android.os.Bundle;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.GradeViewHolder;
import com.hyc.helper.base.activity.BaseListActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.bean.GradeBean;
import com.hyc.helper.bean.GradeInfoBean;
import com.hyc.helper.model.GradeModel;
import com.hyc.helper.model.UserModel;
import java.util.Collections;
import java.util.List;

public class GradeActivity extends BaseListActivity<GradeInfoBean,GradeBean,GradeViewHolder> {

  private GradeModel gradeModel = new GradeModel();
  private UserModel userModel = new UserModel();
  private boolean isFirst = true;
  private boolean isNeedFresh = false;

  @Override
  protected BaseRecycleAdapter<GradeInfoBean, GradeViewHolder> setRecycleAdapter() {
    return new BaseRecycleAdapter<>(R.layout.item_grade,GradeViewHolder.class);
  }

  @Override
  protected int getRefreshLayoutId() {
    return R.id.sw_grade;
  }

  @Override
  protected int getRecycleViewId() {
    return R.id.rv_grade;
  }

  @Override
  protected void requestListData(int page) {
    if (isFirst){
      isFirst = false;
      gradeModel.getGradeInfoFromCache(this);
    }else {
      isNeedFresh = true;
      gradeModel.getGradeFromApi(userModel.getCurUserInfo(),this);
    }
  }

  @Override
  protected List<GradeInfoBean> getData(GradeBean gradeBean) {
    if (isNeedFresh){
      gradeModel.refreshLocalDb(gradeBean.getData());
    }
    Collections.sort(gradeBean.getData());
    return gradeBean.getData();
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_grade;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    setToolBarTitle(R.string.my_grade);
    setEnableLoadMore(false);
  }
}
