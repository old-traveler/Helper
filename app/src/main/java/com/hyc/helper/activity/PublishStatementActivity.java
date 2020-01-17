package com.hyc.helper.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hyc.cuckoo_lib.CuckooNeed;
import com.hyc.helper.R;
import com.hyc.helper.activity.fragment.StatementFragment;
import com.hyc.helper.adapter.viewholder.PublishImageViewHolder;
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.model.StatementModel;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.Glide4Engine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.util.ArrayList;
import java.util.List;

public class PublishStatementActivity extends BaseRequestActivity<BaseRequestBean> {

  @BindView(R.id.et_publish_statement)
  EditText etPublishStatement;
  @BindView(R.id.rv_publish_statement)
  RecyclerView rvPublishStatement;
  private Unbinder unbinder;
  private BaseRecycleAdapter<String, PublishImageViewHolder> baseRecycleAdapter;
  private int REQUEST_CODE_CHOOSE = 2009;
  private UserModel userModel = new UserModel();
  private StatementModel statementModel = new StatementModel();

  @Override
  protected boolean validationInput() {
    String content = etPublishStatement.getText().toString();
    if (TextUtils.isEmpty(content) && baseRecycleAdapter.getItemCount() == 1) {
      ToastHelper.toast("先写点东西吧");
      return false;
    }
    return true;
  }

  @Override
  protected void requestDataFromApi() {
    String content = etPublishStatement.getText().toString();
    UserBean userBean = userModel.getCurUserInfo();
    if (baseRecycleAdapter.getItemCount() > 1) {
      List<String> list = new ArrayList<>();
      for (int i = 1; i < baseRecycleAdapter.getItemCount(); i++) {
        list.add(baseRecycleAdapter.getItemData(i));
      }
      statementModel.publishStatement(userBean, content, list, this);
    } else {
      statementModel.publishStatement(userBean, content, this);
    }
  }

  @Override
  protected void onSuccessGetData(BaseRequestBean baseRequestBean) {
    ToastHelper.toast("发布成功");
    backForResult(StatementFragment.class, RESULT_OK);
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_publish_statement;
  }

  @Override
  public int getMenuId() {
    return R.menu.menu_pushlish_statement;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    unbinder = ButterKnife.bind(this);
    setToolBarTitle(R.string.publish_statement);
    rvPublishStatement.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    rvPublishStatement.setItemAnimator(new DefaultItemAnimator());
    List<String> imageUrl = new ArrayList<>();
    imageUrl.add("add");
    baseRecycleAdapter = new BaseRecycleAdapter<>(imageUrl, R.layout.item_publish_image,
        PublishImageViewHolder.class);
    rvPublishStatement.setAdapter(baseRecycleAdapter);
    baseRecycleAdapter.setOnItemClickListener((itemData, view, position) -> {
      if (view.getId() == R.id.iv_delete) {
        baseRecycleAdapter.removeItemFormList(position);
      } else {
        startSelectImage();
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_publish) {
      startRequest();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @CuckooNeed({ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE })
  private void startSelectImage() {
    int size = 5 - baseRecycleAdapter.getItemCount();
    if (size <= 0) {
      ToastHelper.toast(R.string.max_count_image_tip);
      return;
    }
    Matisse.from(PublishStatementActivity.this)
        .choose(MimeType.ofAll(), false)
        .countable(true)
        .maxSelectable(size)
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .imageEngine(new Glide4Engine())
        .forResult(REQUEST_CODE_CHOOSE);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }

  @Override
  public boolean isOnCreateRequest() {
    return false;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
      List<Uri> result = Matisse.obtainResult(data);
      List<String> imageResult = new ArrayList<>();
      for (Uri uri : result) {
        imageResult.add(uri.toString());
      }
      baseRecycleAdapter.appendDataToList(imageResult);
    }
  }
}
