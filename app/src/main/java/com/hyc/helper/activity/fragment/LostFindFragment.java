package com.hyc.helper.activity.fragment;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.LostFindViewHolder;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.model.LostGoodsModel;
import java.util.List;

public class LostFindFragment extends BaseListFragment<LostBean.GoodsBean,LostBean,LostFindViewHolder> {

  private LostGoodsModel lostGoodsModel= new LostGoodsModel();

  @Override
  protected BaseRecycleAdapter<LostBean.GoodsBean, LostFindViewHolder> getRecycleAdapter() {
    return new BaseRecycleAdapter<>(null,R.layout.item_lost,LostFindViewHolder.class);
  }

  @Override
  protected void initRecyclerView(View view) {
    super.initRecyclerView(view);
    getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
  }

  @Override
  protected int getRefreshLayoutId() {
    return R.id.sfl_lost;
  }

  @Override
  protected int getRecycleViewId() {
    return R.id.rv_lost;
  }

  @Override
  protected void requestListData(int page) {
    lostGoodsModel.getAllLostGoods(page,this);
  }

  @Override
  protected List<LostBean.GoodsBean> getData(LostBean lostBean) {
    return lostBean.getGoods();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_lost_find;
  }
}
