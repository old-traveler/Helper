package com.hyc.helper.base.listener;

import android.view.View;

public interface OnItemClickListener<D> {

  void onItemClick(D itemData, View view,int position);

}
