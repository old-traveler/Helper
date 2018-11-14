package com.hyc.helper.activity;

import android.os.Bundle;
import android.text.TextUtils;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.ExamViewHolder;
import com.hyc.helper.base.activity.BaseListActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.ExamBean;
import com.hyc.helper.bean.ExamInfoBean;
import com.hyc.helper.model.ExamModel;
import com.hyc.helper.model.UserModel;
import java.util.List;

public class ExamActivity extends BaseListActivity<ExamInfoBean,ExamBean,ExamViewHolder> {

  private ExamModel examModel = new ExamModel();
  private UserModel userModel = new UserModel();
  private boolean isFirst = true;
  private boolean isNeedFresh = false;

  @Override
  protected BaseRecycleAdapter<ExamInfoBean, ExamViewHolder> setRecycleAdapter() {
    return new BaseRecycleAdapter<>(R.layout.item_examtion_info,ExamViewHolder.class);
  }

  @Override
  protected int getRefreshLayoutId() {
    return R.id.sw_exam;
  }

  @Override
  protected int getRecycleViewId() {
    return R.id.rv_exam;
  }


  @Override
  protected void requestListData(int page) {
    if (isFirst){
      isFirst = false;
      examModel.getExamInfoFromCache(this);
    }else {
      isNeedFresh = true;
      examModel.getExam(userModel.getCurUserInfo(),this);
    }
  }

  @Override
  public void onNext(ExamBean ts) {
    if (ts.getStatus().equals("need_api")){
      dispose();
      requestListData(0);
    } else if (ts.getStatus().equals("success")) {
      if (isNeedFresh){
        examModel.refreshLocalDb(ts.getRes().getExam());
      }
      loadMoreFinish(getData(ts));
    } else if (!TextUtils.isEmpty(ts.getMsg())) {
      ToastHelper.toast(ts.getMsg());
    } else {
      ToastHelper.toast(String.valueOf(ts.getCode()));
    }
  }

  @Override
  protected List<ExamInfoBean> getData(ExamBean examBean) {
    return examBean.getRes().getExam();
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_examtion;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    setToolBarTitle("Examination Plan");
    setEnableLoadMore(false);
  }
}
