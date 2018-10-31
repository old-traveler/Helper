package com.hyc.helper.activity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import butterknife.Unbinder;
import com.hyc.helper.R;
import com.hyc.helper.activity.fragment.SecondHandFragment;
import com.hyc.helper.adapter.viewholder.PublishImageViewHolder;
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.helper.ValidationHelper;
import com.hyc.helper.model.SecondGoodsModel;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.Glide4Engine;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondMarketPublishActivity extends BaseRequestActivity<BaseRequestBean> {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.et_title)
  EditText etTitle;
  @BindView(R.id.et_price)
  EditText etPrice;
  @BindView(R.id.et_area)
  EditText etArea;
  @BindView(R.id.et_phone)
  EditText etPhone;
  @BindView(R.id.et_attr)
  EditText etAttr;
  @BindView(R.id.et_content)
  EditText etContent;
  @BindView(R.id.rv_publish_goods)
  RecyclerView rvPublishGoods;

  private boolean isSell = true;
  private Unbinder unbinder;
  private int REQUEST_CODE_CHOOSE = 2009;
  private SecondGoodsModel secondGoodsModel = new SecondGoodsModel();
  private UserModel userModel = new UserModel();
  private BaseRecycleAdapter<String, PublishImageViewHolder> baseRecycleAdapter;

  @Override
  protected boolean validationInput() {
    return ValidationHelper.isNotInputEmpty(etTitle, "标题不能为空")
        && ValidationHelper.isNotInputEmpty(etPrice, "价格不能为空")
        && ValidationHelper.isNotInputEmpty(etArea, "发布区域不能为空")
        && ValidationHelper.isNotInputEmpty(etPhone, "联系方式不能为空")
        && ValidationHelper.isNotInputEmpty(etAttr, "成色不能为空")
        && ValidationHelper.isNotInputEmpty(etContent, "详情不能为空")
        && isPublishImageNotEmpty();
  }

  public boolean isPublishImageNotEmpty() {
    if (baseRecycleAdapter.getItemCount() > 1) {
      return true;
    }
    ToastHelper.toast("必须含一张图片");
    return false;
  }

  @Override
  protected void requestDataFromApi() {
    List<String> list = new ArrayList<>();
    for (int i = 1; i < baseRecycleAdapter.getItemCount(); i++) {
      list.add(baseRecycleAdapter.getItemData(i));
    }
    secondGoodsModel.publishMarketGoods(userModel.getCurUserInfo(), getRequestParams(), list, this);
  }

  @Override
  protected void onSuccessGetData(BaseRequestBean baseRequestBean) {
    ToastHelper.toast("发布成功");
    backForResult(SecondHandFragment.class,RESULT_OK);
  }

  private Map<String, String> getRequestParams() {
    Map<String, String> map = new HashMap<>();
    map.put("tit", etTitle.getText().toString());
    map.put("address", etArea.getText().toString());
    map.put("prize", etPrice.getText().toString());
    map.put("content", etContent.getText().toString());
    map.put("phone", etPhone.getText().toString());
    map.put("attr", etAttr.getText().toString());
    map.put("type", isSell ? "1" : "2");
    return map;
  }

  @Override
  public int getMenuId() {
    return R.menu.menu_publish_goods;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.item_sell) {
      isSell = true;
      startRequestApi();
      return true;
    } else if (item.getItemId() == R.id.item_buy) {
      isSell = false;
      startRequestApi();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_second_market_publish;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    unbinder = ButterKnife.bind(this);
    setToolBarTitle(R.string.publish_goods);
    rvPublishGoods.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    rvPublishGoods.setItemAnimator(new DefaultItemAnimator());
    List<String> imageUrl = new ArrayList<>();
    imageUrl.add("add");
    baseRecycleAdapter = new BaseRecycleAdapter<>(imageUrl, R.layout.item_publish_image,
        PublishImageViewHolder.class);
    rvPublishGoods.setAdapter(baseRecycleAdapter);
    baseRecycleAdapter.setOnItemClickListener((itemData, view, position) -> {
      if (view.getId() == R.id.iv_delete) {
        baseRecycleAdapter.removeItemFormList(position);
      } else {
        goToSelectImage();
      }
    });
  }

  @SuppressLint("CheckResult")
  private void goToSelectImage() {
    new RxPermissions(this)
        .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (granted) {
            startSelectImage();
          } else {
            ToastHelper.toast(R.string.camera_premission_tip);
          }
        });
  }

  private void startSelectImage() {
    int size = 5 - baseRecycleAdapter.getItemCount();
    if (size <= 0) {
      ToastHelper.toast(R.string.max_count_image_tip);
      return;
    }
    Matisse.from(SecondMarketPublishActivity.this)
        .choose(MimeType.ofAll(), false)
        .countable(true)
        .maxSelectable(size)
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .imageEngine(new Glide4Engine())
        .forResult(REQUEST_CODE_CHOOSE);
  }

  @Override
  public boolean isOnCreateRequest() {
    return false;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
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
