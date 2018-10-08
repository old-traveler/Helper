package com.hyc.helper.activity.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.SecondGoodsViewHolder;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.bean.SecondHandBean;
import com.hyc.helper.helper.DensityHelper;
import com.hyc.helper.model.SecondGoodsModel;
import com.hyc.helper.view.SpacesItemDecoration;
import java.util.List;

public class SecondHandFragment extends BaseListFragment<SecondHandBean.GoodsBean,SecondHandBean,SecondGoodsViewHolder> {

  private SecondGoodsModel model = new SecondGoodsModel();

  @Override
  protected BaseRecycleAdapter<SecondHandBean.GoodsBean, SecondGoodsViewHolder> setRecycleAdapter() {
    return new BaseRecycleAdapter<>(null,R.layout.item_second,SecondGoodsViewHolder.class);
  }

  @Override
  protected int getRefreshLayoutId() {
    return R.id.sfl_second;
  }

  @Override
  protected int getRecycleViewId() {
    return R.id.rv_second;
  }

  @Override
  protected void initRecyclerView(View view) {
    super.initRecyclerView(view);
    getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(),2));
    getRecyclerView().addItemDecoration(new SpacesItemDecoration(DensityHelper.dip2px(8f)));
  }

  @Override
  protected void requestListData(int page) {
    model.getSecondMarketGoods(page,this);
  }

  @Override
  protected List<SecondHandBean.GoodsBean> getData(SecondHandBean secondHandBean) {
    return secondHandBean.getGoods();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_second_hand;
  }
}
