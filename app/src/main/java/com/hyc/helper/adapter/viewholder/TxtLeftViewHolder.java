package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import cn.bmob.newim.bean.BmobIMMessage;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.helper.ImageRequestHelper;

public class TxtLeftViewHolder extends BaseMessageViewHolder {


  private TextView tvContent;

  public TxtLeftViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    cvHeadView = view.findViewById(R.id.cv_text_left_head);
    tvContent = view.findViewById(R.id.tv_text_left);
  }

  @Override
  public void loadItemData(Context context, BmobIMMessage data, int position) {
    super.loadItemData(context,data,position);
    String avatarUrl = data.getBmobIMUserInfo().getAvatar();
    ImageRequestHelper.loadHeadImage(context,avatarUrl,cvHeadView);
    tvContent.setText(data.getContent());
    UiHelper.initLinkTextView(tvContent,context);
  }

}
