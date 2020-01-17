package com.hyc.helper.im;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.core.ConnectionStatus;
import com.hyc.helper.R;
import com.hyc.helper.activity.UserInfoActivity;
import com.hyc.helper.adapter.viewholder.MessageViewHolder;
import com.hyc.helper.adapter.viewholder.SearchPeopleViewHolder;
import com.hyc.helper.annotation.Subscribe;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.FindPeopleBean;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.RxBus;
import com.hyc.helper.util.ThreadMode;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class ConversationActivity extends BaseActivity implements OnRefreshListener {

  @BindView(R.id.rv_conversation)
  RecyclerView rvConversation;
  @BindView(R.id.sfl_conversation)
  SmartRefreshLayout sflConversation;
  private BaseRecycleAdapter<BmobIMConversation, MessageViewHolder> adapter;
  @BindView(R.id.rv_search_people)
  RecyclerView rvSearchPeople;
  private String searchUsername;
  private BaseRecycleAdapter<FindPeopleBean.DataBean, SearchPeopleViewHolder> searchAdapter;
  private UserModel userModel = new UserModel();

  @Override
  protected void onStart() {
    super.onStart();
    RxBus.getDefault().register(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    RxBus.getDefault().unregister(this);
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_conversation;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBarTitle(R.string.message);
    rvConversation.setLayoutManager(new LinearLayoutManager(this));
    rvConversation.setItemAnimator(new DefaultItemAnimator());
    DividerItemDecoration divider =
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
    divider.setDrawable(getResources().getDrawable(R.drawable.bg_message_divider));
    rvConversation.addItemDecoration(divider);
    sflConversation.setEnableLoadMore(false);
    sflConversation.setOnRefreshListener(this);
    adapter = new BaseRecycleAdapter<>(R.layout.item_message, MessageViewHolder.class);
    rvConversation.setAdapter(adapter);
    initSearchList();
    sflConversation.autoRefresh();
    adapter.setOnItemClickListener((itemData, view, position) -> {
      Bundle bundle1 = new Bundle();
      bundle1.putSerializable(Constant.CHAT_INTENT_KEY, itemData);
      goToOtherActivity(ChatActivity.class, bundle1, false);
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_conversation, menu);
    MenuItem searchItem = menu.findItem(R.id.action_search);
    initSearchView((SearchView) searchItem.getActionView());
    return true;
  }

  @Subscribe(threadMode = ThreadMode.MAIN, eventType = {
      Constant.EventType.CHANGE_CONNECT, Constant.EventType.NET_AVAILABLE,
      Constant.EventType.IM_MESSAGE
  })
  public void onEvent(MessageEvent<ConnectionStatus> event) {
    switch (event.getType()) {
      case Constant.EventType.CHANGE_CONNECT:
        loadConversation(event.getData());
        break;
      case Constant.EventType.NET_AVAILABLE:
        if (ConnectManager.getDefault().getCurrentStatus().equals(ConnectionStatus.DISCONNECT)) {
          sflConversation.autoRefresh();
        }
        break;
      case Constant.EventType.IM_MESSAGE:
        adapter.setDataList(BmobIM.getInstance().loadAllConversation());
        break;
    }
  }

  private void initSearchList() {
    rvSearchPeople.setLayoutManager(new LinearLayoutManager(this));
    rvSearchPeople.setItemAnimator(new DefaultItemAnimator());
    searchAdapter =
        new BaseRecycleAdapter<>(R.layout.item_search_people, SearchPeopleViewHolder.class);
    rvSearchPeople.setAdapter(searchAdapter);
    searchAdapter.setOnItemClickListener(
        (itemData, view, position) -> UserInfoActivity.goToUserInfoActivity(
            ConversationActivity.this,
            itemData.getId(), searchUsername, null, itemData.getHead_pic_thumb()));
  }

  private void initSearchView(SearchView searchView) {
    searchView.setQueryHint(UiHelper.getString(R.string.input_name));
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
        searchUsername = s;
        requestInfoFromApi(s);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
          searchAdapter.setDataList(null);
        }
        return false;
      }
    });
  }

  private void requestInfoFromApi(String name) {
    hideInputWindow();
    addDisposable(userModel.findUserInfoByName(name)
        .subscribe(findPeopleBean -> searchAdapter.setDataList(findPeopleBean.getData()),
            throwable -> ToastHelper.toast(throwable.getMessage())));
  }

  private void loadConversation(ConnectionStatus currentStatus) {
    switch (currentStatus) {
      case DISCONNECT:
        stopRefreshing();
        ToastHelper.toast("连接已断开");
        break;
      case CONNECTED:
        stopRefreshing();
        adapter.setDataList(BmobIM.getInstance().loadAllConversation());
        break;
      case NETWORK_UNAVAILABLE:
        stopRefreshing();
        ToastHelper.toast("网络已经断开");
        break;
      case CONNECTING:
        break;
    }
  }

  private void stopRefreshing() {
    sflConversation.finishRefresh(0);
    closeLoadingView();
  }

  @Override
  public void onRefresh(@NonNull RefreshLayout refreshLayout) {
    if (ConnectManager.getDefault().getCurrentStatus().equals(ConnectionStatus.CONNECTED)) {
      refreshLayout.finishRefresh(0);
      adapter.setDataList(BmobIM.getInstance().loadAllConversation());
    } else {
      ConnectManager.getDefault().connect();
    }
  }
}
