package com.hyc.helper.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class ChatLinearLayoutManager extends LinearLayoutManager {

  private boolean isScrollEnabled = true;

  public ChatLinearLayoutManager(Context context) {
    super(context);
  }

  public void setScrollEnabled(boolean flag) {
    this.isScrollEnabled = flag;
  }

  @Override
  public boolean canScrollVertically() {
    return isScrollEnabled && super.canScrollVertically();
  }
}
