package com.hyc.helper.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hyc.helper.R;
import com.hyc.helper.activity.SecondGoodsDetailActivity;
import com.hyc.helper.activity.SecondMarketPublishActivity;
import com.hyc.helper.adapter.viewholder.SecondGoodsViewHolder;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.SecondHandBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.model.SecondGoodsModel;
import com.hyc.helper.util.parrot.InitialParam;
import com.hyc.helper.view.SpacesItemDecoration;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SecondHandFragment
    extends BaseListFragment<SecondHandBean.GoodsBean, SecondHandBean, SecondGoodsViewHolder> {

  private SecondGoodsModel model = new SecondGoodsModel();
  private UserModel userModel = new UserModel();
  @InitialParam(key = Constant.USER_ID)
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
  protected void initLayoutView(View view) {
    super.initLayoutView(view);
    FloatingActionButton floatingActionButton = view.findViewById(R.id.fb_publish_second);
    floatingActionButton.setOnClickListener(
        view1 -> startActivityForResult(
            new Intent(getActivity(), SecondMarketPublishActivity.class), 2010));
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
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
    if (secondHandBean.getCurrent_page() < getCurPage()) {
      return null;
    }
    return secondHandBean.getGoods();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_second_hand;
  }

  @Override
  public void onItemClick(SecondHandBean.GoodsBean itemData, View view, int position) {
    super.onItemClick(itemData, view, position);
    if (view.getId() == R.id.tv_delete){
      deleteGoods(itemData.getId(),position);
    }else {
      Bundle bundle = new Bundle();
      bundle.putString("goodsId", itemData.getId());
      goToOtherActivity(SecondGoodsDetailActivity.class, bundle, false);
    }

  }

  public void deleteGoods(String id,int position){
    showLoadingView();
    addDisposable(model.deleteGoods(userModel.getCurUserInfo(),id)
        .subscribe(baseRequestBean -> {
          if (baseRequestBean.getCode() == 200){
            getRecycleAdapter().removeItemFormList(position);
          }else {
            ToastHelper.toast(baseRequestBean.getCode());
          }
          closeLoadingView();
        }, throwable -> {
          ToastHelper.toast(throwable.getMessage());
          closeLoadingView();
        }));
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 2010 && resultCode == RESULT_OK) {
      refresh();
    }
  }


}
