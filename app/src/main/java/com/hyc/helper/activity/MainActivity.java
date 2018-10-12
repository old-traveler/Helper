package com.hyc.helper.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.activity.fragment.LostFindFragment;
import com.hyc.helper.activity.fragment.SecondHandFragment;
import com.hyc.helper.activity.fragment.StatementFragment;
import com.hyc.helper.activity.fragment.TimetableFragment;
import com.hyc.helper.adapter.TechFragmentPageAdapter;
import com.hyc.helper.adapter.viewholder.SearchPeopleViewHolder;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.base.fragment.BaseFragment;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.bean.FindPeopleBean;
import com.hyc.helper.helper.ConfigureHelper;
import com.hyc.helper.helper.CupidHelper;
import com.hyc.helper.helper.DateHelper;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.helper.UpdateAppHelper;
import com.hyc.helper.model.ConfigModel;
import com.hyc.helper.model.UserModel;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

  @BindView(R.id.tb_main)
  TabLayout tbMain;
  @BindView(R.id.appbar)
  AppBarLayout appbar;
  @BindView(R.id.vp_main)
  ViewPager vpMain;
  @BindView(R.id.nav_view)
  NavigationView navView;
  @BindView(R.id.main_content)
  DrawerLayout mainContent;
  @BindView(R.id.rv_search_people)
  RecyclerView rvSearchPeople;
  private List<BaseFragment> list;
  private TechFragmentPageAdapter adapter;
  private ListPopupWindow weekListPopWindow;
  private MenuItem selectWeek;
  private UserModel userModel = new UserModel();
  private ConfigModel configModel = new ConfigModel();
  private BaseRecycleAdapter<FindPeopleBean.DataBean,SearchPeopleViewHolder> searchAdapter;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_main;
  }

  @Override
  public void onTabSelected(TabLayout.Tab tab) {

  }

  @Override
  public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override
  public void onTabReselected(TabLayout.Tab tab) {
    if (adapter.getItem(tab.getPosition()) instanceof BaseListFragment) {
      ((BaseListFragment) adapter.getItem(tab.getPosition())).backToTop();
    }
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBar(R.id.toolbar, "Helper", R.drawable.ic_more_info);
    initTabLayout();
    initViewPager();
    initSearchList();
    checkUpdate(configModel.getConfigInfo());
  }

  private void initSearchList() {
    rvSearchPeople.setLayoutManager(new LinearLayoutManager(this));
    rvSearchPeople.setItemAnimator(new DefaultItemAnimator());
    searchAdapter = new BaseRecycleAdapter<>(R.layout.item_search_people,SearchPeopleViewHolder.class);
    rvSearchPeople.setAdapter(searchAdapter);
  }

  private void initViewPager() {
    list = new ArrayList<>(tbMain.getTabCount());
    list.add(new TimetableFragment());
    list.add(new StatementFragment());
    list.add(new SecondHandFragment());
    list.add(new LostFindFragment());
    adapter = new TechFragmentPageAdapter(getSupportFragmentManager(), list);
    vpMain.setAdapter(adapter);
    vpMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tbMain));
    vpMain.setOffscreenPageLimit(5);
  }

  private void initTabLayout() {
    tbMain.addTab(tbMain.newTab().setText(R.string.course_table));
    tbMain.addTab(tbMain.newTab().setText(R.string.school_statement));
    tbMain.addTab(tbMain.newTab().setText(R.string.second_market));
    tbMain.addTab(tbMain.newTab().setText(R.string.lost_find));
    tbMain.addOnTabSelectedListener(this);
    tbMain.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpMain));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      mainContent.openDrawer(GravityCompat.START);
      return true;
    } else if (item.getItemId() == R.id.action_select) {
      weekListPopWindow.setAnchorView(findViewById(R.id.action_select));
      weekListPopWindow.show();
      Objects.requireNonNull(
          weekListPopWindow.getListView()).setBackgroundColor(UiHelper.getColor(R.color.white));
      return true;
    }
    return false;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    selectWeek = menu.findItem(R.id.action_select);
    selectWeek.setTitle(UiHelper.getString(R.string.week_tip, DateHelper.getCurWeek()));
    MenuItem searchItem = menu.findItem(R.id.action_search);
    initSearchView((SearchView) searchItem.getActionView());
    initListPopView();
    return true;
  }

  private void initSearchView(SearchView searchView) {
    searchView.setQueryHint(UiHelper.getString(R.string.input_name));
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
        requestInfoFromApi(s);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)){
          searchAdapter.setDataList(null);
        }
        return false;
      }
    });
  }

  @SuppressLint("CheckResult")
  private void requestInfoFromApi(String name){
    showLoadingView();
    userModel.findUserInfoByName(name)
        .subscribe(findPeopleBean -> {
          closeLoadingView();
          searchAdapter.setDataList(findPeopleBean.getData());
        }, throwable -> {
          closeLoadingView();
          ToastHelper.toast(throwable.getMessage());
        });
  }

  private void initListPopView() {
    List<String> list = new ArrayList<>();
    for (int i = 1; i <= 20; i++) {
      list.add(UiHelper.getString(R.string.week_tip, i));
    }
    weekListPopWindow = new ListPopupWindow(this);
    weekListPopWindow.setAdapter(new ArrayAdapter<>(this, R.layout.item_select_week, list));
    weekListPopWindow.setWidth(DensityUtil.dip2px(70f));
    weekListPopWindow.setHeight(DensityUtil.dip2px(150f));
    weekListPopWindow.setModal(true);
    weekListPopWindow.setOnItemClickListener((adapterView, view, i, l) -> {
      adapter.getFragmentList(TimetableFragment.class, 0).switchWeek(i + 1);
      selectWeek.setTitle(UiHelper.getString(R.string.week_tip, i + 1));
      weekListPopWindow.dismiss();
    });
  }

  private void checkUpdate(ConfigureBean configureBean) {
    if (ConfigureHelper.getVersionCode(this) < configureBean.getUpdate_version_code()
        && !TextUtils.isEmpty(configureBean.getUpdate())) {
      showTipDialog(UiHelper.getString(R.string.update_tip), configureBean.getContent(),
          isPosition -> {
            if (isPosition) {
              startUpdate(configureBean);
            }
          });
    }
  }

  @SuppressLint("CheckResult")
  public void startUpdate(ConfigureBean configureBean) {
    RxPermissions rxPermissions = new RxPermissions(this);
    rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (granted) {
            downApk(configureBean);
          }
        });
  }

  public void downApk(ConfigureBean configureBean) {
    UpdateAppHelper.download(configureBean.getUpdate(), UiHelper.getString(R.string.apk_name),
        this);
    configureBean.setUpdate("");
    configModel.setConfigInfo(configureBean);
  }
}
