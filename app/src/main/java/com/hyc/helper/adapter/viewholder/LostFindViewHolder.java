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
import com.hyc.helper.activity.PictureBrowsingActivity;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.view.ImageLayout;
import java.util.ArrayList;

public class LostFindViewHolder extends BaseViewHolder<LostBean.GoodsBean>
    implements ImageLayout.OnItemClickListener {

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
  @BindView(R.id.tv_delete)
  TextView tvDelete;
  private UserModel userModel = new UserModel();

  public LostFindViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this, itemView);
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
    ivLostImage.setOnItemClickListener(this);
    if (userModel.getCurUserInfo().getData().getUser_id() == Integer.parseInt(data.getUser_id())) {
      tvDelete.setVisibility(View.VISIBLE);
    } else {
      tvDelete.setVisibility(View.GONE);
    }
  }

  @Override
  public void onItemImageClick(int position) {
    if (itemView.getContext() != null) {
      PictureBrowsingActivity.goToPictureBrowsingActivity(ivLostImage.getContext(), position,
          (ArrayList<String>) getData().getPics());
    }
  }

  @Override
  public void setOnClickListener(View.OnClickListener onClickListener) {
    super.setOnClickListener(onClickListener);
    tvDelete.setOnClickListener(onClickListener);
  }
}
