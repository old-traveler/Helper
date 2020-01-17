package com.hyc.helper.activity;

import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.ImageSizeBean;
import com.hyc.helper.helper.FileHelper;
import com.hyc.helper.helper.ImageRequestHelper;
import java.io.File;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE;

public class SchoolCalendarActivity extends BaseActivity {

  @BindView(R.id.iv_calendar)
  SubsamplingScaleImageView ivCalendar;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_school_calendar;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBarTitle(R.string.school_calendar);
    showLoadingView();
    ImageRequestHelper.loadOtherImageAsFile(SchoolCalendarActivity.this,
        "http://images.huthelper.cn:8888/uploads/huthelper/img/web1.png",
        new SimpleTarget<File>() {
          @Override
          public void onResourceReady(@NonNull File resource,
              @Nullable Transition<? super File> transition) {
            loadImage(resource);
            closeLoadingView();
          }
        });
  }

  public void loadImage(File file) {
    ImageSizeBean sizeBean = FileHelper.getImageSize(file.getAbsolutePath());
    if (UiHelper.isLongImage(sizeBean)) {
      ivCalendar.setMinScale(1.0F);
      ivCalendar.setImage(
          ImageSource.uri(Uri.fromFile(file)), new ImageViewState(1.0f, new PointF(0, 0), 0));
    } else {
      ivCalendar.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
      ivCalendar.setImage(ImageSource.uri(Uri.fromFile(file)));
      ivCalendar.setDoubleTapZoomStyle(ZOOM_FOCUS_CENTER_IMMEDIATE);
    }
  }
}
