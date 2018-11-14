package com.hyc.helper.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.PowerBean;
import com.hyc.helper.model.PowerModel;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.Sha1Utils;

public class QueryPowerActivity extends BaseRequestActivity<PowerBean> {

  @BindView(R.id.et_locate)
  EditText etLocate;
  @BindView(R.id.et_room)
  EditText etRoom;
  @BindView(R.id.rb_west)
  RadioButton rbWest;

  private PowerModel powerModel = new PowerModel();
  private UserModel userModel = new UserModel();

  @Override
  protected boolean validationInput() {
    if (!TextUtils.isEmpty(etLocate.getText().toString()) && !TextUtils.isEmpty(
        etRoom.getText().toString())) {
      return true;
    }
    ToastHelper.toast(R.string.query_tip);
    return false;
  }

  @Override
  protected void requestDataFromApi() {
    String part = rbWest.isChecked() ? "1" : "2";
    String locate = etLocate.getText().toString();
    String room = etRoom.getText().toString();
    String enc = Sha1Utils.getEnc(locate, room, userModel.getCurUserInfo(), part);
    powerModel.queryPowerInfo(
        part, locate, room, userModel.getCurUserInfo().getData(), enc, this);
  }

  @Override
  protected void onSuccessGetData(PowerBean powerBean) {
    showConfirmDialog(
        String.format("余电：%s\n余额：%s", powerBean.getElectricity(), powerBean.getBalance()));
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_query_power;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    setToolBarTitle(R.string.query_power_title);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.btn_query_power)
  public void onViewClicked() {
    startRequest();
  }

  @Override
  public boolean isOnCreateRequest() {
    return false;
  }
}
