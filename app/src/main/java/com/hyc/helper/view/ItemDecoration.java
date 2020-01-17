package com.hyc.helper.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者: 贺宇成
 * 时间: 2019-06-11
 * 描述:
 */
public class ItemDecoration extends RecyclerView.ItemDecoration {
  private int space;

  public ItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view,
      RecyclerView parent, RecyclerView.State state) {
    int index = parent.getChildAdapterPosition(view);
    if (index != 0) {
      outRect.top = space;
    }
  }
}
