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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMMessage;
import com.hyc.cuckoo_lib.CuckooNeed;
import com.hyc.helper.R;
import com.hyc.helper.activity.fragment.LostFindFragment;
import com.hyc.helper.activity.fragment.SecondHandFragment;
import com.hyc.helper.activity.fragment.StatementFragment;
import com.hyc.helper.activity.fragment.TimetableFragment;
import com.hyc.helper.adapter.TechFragmentPageAdapter;
import com.hyc.helper.annotation.Subscribe;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.fragment.BaseFragment;
import com.hyc.helper.base.fragment.BaseListFragment;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.CalendarBean;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.ConfigureHelper;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.DateHelper;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.helper.LogHelper;
import com.hyc.helper.helper.RequestHelper;
import com.hyc.helper.helper.SpCacheHelper;
import com.hyc.helper.im.ConversationActivity;
import com.hyc.helper.model.ExamModel;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.helper.UpdateAppHelper;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.util.RxBus;
import com.hyc.helper.util.ThreadMode;
import com.hyc.helper.view.MarqueeTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Collections;
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
  @BindView(R.id.vf)
  ViewFlipper viewFlipper;
  private TechFragmentPageAdapter adapter;
  private ListPopupWindow weekListPopWindow;
  private MenuItem selectWeek;
  private ExamModel examModel = new ExamModel();
  private MenuItem messageTip;
  private UserModel userModel = new UserModel();
  private UpdateAppHelper updateAppHelper;

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
    initLeftView();
    initViewPager();
    initCalendar();
    checkUpdate();
  }

  private void initCalendar() {
    addDisposable(RequestHelper.getRequestApi().getCalendar()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this::loadCalendar, throwable -> {
          LogHelper.log(throwable.getMessage());
          loadCalendar(null);
        }));
  }

  private void loadCalendar(List<CalendarBean> calendarBeans) {
    addDisposable(examModel.getExamInfoFromCache()
        .subscribe(examBean -> {
          if (calendarBeans != null) {
            calendarBeans.addAll(examModel.toCalendar(examBean));
            loadAllCalendar(calendarBeans);
          } else {
            loadAllCalendar(examModel.toCalendar(examBean));
          }
        }, throwable -> {
          LogHelper.log(throwable.getMessage());
          loadAllCalendar(calendarBeans);
        }));
  }

  private void loadAllCalendar(List<CalendarBean> calendarBeans) {
    if (calendarBeans == null || calendarBeans.size() == 0) {
      viewFlipper.setVisibility(View.GONE);
      setToolBarTitle(R.string.app_name);
      return;
    }
    Collections.sort(calendarBeans);
    for (CalendarBean calendarBean : calendarBeans) {
      MarqueeTextView textView =
          (MarqueeTextView) LayoutInflater.from(this)
              .inflate(R.layout.text_notify, viewFlipper, false);
      if (calendarBean.getDays() < 0) {
        textView.setText(
            String.format(UiHelper.getString(R.string.calendar_tip_exam), calendarBean.getName(),
                calendarBean.getDate()));
      } else {
        textView.setText(
            String.format(UiHelper.getString(R.string.calendar_tip), calendarBean.getName(),
                calendarBean.getDays()));
      }
      textView.setMarqueeEnable(true);
      viewFlipper.addView(textView);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    refreshUserInfo();
  }

  private void refreshUserInfo() {
    UserBean userBean = userModel.getCurUserInfo();
    ImageView headImageView = navView.getHeaderView(0).findViewById(R.id.iv_main_head);
    ImageRequestHelper.loadBigHeadImage(this, userBean.getData().getHead_pic_thumb(),
        headImageView);
    TextView tvName = navView.getHeaderView(0).findViewById(R.id.tv_main_name);
    if (TextUtils.isEmpty(userBean.getData().getUsername())) {
      tvName.setText(userBean.getData().getTrueName());
    } else {
      tvName.setText(userBean.getData().getUsername());
    }
    TextView tvDesc = navView.getHeaderView(0).findViewById(R.id.tv_main_desc);
    tvDesc.setText(TextUtils.isEmpty(userBean.getData().getBio())
        ? UiHelper.getString(R.string.default_bio) : userBean.getData().getBio());
  }

  private void initLeftView() {
    UserBean userBean = userModel.getCurUserInfo();
    View.OnClickListener listener = view -> goToOtherActivity(PersonalActivity.class, false);
    ImageView headImageView = navView.getHeaderView(0).findViewById(R.id.iv_main_head);
    ImageRequestHelper.loadBigHeadImage(this, userBean.getData().getHead_pic_thumb(),
        headImageView);
    headImageView.setOnClickListener(listener);
    TextView tvName = navView.getHeaderView(0).findViewById(R.id.tv_main_name);
    if (TextUtils.isEmpty(userBean.getData().getUsername())) {
      tvName.setText(userBean.getData().getTrueName());
    } else {
      tvName.setText(userBean.getData().getUsername());
    }
    tvName.setOnClickListener(listener);
    TextView tvDesc = navView.getHeaderView(0).findViewById(R.id.tv_main_desc);
    tvDesc.setText(TextUtils.isEmpty(userBean.getData().getBio())
        ? UiHelper.getString(R.string.default_bio) : userBean.getData().getBio());
    tvDesc.setOnClickListener(listener);
    navView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
  }

  private void initViewPager() {
    List<BaseFragment> list = new ArrayList<>(tbMain.getTabCount());
    list.add(new TimetableFragment());
    list.add(StatementFragment.newInstance(null));
    list.add(SecondHandFragment.newInstance(null));
    list.add(LostFindFragment.newInstance(null));
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
    } else if (item.getItemId() == R.id.item_query_power) {
      goToOtherActivity(QueryPowerActivity.class, false);
      return true;
    } else if (item.getItemId() == R.id.item_examination) {
      goToOtherActivity(ExamActivity.class, false);
    } else if (item.getItemId() == R.id.item_achievement) {
      goToOtherActivity(GradeActivity.class, false);
    } else if (item.getItemId() == R.id.item_school_date) {
      goToOtherActivity(SchoolCalendarActivity.class, false);
    } else if (item.getItemId() == R.id.item_library) {
      WebActivity.startWebBrowsing(this, R.string.library_url, R.string.library_title);
    } else if (item.getItemId() == R.id.item_about) {
      showConfirmDialog(String.format(UiHelper.getString(R.string.about_us),
          ConfigureHelper.getVersionName()));
    } else if (item.getItemId() == R.id.item_logout) {
      showTipDialog(UiHelper.getString(R.string.tip), UiHelper.getString(R.string.logout_tip),
          isPosition -> {
            if (isPosition) {
              userModel.logout();
              goToOtherActivity(LoginActivity.class, true);
            }
          });
    } else if (item.getItemId() == R.id.item_message) {
      goToOtherActivity(ConversationActivity.class, false);
    }
    return false;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    selectWeek = menu.findItem(R.id.action_select);
    messageTip = menu.findItem(R.id.item_message);
    selectWeek.setTitle(UiHelper.getString(R.string.week_tip, DateHelper.getCurWeek()));
    loadMessageTip();
    initListPopView();
    return true;
  }

  private void loadMessageTip() {
    if (BmobIM.getInstance().getAllUnReadCount() > 0) {
      messageTip.setIcon(R.drawable.ic_message_new);
    } else {
      messageTip.setIcon(R.drawable.ic_message);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN, eventType = Constant.EventType.IM_MESSAGE)
  public void onEvent(MessageEvent<BmobIMMessage> event) {
    loadMessageTip();
  }

  private void initListPopView() {
    int curWeek = DateHelper.getCurWeek();
    List<String> list = new ArrayList<>();
    for (int i = 1; i <= 20; i++) {
      list.add(UiHelper.getString(R.string.week_tip, i));
      if (curWeek == i) {
        list.set(i - 1, "本周");
      }
    }
    weekListPopWindow = new ListPopupWindow(this);
    weekListPopWindow.setAdapter(new ArrayAdapter<>(this, R.layout.item_select_week, list));
    weekListPopWindow.setWidth(DensityUtil.dip2px(80f));
    weekListPopWindow.setHeight(DensityUtil.dip2px(150f));
    weekListPopWindow.setModal(true);
    weekListPopWindow.setOnItemClickListener((adapterView, view, i, l) -> {
      adapter.getFragmentList(TimetableFragment.class, 0).switchWeek(i + 1);
      selectWeek.setTitle(UiHelper.getString(R.string.week_tip, i + 1));
      weekListPopWindow.dismiss();
    });
  }

  private void checkUpdate() {
    if (System.currentTimeMillis() - SpCacheHelper.getLong("cancel") < Constant.ONE_DAY_TIME) {
      return;
    }
    addDisposable(RequestHelper.getRequestApi().getUpdateApkInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(updateApkBean -> {
          if (ConfigureHelper.getVersionCode() < updateApkBean.getVersion_code()) {
            showTipDialog("版本更新", "发现新版本，是否更新?", isPosition -> {
              if (isPosition) {
                downApk(updateApkBean.getApk_url());
              } else {
                SpCacheHelper.putLong("cancel", System.currentTimeMillis());
              }
            });
          }
        }, throwable -> {

        }));
  }

  @CuckooNeed(Manifest.permission.WRITE_EXTERNAL_STORAGE)
  public void downApk(String url) {
    updateAppHelper = new UpdateAppHelper();
    updateAppHelper.download(url, UiHelper.getString(R.string.apk_name), this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (updateAppHelper != null) {
      updateAppHelper.clear();
    }
  }
}
