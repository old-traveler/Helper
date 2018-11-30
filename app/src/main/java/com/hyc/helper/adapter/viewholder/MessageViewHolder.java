package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import com.hyc.helper.R;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.helper.DateHelper;
import com.hyc.helper.helper.ImageRequestHelper;
import java.util.List;

public class MessageViewHolder extends BaseViewHolder<BmobIMConversation> {

  @BindView(R.id.iv_portrait)
  ImageView ivPortrait;
  @BindView(R.id.tv_message_nickname)
  TextView tvMessageNickname;
  @BindView(R.id.tv_message_content)
  TextView tvMessageContent;
  @BindView(R.id.tv_date)
  TextView tvDate;
  @BindView(R.id.tv_unread_count)
  TextView tvUnreadCount;

  public MessageViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    ButterKnife.bind(this, view);
  }

  @Override
  public void loadItemData(Context context, BmobIMConversation data, int position) {
    ImageRequestHelper.loadHeadImage(context,data.getConversationIcon(),ivPortrait);
    tvMessageNickname.setText(data.getConversationTitle());
    List<BmobIMMessage> messages = data.getMessages();
    if (null != messages && messages.size() > 0) {
      BmobIMMessage lastMessage = messages.get(0);
      tvMessageContent.setText(lastMessage.getMsgType().equals("txt")
          ? lastMessage.getContent() : lastMessage.getMsgType());
    }else {
      tvMessageContent.setText(R.string.default_content);
    }
    String date = DateHelper.getDateInfo((messages != null
        && messages.size() > 0) ? messages.get(0).getUpdateTime() : data.getUpdateTime());
    tvDate.setText(date);
    long unReadCount = BmobIM.getInstance().getUnReadCount(data.getConversationId());
    if (unReadCount > 0) {
      tvUnreadCount.setText(String.valueOf(unReadCount));
      tvUnreadCount.setVisibility(View.VISIBLE);
    }else {
      tvUnreadCount.setVisibility(View.GONE);
    }
  }
}
