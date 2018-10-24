package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.bean.GradeInfoBean;

public class GradeViewHolder extends BaseViewHolder<GradeInfoBean> {

  @BindView(R.id.tv_name)
  TextView tvName;
  @BindView(R.id.tv_date)
  TextView tvDate;
  @BindView(R.id.tv_gpa)
  TextView tvGpa;
  @BindView(R.id.tv_score)
  TextView tvScore;

  public GradeViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this,view);
  }

  @Override
  public void loadItemData(Context context, GradeInfoBean data, int position) {
    tvName.setText(data.getKcmc());
    tvDate.setText(String.format("%s学年第%s学期", data.getXn(), data.getXq()));
    tvGpa.setText(String.format("学分&绩点：%s&%s",data.getXf(),data.getJd()));
    tvScore.setText(data.getCj());
  }

}
