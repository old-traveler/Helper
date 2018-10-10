package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.view.ImageLayout;

public class LostFindViewHolder extends BaseViewHolder<LostBean.GoodsBean> {

  @BindView(R.id.tv_content)
  TextView tvContent;
  @BindView(R.id.tv_username)
  TextView tvUsername;
  @BindView(R.id.tv_date)
  TextView tvDate;
  @BindView(R.id.iv_lost_image)
  ImageLayout ivLostImage;
  @BindView(R.id.tv_title)
  TextView tvTitle;
  @BindView(R.id.tv_location)
  TextView tvLocation;
  @BindView(R.id.cv_lost)
  CardView cvLost;

  public LostFindViewHolder(@NonNull View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  @Override
  protected void initItemView(View view) {
  }

  @Override
  public void loadItemData(Context context, LostBean.GoodsBean data, int position) {
    StaggeredGridLayoutManager.LayoutParams layoutParams = (
        StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
    if ((position + 1) % 2 == 0) {
      UiHelper.setViewMargin(cvLost, true, 5, 5, 10, 0);
    } else {
      UiHelper.setViewMargin(cvLost, true, 5, 5, 10, 0);
    }
    itemView.setLayoutParams(layoutParams);
    tvTitle.setText(data.getTit());
    tvLocation.setText(data.getLocate());
    tvContent.setText(data.getContent());
    tvUsername.setText(data.getUsername());
    tvDate.setText(data.getTime());
    ivLostImage.setImageListUrl(data.getPics());
  }
}
