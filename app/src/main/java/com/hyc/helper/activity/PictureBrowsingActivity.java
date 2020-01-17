package com.hyc.helper.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.hyc.helper.R;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.BigImageLoadRecordBean;
import com.hyc.helper.bean.ImageSizeBean;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.FileHelper;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.model.ImageModel;
import com.hyc.helper.util.RxBus;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.ZOOM_FOCUS_CENTER_IMMEDIATE;

public class PictureBrowsingActivity extends AppCompatActivity implements View.OnClickListener {

  private ViewPager viewPager;
  private List<String> imagesUrl;
  private ImageModel imageModel = new ImageModel();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_picture_browsing);
    viewPager = findViewById(R.id.vp_picture);
    initViewWithData(Objects.requireNonNull(getIntent().getExtras()));
    viewPager.setOffscreenPageLimit(imagesUrl.size());
    getWindow().setEnterTransition(new Fade().setDuration(500));
  }

  public static void goToPictureBrowsingActivity(Context context, int curImagePosition,
      ArrayList<String> imagesUrl) {
    Intent intent = new Intent(context, PictureBrowsingActivity.class);
    Bundle bundle = new Bundle();
    bundle.putInt("curImagePosition", curImagePosition);
    bundle.putStringArrayList("images", imagesUrl);
    intent.putExtras(bundle);
    context.startActivity(intent,
        ActivityOptions.makeSceneTransitionAnimation((Activity) context).toBundle());
  }

  public static void goToPictureBrowsingActivity(Context context, String imageUrl) {
    ArrayList<String> arrayList = new ArrayList<>(1);
    arrayList.add(imageUrl.replace("_thumb", ""));
    goToPictureBrowsingActivity(context, 0, arrayList);
  }

  private void initViewWithData(Bundle bundle) {
    imagesUrl = bundle.getStringArrayList("images");
    int curImagePosition = bundle.getInt("curImagePosition");
    viewPager.setAdapter(new ViewPagerAdapter());
    viewPager.setCurrentItem(curImagePosition);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.tv_cancel) {
      sheetDialog.dismiss();
    }
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
      TextView textView = itemView.findViewById(R.id.tv_show_big_image);
      imageView.setOnClickListener(view -> onBackPressed());
      imageView.setOnLongClickListener(v -> showMoreSelect(position));
      if (!imagesUrl.get(position).startsWith("http") && !imagesUrl.get(position)
          .startsWith("/uploads")) {
        loadImage(imageView, scaleImageView, new File(imagesUrl.get(position)), position);
        textView.setVisibility(View.GONE);
      } else if (imagesUrl.get(position).startsWith("http")) {
        textView.setVisibility(View.GONE);
        ImageRequestHelper.loadOtherImageAsFile(PictureBrowsingActivity.this,
            imagesUrl.get(position),
            new SimpleTarget<File>() {
              @Override
              public void onResourceReady(@NonNull File resource,
                  @Nullable Transition<? super File> transition) {
                loadImage(imageView, scaleImageView, resource, position);
              }
            });
      } else {
        Disposable disposable = imageModel.getBigImageLoadRecord(imagesUrl.get(position),
            bean -> {
              if (bean != null && FileHelper.fileIsExist(bean.getFilePath())) {
                textView.setVisibility(View.GONE);
                loadImage(imageView, scaleImageView, new File(bean.getFilePath()), position);
              } else {
                showImage(textView, progressBar, imageView, scaleImageView, position);
              }
            }, throwable -> showImage(textView, progressBar, imageView, scaleImageView, position));
        itemView.setTag(disposable);
      }
      container.addView(itemView);
      return itemView;
    }

    private void showImage(View textView, ProgressBar progressBar, ImageView imageView,
        SubsamplingScaleImageView scaleImageView, int position) {
      textView.setVisibility(View.VISIBLE);
      ImageRequestHelper.loadImageByUrl(PictureBrowsingActivity.this, imagesUrl.get(position),
          imageView);
      textView.setOnClickListener(
          view -> showBigImage(view, progressBar, imageView, scaleImageView, position));
    }

    private void showBigImage(View textView, ProgressBar progressBar, ImageView imageView,
        SubsamplingScaleImageView subsamplingScaleImageView, int position) {
      progressBar.setVisibility(View.VISIBLE);
      textView.setVisibility(View.GONE);
      ImageRequestHelper.loadOralImageAsFile(PictureBrowsingActivity.this, imagesUrl.get(position),
          new SimpleTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource,
                @Nullable Transition<? super File> transition) {
              RxBus.getDefault()
                  .post(new MessageEvent<>(Constant.EventType.ORIGINAL_DOWNLOAD, null));
              imageModel.saveBigImageLoadRecord(
                  new BigImageLoadRecordBean(imagesUrl.get(position), resource.getPath()));
              loadImage(imageView, subsamplingScaleImageView, resource, position);
              progressBar.setVisibility(View.GONE);
            }
          });
    }

    private void loadImage(ImageView imageView, SubsamplingScaleImageView scaleImageView,
        File file, int position) {
      if (FileHelper.isGifImage(file.getAbsolutePath())) {
        imageView.setVisibility(View.VISIBLE);
        ImageRequestHelper.loadGifFromFile(PictureBrowsingActivity.this, file, imageView);
        scaleImageView.setVisibility(View.GONE);
        return;
      }
      imageView.setVisibility(View.GONE);
      scaleImageView.setVisibility(View.VISIBLE);
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
      scaleImageView.setOnLongClickListener(v -> showMoreSelect(position));
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      SubsamplingScaleImageView scaleImageView
          = ((View) object).findViewById(R.id.sv_image_browsing);
      if (scaleImageView != null) {
        scaleImageView.recycle();
      }
      Disposable disposable = (Disposable) ((View) object).getTag();
      if (disposable != null && !disposable.isDisposed()) {
        disposable.dispose();
      }
      container.removeView((View) object);
    }
  }

  @Override
  public void onBackPressed() {
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    getWindow().setExitTransition(new Fade().setDuration(500));
    super.onBackPressed();
  }

  private BottomSheetDialog sheetDialog;

  private boolean showMoreSelect(int position) {
    if (sheetDialog == null) {
      sheetDialog = new BottomSheetDialog(this);
      @SuppressLint("InflateParams") View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_image_browse, null);
      contentView.findViewById(R.id.tv_save).setOnClickListener(v -> {
        ImageRequestHelper.loadImageAsFile(PictureBrowsingActivity.this,
            imagesUrl.get(position).replace("_thumb", ""), new SimpleTarget<File>() {
              @Override
              public void onResourceReady(@NonNull File resource,
                  @Nullable Transition<? super File> transition) {
                String name = imagesUrl.get(position).replace("_thumb", "");
                String[] temp = name.split("\\.");
                String format = temp[temp.length - 1];
                FileHelper.copy(PictureBrowsingActivity.this, resource,
                    System.currentTimeMillis() + "." + format);
              }
            });
        sheetDialog.dismiss();
      });
      contentView.findViewById(R.id.tv_copy).setOnClickListener(v -> {
        ClipboardManager copy =
            (ClipboardManager) PictureBrowsingActivity.this.getSystemService(
                Context.CLIPBOARD_SERVICE);
        assert copy != null;
        copy.setText(Constant.BASE_IMAGE_URL + imagesUrl.get(position).replace("_thumb", ""));
        sheetDialog.dismiss();
        ToastHelper.toast("已复制到剪切板");
      });
      contentView.findViewById(R.id.tv_cancel).setOnClickListener(this);
      sheetDialog.setContentView(contentView);
    }
    sheetDialog.show();
    return true;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }
}
