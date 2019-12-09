package com.hyc.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.hyc.helper.R;
import com.hyc.helper.activity.fragment.LostFindFragment;
import com.hyc.helper.activity.fragment.SecondHandFragment;
import com.hyc.helper.activity.fragment.StatementFragment;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.fragment.BaseFragment;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.util.parrot.InitialParam;

public class PersonalPublishActivity extends BaseActivity {
  @InitialParam(key = Constant.USER_ID)
  private String userId;
  @InitialParam
  private String type;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_personal_publish;
  }

  public static void goToPersonalPublishActivity(Context context,String userId,String type){
    Bundle bundle = new Bundle();
    bundle.putString(Constant.TYPE,type);
    bundle.putString(Constant.USER_ID,userId);
    Intent intent = new Intent(context,PersonalPublishActivity.class);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null && getIntent().getExtras() != null) {
      BaseFragment baseFragment = null;
      switch (type){
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
