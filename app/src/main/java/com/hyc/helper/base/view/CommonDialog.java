package com.hyc.helper.base.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.hyc.helper.R;
import com.hyc.helper.base.listener.OnDialogClickListener;
import com.hyc.helper.base.util.UiHelper;

public class CommonDialog extends Dialog implements View.OnClickListener {

  private OnDialogClickListener dialogClickListener;

  private CommonDialog(Builder builder, int themeResId) {
    super(builder.getContext(), themeResId);
    View view = UiHelper.inflater(builder.getContext()
        , R.layout.lib_dialog_common, null);
    setContentView(view);
    initView(builder);
  }

  private void initView(Builder builder) {
    String content = builder.getContent();
    String title = builder.getTitle();
    String positiveName = builder.getPositiveName();
    String negativeName = builder.getNegativeName();
    dialogClickListener = builder.getDialogClickListener();

    TextView tvContent = findViewById(R.id.content);
    TextView tvTitle = findViewById(R.id.title);
    TextView tvPositive = findViewById(R.id.submit);
    TextView tvNegative = findViewById(R.id.cancel);
    View cutView = findViewById(R.id.v_cut);
    tvContent.setText(content);

    if (!TextUtils.isEmpty(positiveName)) {
      tvPositive.setText(positiveName);
      tvPositive.setOnClickListener(this);
    } else {
      tvPositive.setVisibility(View.GONE);
      cutView.setVisibility(View.GONE);
    }
    if (!TextUtils.isEmpty(negativeName)) {
      tvNegative.setOnClickListener(this);
      tvNegative.setText(negativeName);
    } else {
      tvNegative.setVisibility(View.GONE);
      cutView.setVisibility(View.GONE);
    }
    if (!TextUtils.isEmpty(title)) {
      tvTitle.setText(title);
    } else {
      tvTitle.setVisibility(View.GONE);
    }
  }

  @Override
  public void onClick(View view) {
    if (dialogClickListener != null) {
      dialogClickListener.onDialogItemClick(view.getId() == R.id.submit);
    }
    dismiss();
  }

  public static class Builder {
    private Context mContext;
    private String content;
    private String positiveName;
    private String negativeName;
    private String title;
    private OnDialogClickListener dialogClickListener;

    public String getContent() {
      return content;
    }

    private String getPositiveName() {
      return positiveName;
    }

    private String getNegativeName() {
      return negativeName;
    }

    public String getTitle() {
      return title;
    }

    private OnDialogClickListener getDialogClickListener() {
      return dialogClickListener;
    }

    public Builder(Context mContext) {
      this.mContext = mContext;
    }

    public Builder setContent(String content) {
      this.content = content;
      return this;
    }

    public Builder setPositiveName(String positiveName) {
      this.positiveName = positiveName;
      return this;
    }

    public Builder setNegativeName(String negativeName) {
      this.negativeName = negativeName;
      return this;
    }

    public Builder setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder setDialogClickListener(OnDialogClickListener dialogClickListener) {
      this.dialogClickListener = dialogClickListener;
      return this;
    }

    public Context getContext() {
      return mContext;
    }

    public CommonDialog create() {
      return new CommonDialog(this, R.style.common_dialog);
    }

    public void createAndShow() {
      new CommonDialog(this, R.style.common_dialog).show();
    }
  }
}