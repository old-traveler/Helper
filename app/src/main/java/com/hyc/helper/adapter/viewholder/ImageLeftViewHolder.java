package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import cn.bmob.newim.bean.BmobIMMessage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.ImageSizeBean;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.util.DensityUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageLeftViewHolder extends BaseMessageViewHolder {


  private ImageView bvContent;

  public ImageLeftViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    cvHeadView = view.findViewById(R.id.cv_image_left);
    bvContent = view.findViewById(R.id.iv_image_left);
  }

  @Override
  public void loadItemData(Context context, BmobIMMessage data, int position) {
    super.loadItemData(context, data, position);
    String avatarUrl = data.getBmobIMUserInfo().getAvatar();
    ImageRequestHelper.loadHeadImage(context, avatarUrl, cvHeadView);
    ImageSizeBean sizeBean = getImageSize(data.getExtra());
    RequestOptions options = RequestOptions
        .bitmapTransform(new RoundedCorners(10))
        .override(sizeBean.getWidth(), sizeBean.getHeight())
        .placeholder(R.drawable.bg_chat_placeholder);
    Glide.with(context)
        .load(data.getContent())
        .apply(options)
        .into(bvContent);
  }


  private ImageSizeBean getImageSize(String josn) {
    ImageSizeBean imageSizeBean = null;
    try {
      JSONObject object = new JSONObject(josn);
      int width = object.getInt("width");
      int height = object.getInt("height");
      if (UiHelper.isLongImage(new ImageSizeBean(width, height))) {
        height = width * DensityUtil.getScreenHeight() / DensityUtil.getScreenWidth();
      }
      int maxWidth = DensityUtil.dip2px(100f);
      int minWidth = DensityUtil.dip2px(50f);
      int maxHeight = DensityUtil.dip2px(150f);
      int minHeight = DensityUtil.dip2px(50f);
      if (width > maxWidth) {
        height = height * maxWidth / width;
        width = maxWidth;
      } else if (width < minWidth) {
        height = height * minWidth / width;
        width = minWidth;
      }
      if (height > maxHeight) {
        width = width * maxHeight / height;
        height = maxHeight;
      } else if (height < minHeight) {
        width = width * minHeight / height;
        height = minHeight;
      }
      imageSizeBean = new ImageSizeBean(
          width, height);
    } catch (JSONException e) {
      e.printStackTrace();
      return new ImageSizeBean(150, 300);
    }
    return imageSizeBean;
  }

  @Override
  public void setOnClickListener(View.OnClickListener onClickListener) {
    super.setOnClickListener(onClickListener);
    bvContent.setOnClickListener(onClickListener);
  }
}
