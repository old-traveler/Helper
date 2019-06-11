package com.hyc.helper.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.WebUrlViewHolder;
import com.hyc.helper.base.activity.BaseActivity;
import com.hyc.helper.base.adapter.BaseRecycleAdapter;
import com.hyc.helper.bean.WebUrlBean;
import com.hyc.helper.helper.DbSearchHelper;
import com.hyc.helper.util.DensityUtil;
import com.hyc.helper.view.ItemDecoration;

public class WebUrlCollectActivity extends BaseActivity {
  private BaseRecycleAdapter<WebUrlBean, WebUrlViewHolder> adapter;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_web_url_collect;
  }

  @Override
  public void initViewWithIntentData(Bundle bundle) {
    setToolBarTitle("Web Collect");
    RecyclerView recyclerView = findViewById(R.id.rv_url);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new BaseRecycleAdapter<>(R.layout.item_url, WebUrlViewHolder.class);
    recyclerView.setAdapter(adapter);
    recyclerView.addItemDecoration(new ItemDecoration(DensityUtil.dip2px(2)));
    adapter.setOnItemClickListener(
        (itemData, view, position) -> WebActivity.startWebBrowsing(WebUrlCollectActivity.this,
            itemData.getUrl(),
            itemData.getTitle()));
    showLoadingView();
    addDisposable(DbSearchHelper.searchAllCollectUrl().subscribe(webUrlBeans -> {
      adapter.setDataList(webUrlBeans);
      closeLoadingView();
    }));
  }
}
