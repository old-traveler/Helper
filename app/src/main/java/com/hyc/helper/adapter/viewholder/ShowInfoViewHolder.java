package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.bean.InfoEntity;

public class ShowInfoViewHolder extends BaseViewHolder<InfoEntity> {

  @BindView(R.id.tv_name)
  TextView tvName;
  @BindView(R.id.tv_value)
  TextView tvValue;

  public ShowInfoViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this,view);
  }

  @Override
  public void loadItemData(Context context, InfoEntity data, int position) {
    tvName.setText(data.getTitle());
    tvValue.setText(data.getValue());
  }
}
