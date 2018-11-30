package com.hyc.helper.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class EmojiItemDecoration extends RecyclerView.ItemDecoration {
  private int space;
  private int spanCount;

  public EmojiItemDecoration(int space,int spanCount) {
    this.space = space;
    this.spanCount = spanCount;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view,
      RecyclerView parent, RecyclerView.State state) {
    outRect.top = space;
    outRect.left = space;
    int index = parent.getChildAdapterPosition(view);
    if ((index+1)%spanCount == 0){
      outRect.right = space;
    }
    if (parent.getAdapter() != null
        && parent.getAdapter().getItemCount()-index <= spanCount){
      outRect.bottom = space;
    }
  }
}
