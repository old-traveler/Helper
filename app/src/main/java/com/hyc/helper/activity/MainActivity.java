package com.hyc.helper.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.activity.fragment.LostFindFragment;
import com.hyc.helper.activity.fragment.SecondHandFragment;
import com.hyc.helper.activity.fragment.StatementFragment;
import com.hyc.helper.activity.fragment.TimetableFragment;
import com.hyc.helper.adapter.TechFragmentPageAdapter;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.fragment.BaseFragment;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.helper.DensityHelper;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity  {

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
  private List<BaseFragment> list;
  private TechFragmentPageAdapter adapter;
  private ListPopupWindow mListPop;
  private MenuItem selectWeek;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_main;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBar(R.id.toolbar, "Helper", R.drawable.ic_more_info);
    tbMain.addTab(tbMain.newTab().setText("课表"));
    tbMain.addTab(tbMain.newTab().setText("校园说说"));
    tbMain.addTab(tbMain.newTab().setText("二手市场"));
    tbMain.addTab(tbMain.newTab().setText("失物招领"));
    list = new ArrayList<>(4);
    list.add(new TimetableFragment());
    list.add(new StatementFragment());
    list.add(new SecondHandFragment());
    list.add(new LostFindFragment());
    adapter = new TechFragmentPageAdapter(getSupportFragmentManager(),list);
    vpMain.setAdapter(adapter);
    //tbMain.setupWithViewPager(vpMain);
    vpMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tbMain));
    tbMain.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpMain));
    vpMain.setOffscreenPageLimit(5);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      mainContent.openDrawer(GravityCompat.START);
      return true;
    }else if (item.getItemId() == R.id.action_select){
      mListPop.setAnchorView(findViewById(R.id.action_select));
      mListPop.show();
      mListPop.getListView().setBackgroundColor(UiHelper.getColor(R.color.white));
      return true;
    }
    return false;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main,menu);
    selectWeek = menu.findItem(R.id.action_select);
    initListPopView();
    return true;
  }

  private void initListPopView() {
    List<String> list = new ArrayList<>();
    for (int i = 1; i <= 20 ; i++) {
      list.add("第"+i+"周");
    }
    mListPop = new ListPopupWindow(this);
    mListPop.setAdapter(new ArrayAdapter<>(this,R.layout.item_select_week,list));
    mListPop.setWidth(DensityHelper.dip2px(70f));
    mListPop.setHeight(DensityHelper.dip2px(150f));
    mListPop.setModal(true);
    mListPop.setOnItemClickListener((adapterView, view, i, l) -> {
      adapter.getFragmentList(TimetableFragment.class,0).switchWeek(i+1);
      selectWeek.setTitle("第"+(i+1)+"周");
      mListPop.dismiss();
    });
  }
}
