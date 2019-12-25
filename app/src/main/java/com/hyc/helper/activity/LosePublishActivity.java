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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.cuckoo_lib.CuckooNeed;
import com.hyc.helper.R;
import com.hyc.helper.activity.fragment.LostFindFragment;
import com.hyc.helper.adapter.viewholder.PublishImageViewHolder;
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.helper.DateHelper;
import com.hyc.helper.helper.ValidationHelper;
import com.hyc.helper.model.LostGoodsModel;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.Glide4Engine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LosePublishActivity extends BaseRequestActivity<BaseRequestBean> {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.et_title)
  EditText etTitle;
  @BindView(R.id.et_area)
  EditText etArea;
  @BindView(R.id.et_month)
  EditText etMonth;
  @BindView(R.id.et_day)
  EditText etDay;
  @BindView(R.id.et_hour)
  EditText etHour;
  @BindView(R.id.et_phone)
  EditText etPhone;
  @BindView(R.id.et_content)
  EditText etContent;
  @BindView(R.id.rv_publish_lost)
  RecyclerView rvPublishLost;
  private boolean isFind = true;

  private int REQUEST_CODE_CHOOSE = 2009;
  private BaseRecycleAdapter<String, PublishImageViewHolder> baseRecycleAdapter;
  private LostGoodsModel lostGoodsModel = new LostGoodsModel();
  private UserModel userModel = new UserModel();

  @Override
  public int getMenuId() {
    return R.menu.menu_lose;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.item_find) {
      isFind = true;
      startRequestApi();
      return true;
    } else if (item.getItemId() == R.id.item_lose) {
      isFind = false;
      startRequestApi();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected boolean validationInput() {
    return ValidationHelper.isNotInputEmpty(etTitle, "物品名不能为空")
        && ValidationHelper.isNotInputEmpty(etArea, "地点不能为空")
        && ValidationHelper.isNotInputEmpty(etPhone, "联系方式不能为空")
        && ValidationHelper.isNotInputEmpty(etContent, "详情不能为空")
        && ValidationHelper.isNotInputEmpty(etMonth, "月不能为空")
        && ValidationHelper.isNotInputEmpty(etDay, "日不能为空")
        && ValidationHelper.isNotInputEmpty(etHour, "时不能为空");
  }

  @Override
  public boolean isOnCreateRequest() {
    return false;
  }

  @Override
  protected void requestDataFromApi() {
    List<String> list = new ArrayList<>();
    for (int i = 1; i < baseRecycleAdapter.getItemCount(); i++) {
      list.add(baseRecycleAdapter.getItemData(i));
    }
    lostGoodsModel.publishLost(userModel.getCurUserInfo(), getRequestParams(), list, this);
  }

  private Map<String, String> getRequestParams() {
    Map<String, String> map = new HashMap<>();
    map.put("tit", etTitle.getText().toString());
    map.put("locate", etArea.getText().toString());
    map.put("content", etContent.getText().toString());
    map.put("phone", etPhone.getText().toString());
    map.put("time", getTime());
    map.put("type", isFind ? "1" : "2");
    return map;
  }

  private String getTime() {
    return String.format(UiHelper.getString(R.string.lost_time), DateHelper.getCurYear(),
        etMonth.getText().toString(),
        etDay.getText().toString(), etHour.getText().toString());
  }

  @Override
  protected void onSuccessGetData(BaseRequestBean baseRequestBean) {
    ToastHelper.toast("发布成功");
    backForResult(LostFindFragment.class, RESULT_OK);
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_lose_publish;
  }



  @CuckooNeed({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
  private void startSelectImage() {
    int size = 5 - baseRecycleAdapter.getItemCount();
    if (size <= 0) {
      ToastHelper.toast(R.string.max_count_image_tip);
      return;
    }
    Matisse.from(LosePublishActivity.this)
        .choose(MimeType.ofAll(), false)
        .countable(true)
        .maxSelectable(size)
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .imageEngine(new Glide4Engine())
        .forResult(REQUEST_CODE_CHOOSE);
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBarTitle("Lost Publish");
    rvPublishLost.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    rvPublishLost.setItemAnimator(new DefaultItemAnimator());
    List<String> imageUrl = new ArrayList<>();
    imageUrl.add("add");
    baseRecycleAdapter = new BaseRecycleAdapter<>(imageUrl, R.layout.item_publish_image,
        PublishImageViewHolder.class);
    rvPublishLost.setAdapter(baseRecycleAdapter);
    baseRecycleAdapter.setOnItemClickListener((itemData, view, position) -> {
      if (view.getId() == R.id.iv_delete) {
        baseRecycleAdapter.removeItemFormList(position);
      } else {
        startSelectImage();
      }
    });
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
