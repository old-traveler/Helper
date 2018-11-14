package com.hyc.helper.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;

public class WebActivity extends BaseActivity {

  @BindView(R.id.progressbar)
  ProgressBar progressbar;
  @BindView(R.id.web_view)
  WebView webView;

  public String toolBarTitle;

  @Override
  public int getMenuId() {
    return R.menu.menu_web;
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_web;
  }

  public static void startWebBrowsing(Context context, String url, String title) {
    Bundle bundle = new Bundle();
    bundle.putString("url", url);
    bundle.putString("title", title);
    Intent intent = new Intent(context, WebActivity.class);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  public static void startWebBrowsing(Context context, int urlResId, int titleId) {
    startWebBrowsing(context, UiHelper.getString(urlResId), UiHelper.getString(titleId));
  }

  @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
  @Override
  public void initViewWithIntentData(Bundle bundle) {
    ButterKnife.bind(this);
    setToolBarTitle(bundle.getString("title"));
    webView.loadUrl(bundle.getString("url"));
    webView.addJavascriptInterface(this, "android");
    webView.setWebChromeClient(webChromeClient);
    webView.setWebViewClient(webViewClient);
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);//允许使用js
    webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.
    //支持屏幕缩放
    webSettings.setSupportZoom(true);
    webSettings.setBuiltInZoomControls(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.item_refresh) {
      if (webView != null) {
        webView.reload();
      }
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private WebViewClient webViewClient = new WebViewClient() {
    @Override
    public void onPageFinished(WebView view, String url) {//页面加载完成
      progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
      progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
      super.onReceivedError(view, request, error);
      if (error != null) {
        ToastHelper.toast("加载失败");
      }
    }
  };

  //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
  private WebChromeClient webChromeClient = new WebChromeClient() {
    //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
    @Override
    public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
      localBuilder.setMessage(message).setPositiveButton("确定", null);
      localBuilder.setCancelable(false);
      localBuilder.create().show();

      //注意:
      //必须要这一句代码:result.confirm()表示:
      //处理结果为确定状态同时唤醒WebCore线程
      //否则不能继续点击按钮
      result.confirm();
      return true;
    }

    //获取网页标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
      super.onReceivedTitle(view, title);
      if (toolBarTitle == null || !toolBarTitle.equals(title)){
        setToolBarTitle(title);
      }

    }

    //加载进度回调
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
      progressbar.setProgress(newProgress);
    }
  };

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {//点击返回按钮的时候判断有没有上一页
      webView.goBack(); // goBack()表示返回webView的上一页面
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }
  //
  ///**
  // * JS调用android的方法
  // */
  //@JavascriptInterface //仍然必不可少
  //public void getClient(String str) {
  //
  //}

  @Override
  protected void onDestroy() {
    super.onDestroy();
    //释放资源
    webView.destroy();
    webView = null;
  }
}
