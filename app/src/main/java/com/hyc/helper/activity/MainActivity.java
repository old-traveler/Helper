package com.hyc.helper.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import com.hyc.helper.bean.ConfigureBean;
import com.hyc.helper.helper.ConfigureHelper;
import com.hyc.helper.helper.CupidHelper;
import com.hyc.helper.helper.DateHelper;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.helper.UpdateAppHelper;
import com.hyc.helper.model.ConfigModel;
import com.hyc.helper.model.UserModel;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity {

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
  private UserModel userModel = new UserModel();
  private ConfigModel configModel = new ConfigModel();

  @Override
  protected int getContentViewId() {
    return R.layout.activity_main;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBar(R.id.toolbar, "Helper", R.drawable.ic_more_info);
    tbMain.addTab(tbMain.newTab().setText("课程表"));
    tbMain.addTab(tbMain.newTab().setText("校园说说"));
    tbMain.addTab(tbMain.newTab().setText("二手市场"));
    tbMain.addTab(tbMain.newTab().setText("失物招领"));
    tbMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
        if (adapter.getItem(tab.getPosition()) instanceof BaseListFragment){
          ((BaseListFragment)adapter.getItem(tab.getPosition())).backToTop();
        }
      }
    });
    list = new ArrayList<>(4);
    list.add(new TimetableFragment());
    list.add(new StatementFragment());
    list.add(new SecondHandFragment());
    list.add(new LostFindFragment());
    adapter = new TechFragmentPageAdapter(getSupportFragmentManager(), list);
    vpMain.setAdapter(adapter);
    vpMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tbMain));
    tbMain.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpMain));
    vpMain.setOffscreenPageLimit(5);
    ConfigureBean configureBean = configModel.getConfigInfo();
    cupid(configureBean, CupidHelper.cupid(configureBean, userModel.getStudentId()));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      mainContent.openDrawer(GravityCompat.START);
      return true;
    } else if (item.getItemId() == R.id.action_select) {
      mListPop.setAnchorView(findViewById(R.id.action_select));
      mListPop.show();
      Objects.requireNonNull(
          mListPop.getListView()).setBackgroundColor(UiHelper.getColor(R.color.white));
      return true;
    }
    return false;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    selectWeek = menu.findItem(R.id.action_select);
    selectWeek.setTitle("第" + DateHelper.getCurWeek() + "周");
    initListPopView();
    return true;
  }

  private void initListPopView() {
    List<String> list = new ArrayList<>();
    for (int i = 1; i <= 20; i++) {
      list.add("第" + i + "周");
    }
    mListPop = new ListPopupWindow(this);
    mListPop.setAdapter(new ArrayAdapter<>(this, R.layout.item_select_week, list));
    mListPop.setWidth(DensityUtil.dip2px(70f));
    mListPop.setHeight(DensityUtil.dip2px(150f));
    mListPop.setModal(true);
    mListPop.setOnItemClickListener((adapterView, view, i, l) -> {
      adapter.getFragmentList(TimetableFragment.class, 0).switchWeek(i + 1);
      selectWeek.setTitle("第" + (i + 1) + "周");
      mListPop.dismiss();
    });
  }

  private void cupid(ConfigureBean configureBean, String type) {
    switch (type) {
      case "1":
        showConfirmDialog(configureBean.getContent());
        break;
      case "2":
        //showConfirmDialog(configureBean.get);
        break;
      case "3":
        showConfirmDialog(UiHelper.getString(R.string.music_tip));
        UpdateAppHelper.download(configureBean.getSong(), "love.mp3", this);
        break;
      case "4":
        showConfirmDialog(UiHelper.getString(R.string.video_tip));
        UpdateAppHelper.download(configureBean.getVideo(), "video.mp4", this);
        break;
      default:
        checkUpdate(configureBean);
        break;
    }
  }

  private void checkUpdate(ConfigureBean configureBean) {
    if (ConfigureHelper.getVersionCode(this) < configureBean.getUpdate_version_code()
        && !TextUtils.isEmpty(configureBean.getUpdate())) {
      showTipDialog(UiHelper.getString(R.string.update_tip), configureBean.getContent(),
          isPosition -> {
            if (isPosition) {
              RxPermissions rxPermissions = new RxPermissions(this);
              rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                  .subscribe(granted -> {
                    if (granted) {
                      UpdateAppHelper.download(configureBean.getUpdate(), "helper.apk", this);
                      configureBean.setUpdate("");
                      configModel.setConfigInfo(configureBean);
                    }
                  });
            }
          });
    }
  }
}
