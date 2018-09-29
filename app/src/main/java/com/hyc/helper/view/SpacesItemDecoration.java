package com.hyc.helper.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
  private int space;

  public SpacesItemDecoration(int space) {
    this.space = space;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view,
      RecyclerView parent, RecyclerView.State state) {
    int index = parent.getChildAdapterPosition(view);
    if (index%2==0){
      outRect.left = space;
      outRect.right = space/2;
    }else {
      outRect.left = space/2;
      outRect.right = space;
    }
    outRect.bottom = space;
    if (index <2){
      outRect.top = space;
    }
  }
}
