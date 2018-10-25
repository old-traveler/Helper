package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hyc.helper.R;
import com.hyc.helper.activity.PictureBrowsingActivity;
import com.hyc.helper.activity.UserInfoActivity;
import com.hyc.helper.activity.WebActivity;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.CommentInfoBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.TextPositionBean;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.model.StatementModel;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.view.ImageLayout;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.sackcentury.shinebuttonlib.ShineButton;
import java.util.ArrayList;
import java.util.List;

public class StatementViewHolder extends BaseViewHolder<StatementBean.StatementInfoBean>
    implements ImageLayout.OnItemClickListener {

  @BindView(R.id.iv_publisher_head)
  ImageView ivPublisherHead;
  @BindView(R.id.tv_publish_name)
  TextView tvPublishName;
  @BindView(R.id.tv_user_desc)
  TextView tvUserDesc;
  @BindView(R.id.tv_publish_date)
  TextView tvPublishDate;
  @BindView(R.id.expandable_text)
  TextView tvContent;
  @BindView(R.id.ept_content)
  ExpandableTextView expandableTextView;
  @BindView(R.id.imageLayout)
  ImageLayout imageLayout;
  @BindView(R.id.tv_from)
  TextView tvFrom;
  @BindView(R.id.sb_like)
  ShineButton sbLike;
  @BindView(R.id.v_comment)
  View vComment;
  @BindView(R.id.tv_like_count)
  TextView tvLikeCount;
  @BindView(R.id.tv_comment_info)
  TextView tvCommentInfo;
  @BindView(R.id.tv_delete_statement)
  TextView tvDeleteStatement;

  private UserModel userModel;
  private StatementModel statementModel;

  public StatementViewHolder(@NonNull View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }

  @Override
  protected void initItemView(View view) {
    userModel = new UserModel();
    statementModel = new StatementModel();
  }

  @Override
  public void loadItemData(Context context, StatementBean.StatementInfoBean data, int position) {
    ImageRequestHelper.loadHeadImage(context, data.getHead_pic_thumb(), ivPublisherHead);
    tvPublishName.setText(data.getUsername());
    tvUserDesc.setText(TextUtils.isEmpty(data.getBio())
        ? UiHelper.getString(R.string.default_bio) : data.getBio());
    expandableTextView.setText(data.getContent());
    tvFrom.setText(data.getDep_name());
    tvLikeCount.setText(data.getLikes());
    tvPublishDate.setText(data.getCreated_on());
    if (data.getComments() != null && data.getComments().size() > 0) {
      initCommentClick(context, data.getComments());
      tvCommentInfo.setVisibility(View.VISIBLE);
    } else {
      tvCommentInfo.setVisibility(View.GONE);
    }
    imageLayout.setImageListUrl(data.getPics());
    sbLike.setChecked(data.isIs_like());
    if (data.getUser_id()
        .equals(String.valueOf(userModel.getCurUserInfo().getData().getUser_id()))) {
      tvDeleteStatement.setVisibility(View.VISIBLE);
    } else {
      tvDeleteStatement.setVisibility(View.GONE);
    }
    imageLayout.setOnItemClickListener(this);
    initContentWebLinkClick(tvContent, context);
  }

  private void initContentWebLinkClick(TextView textView, Context context) {
    SpannableStringBuilder style = getWebLinkStyle(textView.getText(), context);
    if (style != null) {
      textView.setText(style);
    }
  }

  private void initCommentClick(Context context, List<CommentInfoBean> commentInfoBeans) {
    StringBuilder stringBuilder = new StringBuilder();
    List<TextPositionBean<String>> textPositionBeans = new ArrayList<>();
    for (int i = 0; i < commentInfoBeans.size(); i++) {
      int start = stringBuilder.length();
      stringBuilder.append(String.format("%s：%s", commentInfoBeans.get(i).getUsername(),
          commentInfoBeans.get(i).getComment()));
      if (i != commentInfoBeans.size() - 1) {
        stringBuilder.append("\n");
      }
      int end =  start + commentInfoBeans.get(i).getUsername().length();
      textPositionBeans.add(new TextPositionBean<>(start,end,commentInfoBeans.get(i).getUser_id()));
    }
    tvCommentInfo.setText(stringBuilder.toString());
    SpannableStringBuilder style = getWebLinkStyle(tvCommentInfo.getText(),context);
    if (style == null){
      style = new SpannableStringBuilder(stringBuilder.toString());
    }
    for (TextPositionBean<String> textPositionBean : textPositionBeans) {
      ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(@NonNull View view) {
          UserInfoActivity.goToUserInfoActivity(context,textPositionBean.getData());
        }
      };
      style.setSpan(clickableSpan,textPositionBean.getStart(),textPositionBean.getEnd(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      tvCommentInfo.setMovementMethod(LinkMovementMethod.getInstance());
    }
    tvCommentInfo.setText(style);
  }

  private SpannableStringBuilder getWebLinkStyle(CharSequence text, Context context) {
    if (text instanceof Spannable) {
      int end = text.length();
      Spannable sp = (Spannable) text;
      URLSpan urls[] = sp.getSpans(0, end, URLSpan.class);
      SpannableStringBuilder style = new SpannableStringBuilder(text);
      style.clearSpans();
      for (URLSpan urlSpan : urls) {
        ClickableSpan myURLSpan = new ClickableSpan() {
          @Override
          public void onClick(@NonNull View view) {
            WebActivity.startWebBrowsing(context, urlSpan.getURL(), "");
          }
        };
        style.setSpan(myURLSpan, sp.getSpanStart(urlSpan),
            sp.getSpanEnd(urlSpan),
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
      }
      return style;
    }
    return null;
  }

  @OnClick({ R.id.iv_publisher_head, R.id.tv_publish_name, R.id.sb_like, R.id.tv_delete_statement })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_publisher_head:
      case R.id.tv_publish_name:
        UserInfoActivity.goToUserInfoActivity(itemView.getContext(), getData().getUser_id(),
            getData().getUsername(), getData().getBio(), getData().getHead_pic_thumb());
        break;
      case R.id.sb_like:
        int likeCount = Integer.parseInt(getData().getLikes());
        if (sbLike.isChecked()) {
          getData().setLikes(String.valueOf(++likeCount));
        } else {
          getData().setLikes(String.valueOf(--likeCount));
        }
        tvLikeCount.setText(String.valueOf(likeCount));
        getData().setIs_like(sbLike.isChecked());
        sbLike.setChecked(sbLike.isChecked());
        statementModel.likeStatement(userModel.getCurUserInfo(), getData().getId());
        break;
    }
  }

  @Override
  public void setOnClickListener(View.OnClickListener onClickListener) {
    super.setOnClickListener(onClickListener);
    vComment.setOnClickListener(onClickListener);
    tvDeleteStatement.setOnClickListener(onClickListener);
  }

  @Override
  public void onItemImageClick(int position) {
    if (itemView.getContext() != null) {
      PictureBrowsingActivity.goToPictureBrowsingActivity(imageLayout.getContext(), position,
          (ArrayList<String>) getData().getPics());
    }
  }
}
