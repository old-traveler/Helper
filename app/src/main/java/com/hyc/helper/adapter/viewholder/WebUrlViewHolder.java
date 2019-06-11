package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.bean.WebUrlBean;

/**
 * 作者: 贺宇成
 * 时间: 2019-06-11
 * 描述:
 */
public class WebUrlViewHolder extends BaseViewHolder<WebUrlBean> {
  private TextView tvTitle;
  private TextView tvUrl;

  public WebUrlViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    tvTitle = view.findViewById(R.id.tv_title);
    tvUrl = view.findViewById(R.id.tv_url);
  }

  @Override
  public void loadItemData(Context context, WebUrlBean data, int position) {
    tvTitle.setText(data.getTitle());
    tvUrl.setText(data.getUrl());
  }
}
