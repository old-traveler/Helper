package com.hyc.helper.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.ShowInfoViewHolder;
import com.hyc.helper.base.activity.BaseRequestActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.GoodsDetailBean;
import com.hyc.helper.bean.InfoEntity;
import com.hyc.helper.helper.DisposableManager;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.model.SecondGoodsModel;
import com.hyc.helper.model.UserModel;
import java.util.ArrayList;
import java.util.List;

public class SecondGoodsDetailActivity extends BaseRequestActivity<GoodsDetailBean> {

  public UserModel userModel = new UserModel();
  public SecondGoodsModel secondGoodsModel = new SecondGoodsModel();

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

  private String goodsId;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_second_goods_detail;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBar(R.id.toolbar);
    goodsId = bundle.getString("goodsId");
  }

  @Override
  protected void requestDataFromApi() {
    secondGoodsModel.getGoodsDetailInfo(userModel.getCurUserInfo(), goodsId, this);
  }

  @Override
  protected void onSuccessGetData(GoodsDetailBean goodsDetailBean) {
    initImageBrowsing(goodsDetailBean.getData().getPics_src());
    initGoodsInfo(goodsDetailBean.getData());
  }

  private void initGoodsInfo(GoodsDetailBean.DataBean dataBean) {
    tvTitle.setText(dataBean.getTit());
    tvContent.setText(dataBean.getContent());
    UiHelper.initLinkTextView(tvContent, this);
    rvInfo.setLayoutManager(new GridLayoutManager(this, 2));
    List<InfoEntity> data = new ArrayList<>();
    data.add(new InfoEntity("商品价格", String.format("¥%s", dataBean.getPrize())));
    data.add(new InfoEntity("商品成色", dataBean.getAttr()));
    data.add(new InfoEntity("联系方式", dataBean.getPhone()));
    data.add(new InfoEntity("发布区域", dataBean.getAddress()));
    data.add(new InfoEntity("发布用户", dataBean.getUsername()));
    data.add(new InfoEntity("发布时间", dataBean.getCreated_on()));
    rvInfo.setAdapter(
        new BaseRecycleAdapter<>(data, R.layout.layout_bottom_info, ShowInfoViewHolder.class));
  }

  private void initImageBrowsing(List<String> pic) {
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

    List<String> pics;

    private DisposableManager disposableManager;

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
          SecondGoodsDetailActivity.this, position,
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
