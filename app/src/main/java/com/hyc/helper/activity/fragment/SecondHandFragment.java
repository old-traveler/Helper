package com.hyc.helper.activity.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.SecondGoodsViewHolder;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.bean.SecondHandBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.model.SecondGoodsModel;
import com.hyc.helper.view.SpacesItemDecoration;
import java.util.List;

public class SecondHandFragment
    extends BaseListFragment<SecondHandBean.GoodsBean, SecondHandBean, SecondGoodsViewHolder> {

  private SecondGoodsModel model = new SecondGoodsModel();
  private UserModel userModel = new UserModel();
  private String userId;

  @Override
  protected BaseRecycleAdapter<SecondHandBean.GoodsBean, SecondGoodsViewHolder> setRecycleAdapter() {
    return new BaseRecycleAdapter<>(null, R.layout.item_second, SecondGoodsViewHolder.class);
  }

  public static SecondHandFragment newInstance(String userId) {
    SecondHandFragment secondHandFragment = new SecondHandFragment();
    Bundle bundle = new Bundle();
    bundle.putString(Constant.USER_ID, userId);
    secondHandFragment.setArguments(bundle);
    return secondHandFragment;
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (getArguments() != null) {
      userId = getArguments().getString(Constant.USER_ID);
    }
    return super.onCreateView(inflater, container, savedInstanceState);
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
    getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(), 2));
    getRecyclerView().addItemDecoration(new SpacesItemDecoration(DensityUtil.dip2px(8f)));
  }

  @Override
  protected void requestListData(int page) {
    if (TextUtils.isEmpty(userId)) {
      model.getSecondMarketGoods(page, this);
    } else {
      model.getPersonalMarket(userModel.getStudentId(),
          userModel.getCurUserInfo().getRemember_code_app(), page, userId, this);
    }
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
