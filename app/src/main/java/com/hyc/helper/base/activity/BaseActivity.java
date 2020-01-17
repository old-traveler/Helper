package com.hyc.helper.base.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.hyc.helper.R;
import com.hyc.helper.base.interfaces.IBaseActivity;
import com.hyc.helper.base.listener.OnDialogClickListener;
import com.hyc.helper.base.view.CommonDialog;
import com.hyc.helper.base.view.LoadingDialog;
import com.hyc.helper.helper.DisposableManager;
import com.hyc.helper.util.parrot.Parrot;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity
    implements View.OnClickListener, IBaseActivity, OnDialogClickListener {

  private LoadingDialog loadingDialog;

  private DisposableManager disposableManager;

  public Toolbar mToolbar;

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentViewId());
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      Parrot.initParam(bundle, this);
    }
    initViewWithIntentData(bundle);
  }

  protected abstract int getContentViewId();

  @Override
  protected void onDestroy() {
    super.onDestroy();
    cancelAllDisposable();
  }

  protected void cancelAllDisposable() {
    if (disposableManager != null) {
      disposableManager.cancelAllDisposable();
    }
  }

  public void addDisposable(Disposable disposable) {
    if (disposableManager == null) {
      disposableManager = new DisposableManager();
    }
    disposableManager.addDisposable(disposable);
  }

  @Override
  public void onClick(View view) {
    if (view.getId() == android.R.id.home) {
      super.onBackPressed();
    }
  }

  @Override
  public void goToOtherActivity(Class<?> cls, boolean isFinish) {
    goToOtherActivity(cls, null, isFinish);
  }

  @Override
  public void goToOtherActivity(Class<?> cls, Bundle bundle, boolean isFinish) {
    Intent intent = new Intent(this, cls);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    startActivity(intent);
    if (isFinish) finish();
  }

  @Override
  public void goToOtherActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
    Intent intent = new Intent(this, cls);
    if (null != bundle) {
      intent.putExtras(bundle);
    }
    startActivityForResult(intent, requestCode);
  }

  public void goToOtherActivityForResult(Class<?> cls, int requestCode) {
    goToOtherActivityForResult(cls, null, requestCode);
  }

  @Override
  public void backForResult(Class<?> cls, Bundle bundle, int resultCode) {
    Intent intent = new Intent(this, cls);
    if (null != bundle) {
      intent.putExtras(bundle);
    }
    setResult(resultCode, intent);
    finish();
  }

  public void backForResult(Class<?> cls, int resultCode) {
    backForResult(cls, null, resultCode);
  }

  public void setToolBar() {
    this.setToolBar(R.id.toolbar);
  }

  public void setToolBar(int toolBarId) {
    Toolbar toolbar = findViewById(toolBarId);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      this.mToolbar = toolbar;
    }
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  public void setNoBackToolBar() {
    this.setNoBackToolBar(R.id.toolbar);
  }

  public void setNoBackToolBar(int toolBarId) {
    Toolbar toolbar = findViewById(toolBarId);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      this.mToolbar = toolbar;
    }
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(false);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  public void setHomeResId(int resId) {
    ActionBar actionBar = getSupportActionBar();
    if (mToolbar != null && actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeAsUpIndicator(resId);
    }
  }

  public void setToolBarTitle(String title) {
    if (mToolbar == null) {
      setToolBar();
    }
    mToolbar.setTitle(title);
  }

  public void setToolBar(int toolBarId, String title, int homeResId) {
    Toolbar toolbar = findViewById(toolBarId);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      toolbar.setTitle(title);
      this.mToolbar = toolbar;
    }
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
      actionBar.setHomeAsUpIndicator(homeResId);
    }
    mToolbar.setTitle(title);
  }

  public void setToolBarTitle(int titleId) {
    if (mToolbar == null) {
      setToolBar();
    }
    mToolbar.setTitle(titleId);
  }

  @Override
  public void showLoadingView() {
    if (null != loadingDialog
        && loadingDialog.isShowing()) {
      return;
    } else if (null == loadingDialog) {
      LoadingDialog.Builder loadBuilder
          = new LoadingDialog.Builder(this)
          .setCancelable(true)
          .setCancelOutside(true);
      loadingDialog = loadBuilder.create();
    }
    loadingDialog.show();
  }

  @Override
  public void closeLoadingView() {
    if (loadingDialog != null && loadingDialog.isShowing()) {
      loadingDialog.dismiss();
    }
  }

  @Override
  public void hideInputWindow() {
    View focus = getCurrentFocus();
    if (focus != null) {
      IBinder focusBinder = focus.getWindowToken();
      InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      if (focusBinder != null && manager != null) {
        manager.hideSoftInputFromWindow(focusBinder, InputMethodManager.HIDE_NOT_ALWAYS);
      }
    }
  }

  @Override
  public void showInputWindow(EditText mEditText) {
    mEditText.requestFocus();
    InputMethodManager manager = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
    if (manager != null) {
      manager.showSoftInput(mEditText, 0);
    }
  }

  public void showTipDialog(String content) {
    showTipDialog(getString(R.string.tip), content);
  }

  public void showTipDialog(String title, String content) {
    new CommonDialog.Builder(this)
        .setTitle(title)
        .setContent(content)
        .setPositiveName(getString(R.string.confirm))
        .setNegativeName(getString(R.string.cancel))
        .setDialogClickListener(this)
        .createAndShow();
  }

  public void showTipDialog(String title, String content,
      OnDialogClickListener onDialogClickListener) {
    new CommonDialog.Builder(this)
        .setTitle(title)
        .setContent(content)
        .setPositiveName(getString(R.string.confirm))
        .setNegativeName(getString(R.string.cancel))
        .setDialogClickListener(onDialogClickListener)
        .createAndShow();
  }

  @Override
  public void showConfirmDialog(String content) {
    new CommonDialog.Builder(this)
        .setTitle(getString(R.string.tip))
        .setContent(content)
        .setPositiveName(getString(R.string.confirm))
        .setDialogClickListener(this)
        .createAndShow();
  }

  public void showCancelDialog(String content) {
    new CommonDialog.Builder(this)
        .setTitle(getString(R.string.tip))
        .setContent(content)
        .setNegativeName(getString(R.string.cancel))
        .setDialogClickListener(this)
        .createAndShow();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      super.onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onDialogItemClick(boolean isPosition) {

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (getMenuId() != -1) {
      getMenuInflater().inflate(getMenuId(), menu);
      return true;
    }
    return super.onCreateOptionsMenu(menu);
  }

  public int getMenuId() {
    return -1;
  }

  public void setStatusBarColor(int statusColor) {
    Window window = getWindow();
    window.setStatusBarColor(getResources().getColor(statusColor));
  }

  public boolean isSoftShowing() {
    int screenHeight = getWindow().getDecorView().getHeight();
    Rect rect = new Rect();
    getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
    return screenHeight - rect.bottom != 0;
  }

  public int getStatusHeight() {
    int height = 0;
    int resourceId = getApplicationContext().getResources()
        .getIdentifier("status_bar_height", "dimen", "android");
    if (resourceId > 0) {
      height = getApplicationContext().getResources().getDimensionPixelSize(resourceId);
    }
    return height;
  }
}
