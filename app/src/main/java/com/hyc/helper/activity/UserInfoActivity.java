package com.hyc.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.ImageRequestHelper;
import org.greenrobot.greendao.annotation.NotNull;

public class UserInfoActivity extends BaseActivity {

  @BindView(R.id.iv_info_bg)
  ImageView ivInfoBg;
  @BindView(R.id.tv_info_name)
  TextView tvInfoName;
  @BindView(R.id.tv_info_desc)
  TextView tvInfoDesc;
  @BindView(R.id.cv_info_portrait)
  ImageView cvInfoPortrait;
  private String headUrl;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_user_info;
  }

  public static void goToUserInfoActivity(
      Context context,String userId,String username,String userBio,String headUrl){
    Bundle bundle = new Bundle();
    bundle.putString(Constant.USER_BIO,userBio);
    bundle.putString(Constant.USER_HEAD_URL,headUrl);
    bundle.putString(Constant.USER_NAME,username);
    bundle.putString(Constant.USER_ID,userId);
    Intent intent = new Intent(context,UserInfoActivity.class);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  @Override
  public void initViewWithIntentData(@NotNull Bundle bundle) {
    ButterKnife.bind(this);
    setToolBarTitle(R.string.user_info);
    ImageRequestHelper.loadOtherImage(this,UiHelper.getString(R.string.default_info_bg),ivInfoBg);
    tvInfoName.setText(bundle.getString(Constant.USER_NAME));
    ImageRequestHelper.loadHeadImage(this,bundle.getString(Constant.USER_HEAD_URL),cvInfoPortrait);
    headUrl = bundle.getString(Constant.USER_HEAD_URL);
    if (!TextUtils.isEmpty(bundle.getString(Constant.USER_BIO))){
      tvInfoDesc.setText(bundle.getString(Constant.USER_BIO));
    }
  }


  @OnClick({
      R.id.fb_user_lost, R.id.fb_user_statement, R.id.fb_user_market, R.id.cv_info_portrait
  })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.fb_user_lost:
        break;
      case R.id.fb_user_statement:
        break;
      case R.id.fb_user_market:
        break;
      case R.id.cv_info_portrait:
        PictureBrowsingActivity.goToPictureBrowsingActivity(this,headUrl);
        break;
    }
  }
}
