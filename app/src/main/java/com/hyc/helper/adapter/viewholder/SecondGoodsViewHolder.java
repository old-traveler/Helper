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
import com.hyc.helper.bean.SecondHandBean;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.model.UserModel;
import io.reactivex.disposables.Disposable;

public class SecondGoodsViewHolder extends BaseViewHolder<SecondHandBean.GoodsBean> {

  @BindView(R.id.iv_second_goods)
  ImageView ivSecondGoods;
  @BindView(R.id.tv_second_title)
  TextView tvSecondTitle;
  @BindView(R.id.tv_second_price)
  TextView tvSecondPrice;
  @BindView(R.id.tv_date)
  TextView tvDate;
  @BindView(R.id.tv_delete)
  TextView tvDelete;

  private Disposable disposable;

  public UserModel userModel = new UserModel();

  public SecondGoodsViewHolder(@NonNull View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  @Override
  public void onViewRecycled() {
    super.onViewRecycled();
    if (disposable != null && !disposable.isDisposed()){
      disposable.dispose();
    }
  }

  @Override
  protected void initItemView(View view) {

  }

  @Override
  public void loadItemData(Context context, SecondHandBean.GoodsBean data, int position) {
    if (disposable != null && !disposable.isDisposed()){
      disposable.dispose();
    }
    disposable = ImageRequestHelper.loadImage(context, data.getImage(), ivSecondGoods);
    tvSecondTitle.setText(data.getTit());
    String price = "¥" + data.getPrize();
    tvSecondPrice.setText(price);
    tvDate.setText(data.getCreated_on());
    if (userModel.getCurUserInfo().getData().getUser_id() == Integer.parseInt(data.getUser_id())){
      tvDelete.setVisibility(View.VISIBLE);
    }else {
      tvDelete.setVisibility(View.GONE);
    }
  }


  @Override
  public void setOnClickListener(View.OnClickListener onClickListener) {
    super.setOnClickListener(onClickListener);
    tvDelete.setOnClickListener(onClickListener);
  }
}
