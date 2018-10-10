package com.hyc.helper.base.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.util.ToastHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;

public abstract class BaseListActivity<T, VH extends BaseViewHolder<T>>
    extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

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
  }

  private void initRecyclerView() {
    RecyclerView recyclerView = getRecycleView();
    mRefreshLayout = getRefreshLayout();
    adapter = getRecycleAdapter();
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setLayoutManager(getLayoutManager());
    recyclerView.setAdapter(adapter);
    if (mRefreshLayout != null) {
      initRecyclerView();
      mRefreshLayout.setOnRefreshListener(this);
      mRefreshLayout.setOnLoadMoreListener(this);
      mRefreshLayout.autoRefresh();
    }
  }

  protected abstract RecyclerView getRecycleView();

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

  protected abstract SmartRefreshLayout getRefreshLayout();

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

  public abstract BaseRecycleAdapter<T, VH> getRecycleAdapter();

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
  }

  public void loadFail(String msg) {
    ToastHelper.toast(msg);
  }
}
