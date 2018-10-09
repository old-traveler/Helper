package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.helper.ImageRequestHelper;

public class PublishImageViewHolder extends BaseViewHolder<String> {

  @BindView(R.id.iv_image)
  ImageView ivImage;
  @BindView(R.id.iv_delete)
  ImageView ivDelete;

  public PublishImageViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this, itemView);
  }

  @Override
  public void loadItemData(Context context, String data, int position) {
    if (data.equals("add")){
      ImageRequestHelper.loadImage(context,R.drawable.ic_add_image,ivImage);
      ivDelete.setVisibility(View.GONE);
    }else {
      ImageRequestHelper.loadImage(context,Uri.parse(data),ivImage);
      ivDelete.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void setOnClickListener(View.OnClickListener onClickListener) {
    super.setOnClickListener(onClickListener);
    ivDelete.setOnClickListener(onClickListener);
    ivImage.setOnClickListener(getData().equals("add")?onClickListener:null);
  }
}
