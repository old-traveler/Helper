package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.bean.FindPeopleBean;
import com.hyc.helper.helper.ImageRequestHelper;

public class SearchPeopleViewHolder extends BaseViewHolder<FindPeopleBean.DataBean> {

  @BindView(R.id.iv_search_profile)
  ImageView ivSearchProfile;
  @BindView(R.id.tv_search_name)
  TextView tvSearchName;

  public SearchPeopleViewHolder(@NonNull View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  @Override
  protected void initItemView(View view) {

  }

  @Override
  public void loadItemData(Context context, FindPeopleBean.DataBean data, int position) {
    ImageRequestHelper.loadHeadImage(context,data.getHead_pic_thumb(),ivSearchProfile);
    tvSearchName.setText(String.format("%s/%s", data.getClass_name(), data.getDep_name()));
  }
}
