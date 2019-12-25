package com.hyc.helper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.bmob.newim.bean.BmobIMMessage;
import com.hyc.helper.R;
import com.hyc.helper.adapter.viewholder.BaseMessageViewHolder;
import com.hyc.helper.adapter.viewholder.ImageLeftViewHolder;
import com.hyc.helper.adapter.viewholder.ImageRightViewHolder;
import com.hyc.helper.adapter.viewholder.TxtLeftViewHolder;
import com.hyc.helper.adapter.viewholder.TxtRightViewHolder;
import com.hyc.helper.adapter.viewholder.VoiceLeftViewHolder;
import com.hyc.helper.adapter.viewholder.VoiceRightViewHolder;
import com.hyc.helper.base.adapter.viewholder.BaseViewHolder;
import com.hyc.helper.helper.Constant;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter{


  private List<BmobIMMessage> messages;

  private Context context;

  private String userId;

  private OnChatMessageClickListener onChatMessageClick;

  public void setOnChatMessageClickListener(
      OnChatMessageClickListener onChatMessageClick) {
    this.onChatMessageClick = onChatMessageClick;
  }

  public interface OnChatMessageClickListener{
    void onChatMessageClick(View v,int position,BmobIMMessage message);
  }

  public ChatAdapter(String userId,List<BmobIMMessage> messages){
    this.messages = messages;
    this.userId = userId;
  }

  @Override
  public int getItemViewType(int position) {
    String id = messages.get(position).getFromId();
    switch (messages.get(position).getMsgType()){
      case Constant.MSG_TXT: return !id.equals(userId)?1:2;
      case Constant.MSG_IMAGE: return !id.equals(userId)?3:4;
      case Constant.MSG_VOICE: return !id.equals(userId)?5:6;
    }
    return 1;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    if (context == null){
      context = viewGroup.getContext();
    }
    switch (i){
      case 1:return new TxtLeftViewHolder(getItemView(viewGroup,R.layout.item_left_txt));
      case 2:return new TxtRightViewHolder(getItemView(viewGroup, R.layout.item_right_txt));
      case 3:return new ImageLeftViewHolder(getItemView(viewGroup,R.layout.item_left_image));
      case 4:return new ImageRightViewHolder(getItemView(viewGroup,R.layout.item_right_image));
      case 5:return new VoiceLeftViewHolder(getItemView(viewGroup,R.layout.item_left_voice));
      case 6:return new VoiceRightViewHolder(getItemView(viewGroup,R.layout.item_right_voice));
    }
    return new TxtLeftViewHolder(getItemView(viewGroup,R.layout.item_left_txt));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
    baseViewHolder.loadItemData(context,messages.get(i),i);
    if (viewHolder instanceof BaseMessageViewHolder){
      BaseMessageViewHolder messageViewHolder = (BaseMessageViewHolder) viewHolder;
      messageViewHolder.setOnClickListener(view -> onItemClick(view,i));
    }
  }

  private void onItemClick(View view,int position) {
    if (onChatMessageClick != null){
      onChatMessageClick.onChatMessageClick(view,position,messages.get(position));
    }
  }

  @Override
  public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
    super.onViewRecycled(holder);
    BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
    baseViewHolder.onViewRecycled();
  }

  @Override
  public int getItemCount() {
    return messages == null?0:messages.size();
  }


  private View getItemView(ViewGroup viewGroup,int resId){
    return LayoutInflater.from(viewGroup.getContext()).inflate(resId,viewGroup,false);
  }

  public void setMessages(List<BmobIMMessage> messages){
    this.messages = messages;
    notifyDataSetChanged();
  }

  public void insertMessages(List<BmobIMMessage> messageRecodes){
    int preSize = messageRecodes.size();
    if (preSize > 0){
      messageRecodes.addAll(messages);
      messages = messageRecodes;
      for (int i = 0; i < preSize; i++) {
        notifyItemInserted(i);
      }
      for (int i = preSize; i < getItemCount(); i++) {
        notifyItemChanged(i);
      }
    }
  }

  public void sendNewMessage(BmobIMMessage message){
    if (messages == null){
      messages = new ArrayList<>();
    }
    int position = getItemCount();
    messages.add(message);
    notifyItemInserted(position);
  }

  public List<BmobIMMessage> getData(){
    return messages;
  }


  public BmobIMMessage getLastMessage(){
    int itemCount = getItemCount();
    if (itemCount > 0){
      return messages.get(itemCount - 1);
    }
    return null;
  }

  public void updateMessage(BmobIMMessage message){
    notifyItemChanged(messages.indexOf(message));
  }

  public BmobIMMessage getFirstMessage(){
    if (getItemCount()>0){
      return messages.get(0);
    }
    return null;
  }


}
