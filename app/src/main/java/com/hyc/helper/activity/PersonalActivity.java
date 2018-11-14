package com.hyc.helper.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.helper.UploadImageObserver;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.util.Glide4Engine;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonalActivity extends BaseActivity {

  @BindView(R.id.iv_head)
  ImageView ivHead;
  @BindView(R.id.tv_username)
  TextView tvUsername;
  @BindView(R.id.tv_bio)
  TextView tvBio;
  @BindView(R.id.tv_name)
  TextView tvName;
  @BindView(R.id.tv_number)
  TextView tvNumber;
  @BindView(R.id.tv_dep_name)
  TextView tvDepName;
  @BindView(R.id.tv_class_name)
  TextView tvClassName;
  private ListPopupWindow publishPopupWindow;
  private UserModel userModel = new UserModel();
  private Disposable disposable;

  private int REQUEST_CODE_CHOOSE = 2009;
  private int REQUEST_CODE_USERNAME = 2010;
  private int REQUEST_CODE_BIO = 2011;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_personal;
  }

  @Override
  public int getMenuId() {
    return R.menu.menu_personal;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.my_publish) {
      publishPopupWindow.setAnchorView(findViewById(R.id.my_publish));
      publishPopupWindow.show();
      Objects.requireNonNull(
          publishPopupWindow.getListView()).setBackgroundColor(UiHelper.getColor(R.color.white));
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBarTitle(R.string.personal);
    initView(userModel.getCurUserInfo().getData());
    initMenuPopupWindow();
  }

  private void initMenuPopupWindow() {
    List<String> list = new ArrayList<>();
    list.add(UiHelper.getString(R.string.school_statement));
    list.add(UiHelper.getString(R.string.second_market));
    list.add(UiHelper.getString(R.string.lost_find));
    publishPopupWindow = new ListPopupWindow(this);
    publishPopupWindow.setAdapter(new ArrayAdapter<>(this, R.layout.item_select_week, list));
    publishPopupWindow.setWidth(DensityUtil.dip2px(90f));
    publishPopupWindow.setHeight(DensityUtil.dip2px(100f));
    publishPopupWindow.setModal(true);
    publishPopupWindow.setOnItemClickListener((adapterView, view, i, l) -> {
      publishPopupWindow.dismiss();
      String type;
      if (i == 0) {
        type = Constant.TYPE_STATEMENT;
      } else if (i == 1) {
        type = Constant.TYPE_SECOND;
      } else {
        type = Constant.TYPE_LOST;
      }
      PersonalPublishActivity.goToPersonalPublishActivity(this,
          String.valueOf(userModel.getCurUserInfo().getData().getUser_id()), type);
    });
  }

  private void initView(UserBean.DataBean data) {
    ImageRequestHelper.loadBigHeadImage(this, data.getHead_pic_thumb(), ivHead);
    tvUsername.setText(
        TextUtils.isEmpty(data.getUsername()) ? data.getTrueName() : data.getUsername());
    tvBio.setText(TextUtils.isEmpty(data.getBio()) ? UiHelper.getString(R.string.default_bio)
        : data.getBio());
    tvName.setText(String.format("%s%s", UiHelper.getString(R.string.name), data.getTrueName()));
    tvNumber.setText(
        String.format("%s%s", UiHelper.getString(R.string.number), data.getStudentKH()));
    tvDepName.setText(String.format("%s%s", UiHelper.getString(R.string.dep), data.getDep_name()));
    tvClassName.setText(
        String.format("%s%s", UiHelper.getString(R.string.str_class), data.getClass_name()));
  }

  @OnClick({ R.id.iv_head, R.id.tv_username, R.id.tv_bio })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_head:
        replaceHeadImage();
        break;
      case R.id.tv_username:
        goToOtherActivityForResult(InputActivity.class, REQUEST_CODE_USERNAME);
        break;
      case R.id.tv_bio:
        goToOtherActivityForResult(InputActivity.class, REQUEST_CODE_BIO);
        break;
    }
  }

  private void replaceHeadImage() {
    addDisposable(new RxPermissions(this)
        .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (granted) {
            startSelectImage();
          } else {
            ToastHelper.toast(R.string.camera_premission_tip);
          }
        }));
  }

  private void startSelectImage() {
    Matisse.from(this)
        .choose(MimeType.ofAll(), false)
        .countable(true)
        .maxSelectable(1)
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .imageEngine(new Glide4Engine())
        .forResult(REQUEST_CODE_CHOOSE);
  }

  public void startCropImage(Uri sourceUri) {
    String headImageFilePath = "head_image.jpg";
    Uri destinationUri = Uri.fromFile(new File(getCacheDir(), headImageFilePath));
    UCrop.Options options = new UCrop.Options();
    options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
    options.setToolbarColor(UiHelper.getColor(R.color.colorPrimary));
    options.setStatusBarColor(UiHelper.getColor(R.color.colorPrimary));
    options.withAspectRatio(9, 9);
    options.withMaxResultSize(1080, 1080);
    UCrop.of(sourceUri, destinationUri)
        .withOptions(options)
        .start(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && data != null) {
      List<Uri> result = Matisse.obtainResult(data);
      if (result != null && result.size() > 0) {
        startCropImage(result.get(0));
      }
    } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
      uploadImage(UCrop.getOutput(data));
    } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
      ToastHelper.toast(Objects.requireNonNull(UCrop.getError(data)).getMessage());
    } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_USERNAME && data != null) {
      updateUsername(Objects.requireNonNull(data.getExtras()).getString("input"));
    } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_BIO && data != null) {
      updateBio(Objects.requireNonNull(data.getExtras()).getString("input"));
    }
  }

  private void updateUsername(final String input) {
    showLoadingView();
    disposable = userModel.updateUsername(userModel.getCurUserInfo(), input)
        .subscribe(baseRequestBean -> {
          closeLoadingView();
          if (baseRequestBean.getCode() == 200) {
            tvUsername.setText(input);
            userModel.updateLocalUsername(input);
          } else if (TextUtils.isEmpty(baseRequestBean.getMsg())) {
            ToastHelper.toast(String.valueOf(baseRequestBean.getCode()));
          } else {
            ToastHelper.toast(baseRequestBean.getMsg());
          }
        }, throwable -> {
          closeLoadingView();
          ToastHelper.toast(throwable.getMessage());
        });
  }

  private void updateBio(final String input) {
    showLoadingView();
    disposable = userModel.updateBio(userModel.getCurUserInfo(), input)
        .subscribe(baseRequestBean -> {
          closeLoadingView();
          if (baseRequestBean.getCode() == 200) {
            tvBio.setText(input);
            userModel.updateLocalUserBio(input);
          } else if (TextUtils.isEmpty(baseRequestBean.getMsg())) {
            ToastHelper.toast(String.valueOf(baseRequestBean.getCode()));
          } else {
            ToastHelper.toast(baseRequestBean.getMsg());
          }
        }, throwable -> {
          closeLoadingView();
          ToastHelper.toast(throwable.getMessage());
        });
  }

  public void uploadImage(Uri uri) {
    showLoadingView();
    userModel.updateUserHeadImage(this, userModel.getCurUserInfo(), uri, new UploadImageObserver(1,
        new UploadImageObserver.OnUploadImageListener() {
          @Override
          public void onSuccess(String image) {
            closeLoadingView();
            userModel.updateLocalUserHeadImage(image.substring(2));
            initView(userModel.getCurUserInfo().getData());
          }

          @Override
          public void onFailure(Throwable e) {
            closeLoadingView();
            ToastHelper.toast(e.getMessage());
          }
        }));
  }
}
