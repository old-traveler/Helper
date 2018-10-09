package com.hyc.helper.activity;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.text.TextUtils;
import android.transition.Fade;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.model.UserModel;

public class LoginActivity extends BaseRequestActivity<UserBean> {

  @BindView(R.id.et_username)
  EditText etUsername;
  @BindView(R.id.et_password)
  EditText etPassword;
  @BindView(R.id.btn_login)
  MaterialButton btnLogin;
  private Unbinder unbinder;
  private UserModel userModel = new UserModel();

  @Override
  protected int getContentViewId() {
    return R.layout.activity_login;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    unbinder = ButterKnife.bind(this);
    setToolBarTitle(R.string.login);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  @Override
  public void onClick(View view) {
    super.onClick(view);
    if (view.getId() == R.id.btn_login) {
      startRequest();
    }
  }

  @Override
  protected boolean validationInput() {
    if (TextUtils.isEmpty(etUsername.getText().toString())) {
      ToastHelper.toast(R.string.number_not_empty);
      return false;
    } else if (TextUtils.isEmpty(etPassword.getText().toString())) {
      ToastHelper.toast(R.string.password_not_empty);
      return false;
    }
    return true;
  }

  @Override
  protected void requestDataFromApi() {
    userModel.login(etUsername.getText().toString()
        , etPassword.getText().toString(), this);
  }

  @Override
  protected void onSuccessGetData(UserBean userBean) {
    if (userBean.getCode() == 200 && userBean.getData()!=null) {
      userModel.cacheUserInfo(userBean);
      goToOtherActivity(MainActivity.class, true);
    } else if (userBean.getMsg()!=null){
      ToastHelper.toast(userBean.getMsg());
    }
  }

  @Override
  public boolean isOnCreateRequest() {
    return false;
  }


}
