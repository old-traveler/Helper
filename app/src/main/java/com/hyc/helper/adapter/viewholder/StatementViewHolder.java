package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.CommentInfoBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.model.StatementModel;
import com.hyc.helper.model.UserModel;
import com.hyc.helper.view.ImageLayout;
import com.sackcentury.shinebuttonlib.ShineButton;
import io.reactivex.functions.Consumer;

public class StatementViewHolder extends BaseViewHolder<StatementBean.StatementInfoBean> {

  @BindView(R.id.iv_publisher_head)
  ImageView ivPublisherHead;
  @BindView(R.id.tv_publish_name)
  TextView tvPublishName;
  @BindView(R.id.tv_user_desc)
  TextView tvUserDesc;
  @BindView(R.id.tv_publish_date)
  TextView tvPublishDate;
  @BindView(R.id.tv_content)
  TextView tvContent;
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

  private UserModel userModel;
  private StatementModel statementModel;

  public StatementViewHolder(@NonNull View itemView) {
    super(itemView);
    ButterKnife.bind(this,itemView);
  }

  @Override
  protected void initItemView(View view) {
    userModel = new UserModel();
    statementModel = new StatementModel();
  }

  @Override
  public void loadItemData(Context context, StatementBean.StatementInfoBean data, int position) {
    ImageRequestHelper.loadHeadImage(context,data.getHead_pic_thumb(),ivPublisherHead);
    tvPublishName.setText(data.getUsername());
    tvUserDesc.setText(TextUtils.isEmpty(data.getBio())
        ? UiHelper.getString(R.string.default_bio) : data.getBio());
    tvContent.setText(data.getContent());
    tvFrom.setText(data.getDep_name());
    tvLikeCount.setText(data.getLikes());
    tvPublishDate.setText(data.getCreated_on());
    if (data.getComments()!=null&&data.getComments().size()>0){
      StringBuilder stringBuilder = new StringBuilder();
      for (CommentInfoBean commentInfoBean : data.getComments()) {
        stringBuilder.append(commentInfoBean.getUsername());
        stringBuilder.append(": ");
        stringBuilder.append(commentInfoBean.getComment());
        stringBuilder.append("\n");
      }
      if (stringBuilder.length()>0){
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
      }
      tvCommentInfo.setText(stringBuilder.toString());
      tvCommentInfo.setVisibility(View.VISIBLE);
    }else {
      tvCommentInfo.setVisibility(View.GONE);
    }
    imageLayout.setImageListUrl(data.getPics());
    sbLike.setChecked(data.isIs_like());
  }

  @OnClick({ R.id.iv_publisher_head, R.id.tv_publish_name, R.id.sb_like })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.iv_publisher_head:
        break;
      case R.id.tv_publish_name:
        break;
      case R.id.sb_like:
        int likeCount = Integer.parseInt(getData().getLikes());
        if (sbLike.isChecked()){
          getData().setLikes(String.valueOf(++likeCount));
        }else {
          getData().setLikes(String.valueOf(--likeCount));
        }
        tvLikeCount.setText(String.valueOf(likeCount));
        getData().setIs_like(sbLike.isChecked());
        sbLike.setChecked(sbLike.isChecked());
        statementModel.likeStatement(userModel.getCurUserInfo(), getData().getId());
        break;
    }
  }
}
