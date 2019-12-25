package com.hyc.helper.adapter.viewholder;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import cn.bmob.newim.bean.BmobIMMessage;
import com.hyc.helper.R;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.helper.Constant;
import com.hyc.helper.helper.ImageRequestHelper;
import com.hyc.helper.helper.VoicePlayer;
import org.json.JSONException;
import org.json.JSONObject;

public class VoiceRightViewHolder extends BaseMessageViewHolder implements View.OnClickListener,
    MediaPlayer.OnCompletionListener {


  private TextView tvDuration;

  private View playView;


  public VoiceRightViewHolder(@NonNull View itemView) {
    super(itemView);
  }

  @Override
  protected void initItemView(View view) {
    cvHeadView = view.findViewById(R.id.cv_voice_right);
    tvDuration = view.findViewById(R.id.tv_right_duration);
    playView = view.findViewById(R.id.v_playing);
  }

  @Override
  public void loadItemData(Context context, BmobIMMessage data, int position) {
    super.loadItemData(context,data,position);
    ImageRequestHelper.loadHeadImage(context,userModel.getCurUserInfo().getData().getHead_pic_thumb(),cvHeadView);
    int duration = 0;
    String info = data.getExtra();
    try {
      JSONObject jsonObject = new JSONObject(info);
      duration = jsonObject.getJSONObject(
          Constant.MSG_EXTRA).getInt("duration");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    this.setData(data);
    playView.setVisibility(View.GONE);
    tvDuration.setOnClickListener(this);
    tvDuration.setText(String.format(UiHelper.getString(R.string.voice_length_tip),duration));
  }


  @Override
  public void onClick(View view) {
    playView.setVisibility(View.VISIBLE);
    Animation operatingAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.playing_voice);
    LinearInterpolator lin = new LinearInterpolator();
    operatingAnim.setInterpolator(lin);
    playView.startAnimation(operatingAnim);
    VoicePlayer.getDefault().play(getData().getContent());
    VoicePlayer.getDefault().setOnCompletionListener(this);
  }

  @Override
  public void onCompletion(MediaPlayer mediaPlayer) {
    playView.clearAnimation();
    playView.setVisibility(View.GONE);
  }

}