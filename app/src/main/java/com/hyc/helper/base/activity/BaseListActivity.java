package com.hyc.helper.base.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.listener.OnItemClickListener;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.helper.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.List;

public abstract class BaseListActivity<T, B extends BaseRequestBean, VH extends BaseViewHolder<T>>
    extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, Observer<B>,
    OnItemClickListener<T> {

  private Disposable disposable;
  private RecyclerView recyclerView;
  private static final int delay = 1500;
  private SmartRefreshLayout mRefreshLayout;
  private int pageStart = 1;
  private int page = pageStart;
  private BaseRecycleAdapter<T, VH> adapter;
  //用于区分没有更多内容时停止加载更多和单纯的禁止加载更多
  private boolean enableLoadMore = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initRecyclerView();
    startRequest();
  }

  private void startRequest() {
    if (mRefreshLayout != null) {
      mRefreshLayout.setOnRefreshListener(this);
      mRefreshLayout.setOnLoadMoreListener(this);
      mRefreshLayout.autoRefresh();
    }
  }

  public void refresh() {
    if (mRefreshLayout != null) {
      mRefreshLayout.autoRefresh();
    }
  }

  public void backToTop(){
    if (recyclerView!=null){
      recyclerView.scrollToPosition(0);
    }
  }

  public RecyclerView getRecyclerView() {
    return recyclerView;
  }

  public BaseRecycleAdapter<T, VH> getRecycleAdapter() {
    return adapter;
  }

  protected void initRecyclerView() {
    recyclerView = findViewById(getRecycleViewId());
    mRefreshLayout = findViewById(getRefreshLayoutId());
    adapter = setRecycleAdapter();
    adapter.setOnItemClickListener(this);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setLayoutManager(getLayoutManager());
    recyclerView.setAdapter(adapter);
  }

  protected abstract BaseRecycleAdapter<T, VH> setRecycleAdapter();

  protected abstract int getRefreshLayoutId();

  protected abstract int getRecycleViewId();


  public void setEnableLoadMore(boolean enableLoadMore) {
    if (mRefreshLayout != null) {
      mRefreshLayout.setEnableLoadMore(enableLoadMore);
    }
    this.enableLoadMore = enableLoadMore;
  }

  public void setEnableRefresh(boolean enableRefresh) {
    if (mRefreshLayout != null) {
      mRefreshLayout.setEnableRefresh(enableRefresh);
    }
  }


  @Override
  public void onRefresh(@NonNull RefreshLayout refreshLayout) {
    remakePage();
    requestListData(page);
  }

  protected abstract void requestListData(int page);

  public void setPageStart(int pageStart) {
    this.pageStart = pageStart;
  }

  private void remakePage() {
    mRefreshLayout.setEnableLoadMore(enableLoadMore);
    page = pageStart;
  }

  @Override
  public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
    this.page = this.page + 1;
    requestListData(page);
  }


  public RecyclerView.LayoutManager getLayoutManager() {
    return new LinearLayoutManager(this);
  }


  public void loadMoreFinish(List<T> data) {
    if (page == pageStart) {
      if (null != data) {
        adapter.setDataList(data);
      }
      mRefreshLayout.finishRefresh();
    } else {
      if (null != data && data.size() > 0) {
        adapter.appendDataToList(data);
      } else if (null != data) {
        mRefreshLayout.setEnableLoadMore(false);
      }
      mRefreshLayout.finishLoadMore();
    }
    page++;
  }

  public void refreshFailure(String msg) {
    mRefreshLayout.finishRefresh(delay);
    ToastHelper.toast(msg);
  }

  public void loadMoreFailure(String msg) {
    mRefreshLayout.finishLoadMore(delay);
    ToastHelper.toast(msg);
  }

  public void showRefreshing() {
    if (!mRefreshLayout.getState().equals(RefreshState.Refreshing)) {
      mRefreshLayout.autoRefresh();
    }
  }

  public void stopRefreshing() {
    mRefreshLayout.finishRefresh();
    mRefreshLayout.finishLoadMore(0);
  }

  @Override
  public void onSubscribe(Disposable d) {
    disposable = d;
  }

  @Override
  public void onNext(B ts) {
    if (ts.getCode() == Constant.REQUEST_SUCCESS) {
      loadMoreFinish(getData(ts));
    } else if (ts.getCode() == Constant.NEED_API_DATA){
      disposable.dispose();
      requestListData(page);
    } else if (!TextUtils.isEmpty(ts.getMsg())) {
      ToastHelper.toast(ts.getMsg());
    } else {
      ToastHelper.toast(String.valueOf(ts.getCode()));
    }
  }

  public void dispose(){
    if (disposable != null && !disposable.isDisposed()){
      disposable.dispose();
    }
  }

  protected abstract List<T> getData(B b);

  @Override
  public void onError(Throwable e) {
    stopRefreshing();
    ToastHelper.toast(e.getMessage());
  }

  @Override
  public void onComplete() {
    stopRefreshing();
  }

  @Override
  public void onItemClick(T itemData, View view, int position) {

  }

  public void loadFail(String msg) {
    ToastHelper.toast(msg);
  }
}
