package com.hyc.helper.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.headzoomlayout.HeadZoomLayout;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.ShowInfoViewHolder;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.InfoEntity;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.helper.DisposableManager;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.util.DensityUtil;
import java.util.ArrayList;
import java.util.List;

public class LostFindDetailActivity extends BaseActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.vp_goods_images)
  ViewPager vpGoodsImages;
  @BindView(R.id.tv_page)
  TextView tvPage;
  @BindView(R.id.tv_title)
  TextView tvTitle;
  @BindView(R.id.tv_content)
  TextView tvContent;
  @BindView(R.id.rv_info)
  RecyclerView rvInfo;
  @BindView(R.id.ll_info)
  LinearLayout llInfo;
  @BindView(R.id.tv_fail_tip)
  TextView tvFailTip;
  @BindView(R.id.fl_head)
  FrameLayout flHead;
  @BindView(R.id.hzl_detail)
  HeadZoomLayout hzlDetail;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_second_goods_detail;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBar();
    LostBean.GoodsBean goodsBean = (LostBean.GoodsBean) bundle.getSerializable("lost");
    if (goodsBean != null) {
      initImageBrowsing(goodsBean.getPics());
      initLostFindInfo(goodsBean);
    }
  }

  private void initLostFindInfo(LostBean.GoodsBean dataBean) {
    String type = dataBean.getType();
    tvTitle.setText(dataBean.getTit());
    tvContent.setText(dataBean.getContent());
    UiHelper.initLinkTextView(tvContent, this);
    rvInfo.setLayoutManager(new GridLayoutManager(this, 2));
    List<InfoEntity> data = new ArrayList<>();
    data.add(new InfoEntity(type.equals("1") ? "拾到物品" : "丢失物品", dataBean.getTit()));
    data.add(new InfoEntity(type.equals("1") ? "拾到时间" : "丢失时间", dataBean.getTime()));
    data.add(new InfoEntity(type.equals("1") ? "拾到地点" : "丢失地点", dataBean.getLocate()));
    data.add(new InfoEntity("联系电话", dataBean.getPhone()));
    data.add(new InfoEntity("发布用户", dataBean.getUsername()));
    data.add(new InfoEntity("发布时间", dataBean.getCreated_on()));
    rvInfo.setAdapter(
        new BaseRecycleAdapter<>(data, R.layout.layout_bottom_info, ShowInfoViewHolder.class));
    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
    params.setMargins(0,getStatusHeight(), 0, 0);
  }

  private void initImageBrowsing(List<String> pic) {
    if (pic == null || pic.size() == 0) {
      setToolBarTitle("失物详情");
      toolbar.setBackgroundColor(UiHelper.getColor(R.color.colorPrimary));
      hzlDetail.setZoomEnable(false);
      flHead.setVisibility(View.GONE);
      setStatusBarColor(R.color.colorPrimary);
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) hzlDetail.getLayoutParams();
      params.setMargins(0,DensityUtil.dip2px(50), 0, 0);
      llInfo.setLayoutParams(params);
      return;
    }
    tvPage.setText(String.format(UiHelper.getString(R.string.page), 1, pic.size()));
    vpGoodsImages.setAdapter(new ViewPagerAdapter(pic));
    vpGoodsImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int i, float v, int i1) {

      }

      @Override
      public void onPageSelected(int i) {
        tvPage.setText(String.format(UiHelper.getString(R.string.page), i + 1, pic.size()));
      }

      @Override
      public void onPageScrollStateChanged(int i) {

      }
    });
  }

  public class ViewPagerAdapter extends PagerAdapter {

    private DisposableManager disposableManager;

    List<String> pics;

    ViewPagerAdapter(List<String> pics) {
      this.pics = pics;
      disposableManager = new DisposableManager(getCount());
    }

    @Override
    public int getCount() {
      return pics == null ? 0 : pics.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
      return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
      ImageView imageView = new ImageView(container.getContext());
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      ImageRequestHelper.loadNotCropImage(container.getContext(), pics.get(position), imageView);
      imageView.setOnClickListener(view -> PictureBrowsingActivity.goToPictureBrowsingActivity(
          LostFindDetailActivity.this, position,
          (ArrayList<String>) pics));
      container.addView(imageView);
      return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
      disposableManager.cancelDisposable(position);
      container.removeView((View) object);
    }
  }
}
