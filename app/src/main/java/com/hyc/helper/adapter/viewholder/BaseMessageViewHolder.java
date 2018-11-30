package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import cn.bmob.newim.bean.BmobIMMessage;
import com.hyc.helper.R;
import com.hyc.helper.activity.UserInfoActivity;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.helper.SendMessageHelper;
import com.hyc.helper.model.UserModel;

public abstract class BaseMessageViewHolder extends BaseViewHolder<BmobIMMessage> {


  ImageView cvHeadView;

  UserModel userModel = new UserModel();
  private ProgressBar progressBar;
  private View errorTip;

  BaseMessageViewHolder(@NonNull View itemView) {
    super(itemView);
    progressBar = itemView.findViewById(R.id.pb_message);
    errorTip = itemView.findViewById(R.id.iv_send_error);
  }

  @Override
  public void loadItemData(Context context, BmobIMMessage data, int position) {
    String id = data.getFromId();
    cvHeadView.setOnClickListener(view -> UserInfoActivity.goToUserInfoActivity(context,id));
    if (!id.equals(String.valueOf(userModel.getCurUserInfo().getData().getUser_id()))){
      return;
    }
    if (data.getSendStatus()==2){
      progressBar.setVisibility(View.GONE);
      errorTip.setVisibility(View.GONE);
    }else if (data.getSendStatus() == 1){
      progressBar.setVisibility(View.VISIBLE);
      errorTip.setVisibility(View.GONE);
    }else {
      errorTip.setVisibility(View.VISIBLE);
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override
  public void setOnClickListener(View.OnClickListener onClickListener) {
    super.setOnClickListener(onClickListener);
    if (errorTip != null){
      errorTip.setOnClickListener(onClickListener);
    }
  }
}
