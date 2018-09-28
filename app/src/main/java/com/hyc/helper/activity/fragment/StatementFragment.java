package com.hyc.helper.activity.fragment;

import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.StatementViewHolder;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.model.StatementModel;
import com.hyc.helper.model.UserModel;
import java.util.List;

public class StatementFragment extends
    BaseListFragment<StatementBean.StatementInfoBean,StatementBean,StatementViewHolder> {
  private UserModel userModel = new UserModel();
  private StatementModel statementModel = new StatementModel();

  @Override
  protected BaseRecycleAdapter<StatementBean.StatementInfoBean, StatementViewHolder> getRecycleAdapter() {
    return new BaseRecycleAdapter<>(null, R.layout.item_statement,StatementViewHolder.class);
  }

  @Override
  protected int getRefreshLayoutId() {
    return R.id.sfl_statement;
  }

  @Override
  protected int getRecycleViewId() {
    return R.id.rv_statement;
  }

  @Override
  protected void requestListData(int page) {
    statementModel.getStatementByPage(page,userModel.getStudentId(),this);
  }

  @Override
  protected List<StatementBean.StatementInfoBean> getData(StatementBean statementBean) {
    return statementBean.getStatement();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_statement;
  }
}
