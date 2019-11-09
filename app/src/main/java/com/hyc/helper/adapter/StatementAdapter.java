package com.hyc.helper.adapter;

import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import com.hyc.helper.adapter.viewholder.StatementViewHolder;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.bean.StatementInfoBean;
import java.util.List;

public class StatementAdapter extends BaseRecycleAdapter<StatementInfoBean, StatementViewHolder> {

  private SparseIntArray sparseIntArray;

  public StatementAdapter(
      List<StatementInfoBean> dataList, int layoutId,
      Class<StatementViewHolder> statementViewHolderClass) {
    super(dataList, layoutId, statementViewHolderClass);
    sparseIntArray = new SparseIntArray();
  }

  @Override
  public void onBindViewHolder(@NonNull StatementViewHolder baseViewHolder, int i) {
    baseViewHolder.setSparseBooleanArray(sparseIntArray);
    super.onBindViewHolder(baseViewHolder, i);
  }

  @Override
  public void setDataList(List<StatementInfoBean> dataList) {
    super.setDataList(dataList);
    sparseIntArray = new SparseIntArray();
  }
}
