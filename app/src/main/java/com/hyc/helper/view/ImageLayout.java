package com.hyc.helper.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.hyc.helper.R;
import com.hyc.helper.helper.DisposableManager;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.helper.ImageRequestHelper;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public class ImageLayout extends ViewGroup implements View.OnClickListener {

  public static final int DEFAULT_MAX_COUNT = 4;

  public static final int DEFAULT_LINE = 5;

  private List<String> imageUrlList;

  private OnItemClickListener onItemClickListener;

  private DisposableManager disposableManager;

  public interface OnItemClickListener {
    void onItemImageClick(int position);
  }

  private int maxWidth;
  private int maxHeight;
  private int maxCount;

  public ImageLayout(Context context) {
    super(context);
  }

  @SuppressLint("CustomViewStyleable")
  public ImageLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.imageLayout);
    try {
      maxWidth = typedArray.getDimensionPixelSize(
          R.styleable.imageLayout_maxWidth, DensityUtil.dip2px(300f));
      maxHeight = typedArray.getDimensionPixelSize(
          R.styleable.imageLayout_maxHeight, DensityUtil.dip2px(300f));
      maxCount = typedArray.getDimensionPixelSize(
          R.styleable.imageLayout_maxCount, DEFAULT_MAX_COUNT);
    } finally {
      typedArray.recycle();
    }
  }

  @Override
  protected void onLayout(boolean i, int l, int t, int r, int b) {
    int length = maxWidth / 2;
    if (imageUrlList.size() == 1) {
      getChildAt(0).layout(l, 0, r, b - t);
    } else {
      int count = getChildCount();
      for (int j = 0; j < count; j++) {
        int top = j / 2 * (length + DEFAULT_LINE);
        if (j % 2 == 0) {
          getChildAt(j).layout(l, top, l + length, top + length);
        } else {
          getChildAt(j).layout(l + length + DEFAULT_LINE, top, r, top + length);
        }
      }
    }
  }

  public void setImageListUrl(List<String> imageUrlList) {
    if (getChildCount() > 0) {
      removeAllViews();
    }
    if (disposableManager == null) {
      disposableManager = new DisposableManager();
    } else {
      disposableManager.cancelAllDisposable();
      disposableManager = new DisposableManager(imageUrlList.size());
    }
    this.imageUrlList = imageUrlList;
    if (imageUrlList == null || imageUrlList.size() == 0) {
      setVisibility(GONE);
    } else {
      setVisibility(VISIBLE);
      if (imageUrlList.size() == 1) {
        addItemImageView(imageUrlList.get(0), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
      } else {
        addAllItemImageView(imageUrlList, maxWidth / 2);
      }
    }
  }

  public void clear(){
    if (disposableManager != null){
      disposableManager.cancelAllDisposable();
    }
    removeAllViews();
  }

  private void addItemImageView(String url, int width, int height) {
    ImageView imageView = new ImageView(getContext());
    imageView.setScaleType(ImageView.ScaleType.CENTER);
    LayoutParams params = new LayoutParams(width, height);
    imageView.setLayoutParams(params);
    disposableManager.addDisposable(ImageRequestHelper.loadImage(getContext(), url, imageView));
    imageView.setOnClickListener(this);
    addView(imageView);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    measureChildren(widthMeasureSpec, heightMeasureSpec);
    maxWidth = Math.min(MeasureSpec.getSize(widthMeasureSpec), maxWidth);
    int measureWidth = measureWidth();
    int measureHeight = measureHeight();
    setMeasuredDimension(measureWidth, measureHeight);
  }

  private int measureHeight() {
    if (getChildCount() == 0) {
      return 0;
    } else if (getChildCount() == 1) {
      return Math.min(getChildAt(0).getMeasuredHeight(), maxHeight);
    } else {
      int imageLength = maxWidth / 2;
      int childCount = getChildCount() - 1;
      return imageLength * (childCount / 2 + 1) + childCount / 2 * DEFAULT_LINE;
    }
  }

  private int measureWidth() {
    if (getChildCount() == 0) {
      return 0;
    } else if (getChildCount() == 1) {
      return getChildAt(0).getMeasuredWidth();
    } else {
      return maxWidth + DEFAULT_LINE;
    }
  }

  private void addAllItemImageView(List<String> imageUrlList, int length) {
    for (String s : imageUrlList) {
      addItemImageView(s, length, length);
    }
  }

  @Override
  public void onClick(View view) {
    if (onItemClickListener != null) {
      onItemClickListener.onItemImageClick(indexOfChild(view));
    }
  }

  public void setOnItemClickListener(
      OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }
}
