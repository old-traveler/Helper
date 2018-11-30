package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.newim.bean.BmobIMMessage;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.helper.ImageRequestHelper;

public class TxtRightViewHolder extends BaseMessageViewHolder {


  private TextView tvContent;

  public TxtRightViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    cvHeadView = view.findViewById(R.id.cv_text_right_head);
    tvContent = view.findViewById(R.id.tv_text_right);
  }

  @Override
  public void loadItemData(Context context, BmobIMMessage data, int position) {
    super.loadItemData(context,data,position);
    ImageRequestHelper.loadHeadImage(context,userModel.getCurUserInfo().getData().getHead_pic_thumb(),cvHeadView);
    tvContent.setText(data.getContent());
    UiHelper.initLinkTextView(tvContent,context);
  }



}