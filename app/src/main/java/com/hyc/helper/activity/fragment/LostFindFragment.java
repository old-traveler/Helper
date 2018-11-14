package com.hyc.helper.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hyc.helper.R;
import com.hyc.helper.activity.LosePublishActivity;
import com.hyc.helper.activity.LostFindDetailActivity;
import com.hyc.helper.adapter.viewholder.LostFindViewHolder;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.LostBean;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.model.LostGoodsModel;
import com.hyc.helper.model.UserModel;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class LostFindFragment
    extends BaseListFragment<LostBean.GoodsBean, LostBean, LostFindViewHolder> {

  private LostGoodsModel lostGoodsModel = new LostGoodsModel();
  private UserModel userModel = new UserModel();
  private String userId;

  @Override
  protected BaseRecycleAdapter<LostBean.GoodsBean, LostFindViewHolder> setRecycleAdapter() {
    return new BaseRecycleAdapter<>(null, R.layout.item_lost, LostFindViewHolder.class);
  }

  public static LostFindFragment newInstance(String userId) {
    LostFindFragment lostFindFragment = new LostFindFragment();
    Bundle bundle = new Bundle();
    bundle.putString(Constant.USER_ID, userId);
    lostFindFragment.setArguments(bundle);
    return lostFindFragment;
  }

  @Override
  protected void initLayoutView(View view) {
    super.initLayoutView(view);
    FloatingActionButton floatingActionButton = view.findViewById(R.id.fb_publish);
    floatingActionButton.setOnClickListener(view1 -> startActivityForResult(
        new Intent(getActivity(), LosePublishActivity.class), 2010));
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
  protected void initRecyclerView(View view) {
    super.initRecyclerView(view);
    getRecyclerView().setLayoutManager(
        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
    if (TextUtils.isEmpty(userId)) {
      lostGoodsModel.getAllLostGoods(page, this);
    } else {
      lostGoodsModel.getPersonalLost(userModel.getStudentId(),
          userModel.getCurUserInfo().getRemember_code_app(), page, userId, this);
    }
  }

  @Override
  protected List<LostBean.GoodsBean> getData(LostBean lostBean) {
    if (lostBean.getCurrent_page()<getCurPage()){
      return null;
    }
    return lostBean.getGoods();
  }

  @Override
  protected int getLayoutId() {
    return R.layout.fragment_lost_find;
  }

  @Override
  public void onItemClick(LostBean.GoodsBean itemData, View view, int position) {
    super.onItemClick(itemData, view, position);
    if (R.id.tv_delete == view.getId()){
      deleteLostItem(itemData.getId(),position);
    }else {
      Bundle bundle = new Bundle();
      bundle.putSerializable("lost",itemData);
      goToOtherActivity(LostFindDetailActivity.class,bundle,false);
    }
  }

  private void deleteLostItem(String id,int position) {
    showLoadingView();
    addDisposable(lostGoodsModel.deleteLost(userModel.getCurUserInfo(),id)
        .subscribe(baseRequestBean -> {
          getRecycleAdapter().removeItemFormList(position);
          closeLoadingView();
        }, throwable -> {
          closeLoadingView();
          ToastHelper.toast(throwable.getMessage());
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
