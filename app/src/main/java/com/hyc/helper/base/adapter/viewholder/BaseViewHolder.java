package com.hyc.helper.base.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

  public int currentPosition;

  private T data;

  public BaseViewHolder(@NonNull View itemView) {
    super(itemView);
    initItemView(itemView);
  }

  protected abstract void initItemView(View view);

  public abstract void loadItemData(Context context, T data, int position);

  public void onViewRecycled() {

  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public void setOnClickListener(View.OnClickListener onClickListener) {
    itemView.setOnClickListener(onClickListener);
  }
}
