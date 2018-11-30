package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.bean.LocalImageBean;
import com.hyc.helper.helper.ImageRequestHelper;

public class LocalImageViewHolder extends BaseViewHolder<LocalImageBean> {

  private ImageView ivLocalImage;

  public LocalImageViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ivLocalImage = itemView.findViewById(R.id.iv_local_image);
  }

  @Override
  public void loadItemData(Context context, LocalImageBean data, int position) {
    ImageRequestHelper.loadOtherImage(context,
        TextUtils.isEmpty(data.getThumbnailPath()) ? data.getImageRealPath()
            : data.getThumbnailPath(), ivLocalImage);
  }
}
