package com.hyc.helper.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ChatSpacesItemDecoration extends RecyclerView.ItemDecoration {

  private int space;

  public ChatSpacesItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view,
      RecyclerView parent, RecyclerView.State state) {
    if (parent.getChildAdapterPosition(view) == 0){
      outRect.top = space/2;
    }
    outRect.bottom = space;
  }

}
