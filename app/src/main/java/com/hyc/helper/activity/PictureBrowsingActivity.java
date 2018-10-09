package com.hyc.helper.activity;

import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.ImageSizeBean;
import com.hyc.helper.helper.FileHelper;
import java.io.File;
import java.util.List;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE;

public class PictureBrowsingActivity extends AppCompatActivity {

  private ViewPager viewPager;
  private List<String> imagesUrl;
  private int curImagePosition;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
    setContentView(R.layout.activity_picture_browsing);
    viewPager = findViewById(R.id.vp_picture);
    initViewWithData(getIntent().getExtras());
    viewPager.setOffscreenPageLimit(3);
    getWindow().setEnterTransition(new Fade().setDuration(500));
  }

  private void initViewWithData(Bundle bundle) {
    imagesUrl = bundle.getStringArrayList("images");
    curImagePosition = bundle.getInt("curImagePosition");
    viewPager.setAdapter(new ViewPagerAdapter());
    viewPager.setCurrentItem(curImagePosition);
  }

  public class ViewPagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
      return imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
      return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      View itemView = LayoutInflater.from(container.getContext())
          .inflate(R.layout.item_pager_image, container, false);
      final SubsamplingScaleImageView scaleImageView
          = itemView.findViewById(R.id.sv_image_browsing);
      final ImageView imageView = itemView.findViewById(R.id.iv_browsing);
      final ProgressBar progressBar = itemView.findViewById(R.id.pb_image_browsing);

      if (!imagesUrl.get(position).startsWith("http")) {
        loadImage(scaleImageView, new File(imagesUrl.get(position)));
      } else {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(PictureBrowsingActivity.this)
            .asFile()
            .load(imagesUrl.get(position))
            .into(new SimpleTarget<File>() {
              @Override
              public void onResourceReady(@NonNull File resource,
                  @Nullable Transition<? super File> transition) {
                loadImage(scaleImageView, resource);
                progressBar.setVisibility(View.GONE);
              }
            });
      }

      container.addView(itemView);
      return itemView;
    }

    private void loadImage(SubsamplingScaleImageView scaleImageView, File file) {
      ImageSizeBean sizeBean = FileHelper.getImageSize(file.getAbsolutePath());
      if (UiHelper.isLongImage(sizeBean)) {
        scaleImageView.setMinScale(1.0F);
        scaleImageView.setImage(
            ImageSource.uri(Uri.fromFile(file)), new ImageViewState(1.0f, new PointF(0, 0), 0));
      } else {
        scaleImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
        scaleImageView.setImage(ImageSource.uri(Uri.fromFile(file)));
        scaleImageView.setDoubleTapZoomStyle(ZOOM_FOCUS_CENTER_IMMEDIATE);
      }
      scaleImageView.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }
  }

  @Override
  public void onBackPressed() {
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    getWindow().setExitTransition(new Fade().setDuration(500));
    super.onBackPressed();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
