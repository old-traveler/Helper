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
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.UserInfoBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.model.UserModel;
import org.greenrobot.greendao.annotation.NotNull;

public class UserInfoActivity extends BaseRequestActivity<UserInfoBean> {

  @BindView(R.id.iv_info_bg)
  ImageView ivInfoBg;
  @BindView(R.id.tv_info_name)
  TextView tvInfoName;
  @BindView(R.id.tv_info_desc)
  TextView tvInfoDesc;
  @BindView(R.id.cv_info_portrait)
  ImageView cvInfoPortrait;
  private String headUrl;
  private String userId;
  private UserModel userModel = new UserModel();

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

  public static void goToUserInfoActivity(Context context,String userId){
    Bundle bundle = new Bundle();
    bundle.putString(Constant.USER_ID,userId);
    Intent intent = new Intent(context,UserInfoActivity.class);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  @Override
  public void initViewWithIntentData(@NotNull Bundle bundle) {
    ButterKnife.bind(this);
    setToolBarTitle(R.string.user_info);
    userId = bundle.getString(Constant.USER_ID);
    ImageRequestHelper.loadOtherImage(this,UiHelper.getString(R.string.default_info_bg),ivInfoBg);
    if (TextUtils.isEmpty(bundle.getString(Constant.USER_HEAD_URL))){
      startRequestApi();
    }else {
      tvInfoName.setText(bundle.getString(Constant.USER_NAME));
      ImageRequestHelper.loadHeadImage(this,bundle.getString(Constant.USER_HEAD_URL),cvInfoPortrait);
      headUrl = bundle.getString(Constant.USER_HEAD_URL);
      if (!TextUtils.isEmpty(bundle.getString(Constant.USER_BIO))){
        tvInfoDesc.setText(bundle.getString(Constant.USER_BIO));
      }
    }
  }


  @OnClick({
      R.id.fb_user_lost, R.id.fb_user_statement, R.id.fb_user_market, R.id.cv_info_portrait
  })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.fb_user_lost:
        goToPersonalPublishActivity(Constant.TYPE_LOST);
        break;
      case R.id.fb_user_statement:
        goToPersonalPublishActivity(Constant.TYPE_STATEMENT);
        break;
      case R.id.fb_user_market:
        goToPersonalPublishActivity(Constant.TYPE_SECOND);
        break;
      case R.id.cv_info_portrait:
        PictureBrowsingActivity.goToPictureBrowsingActivity(this,headUrl);
        break;
    }
  }

  public void goToPersonalPublishActivity(String type){
    PersonalPublishActivity.goToPersonalPublishActivity(this,userId,type);
  }

  @Override
  protected void requestDataFromApi() {
    userModel.findUserInfoById(userModel.getCurUserInfo(),userId,this);
  }

  @Override
  protected void onSuccessGetData(UserInfoBean userInfoBean) {
    tvInfoName.setText(userInfoBean.getData().getUsername());
    ImageRequestHelper.loadHeadImage(this,userInfoBean.getData().getHead_pic_thumb(),cvInfoPortrait);
    headUrl = userInfoBean.getData().getHead_pic_thumb();
    if (!TextUtils.isEmpty(userInfoBean.getData().getBio())){
      tvInfoDesc.setText(userInfoBean.getData().getBio());
    }
  }

  @Override
  public boolean isOnCreateRequest() {
    return false;
  }
}
