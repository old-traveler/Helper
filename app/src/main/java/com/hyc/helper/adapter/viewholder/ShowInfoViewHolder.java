package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.hyc.helper.R;
import com.hyc.helper.activity.UserInfoActivity;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.InfoEntity;

public class ShowInfoViewHolder extends BaseViewHolder<InfoEntity> {

  @BindView(R.id.tv_name)
  TextView tvName;
  @BindView(R.id.tv_value)
  TextView tvValue;

  public ShowInfoViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this, view);
  }

  @Override
  public void loadItemData(Context context, InfoEntity data, int position) {
    tvName.setText(data.getTitle());
    tvValue.setText(data.getValue());
    UiHelper.initLinkTextView(tvValue, context);
    if (!TextUtils.isEmpty(data.getUserId())) {
      SpannableStringBuilder style = new SpannableStringBuilder(tvValue.getText());
      ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(@NonNull View view) {
          UserInfoActivity.goToUserInfoActivity(context, data.getUserId());
        }
      };
      style.setSpan(clickableSpan, 0, data.getValue().length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      tvValue.setMovementMethod(LinkMovementMethod.getInstance());
      tvValue.setText(style);
    }
  }
}
