package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.util.UiHelper;

public class CourseDataViewHolder extends BaseViewHolder<Boolean> {

  @BindView(R.id.tv_number)
  TextView tvNumber;
  @BindView(R.id.cv_date)
  CardView cvDate;

  public CourseDataViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this, view);
  }

  @Override
  public void loadItemData(Context context, Boolean data, int position) {
    if (data) {
      cvDate.setCardBackgroundColor(UiHelper.getColor(R.color.colorPrimary));
    } else {
      cvDate.setCardBackgroundColor(UiHelper.getColor(R.color.bg_no));
    }
    tvNumber.setText(String.valueOf(position + 1));
  }
}
