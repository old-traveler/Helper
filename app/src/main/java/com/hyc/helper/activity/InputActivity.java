package com.hyc.helper.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.util.ToastHelper;

public class InputActivity extends BaseActivity {

  @BindView(R.id.et_input)
  EditText etInput;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_input;
  }

  @Override
  public int getMenuId() {
    return R.menu.menu_input;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.ok) {
      if (!TextUtils.isEmpty(etInput.getText().toString())) {
        Bundle bundle = new Bundle();
        bundle.putString("input", etInput.getText().toString());
        backForResult(PersonalActivity.class, bundle, RESULT_OK);
      } else {
        ToastHelper.toast(R.string.input_tip);
      }
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBar();
  }
}
