package com.hyc.helper.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.hyc.helper.R;
import com.hyc.helper.activity.fragment.LostFindFragment;
import com.hyc.helper.activity.fragment.SecondHandFragment;
import com.hyc.helper.activity.fragment.StatementFragment;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.fragment.BaseFragment;
import com.hyc.helper.helper.Constant;

public class PersonalPublishActivity extends BaseActivity {

  @Override
  protected int getContentViewId() {
    return R.layout.activity_personal_publish;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null && getIntent().getExtras() != null) {
      BaseFragment baseFragment = null;
      String userId = getIntent().getExtras().getString(Constant.USER_ID);
      switch (getIntent().getExtras().getString(Constant.TYPE,"")){
        case Constant.TYPE_LOST:
          baseFragment = LostFindFragment.newInstance(userId);
          break;
        case Constant.TYPE_SECOND:
          baseFragment = SecondHandFragment.newInstance(userId);
          break;
        case Constant.TYPE_STATEMENT:
          baseFragment = StatementFragment.newInstance(userId);
          break;
      }
      if (baseFragment!=null){
        loadFragment(baseFragment);
      }
    }
  }

  public void loadFragment(Fragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .add(R.id.fl_personal_statement, fragment).commit();
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    setToolBarTitle(R.string.my_publish);
  }
}
