package com.hyc.helper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import cn.bmob.newim.core.BmobRecordManager;
import com.hyc.helper.R;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.base.util.UiHelper;
import com.hyc.helper.helper.LogHelper;
import com.hyc.helper.util.AudioRecordManager;
import com.hyc.helper.util.DensityUtil;

public class VoiceRecordView extends View implements AudioRecordManager.OnAudioStatusUpdateListener {

  private AudioRecordManager recordManager;
  private int[] volumes;
  private boolean isPress = false;
  private Paint paint;
  private int recordTime = 0;
  private Bitmap bitmap;
  private String id;
  private boolean isCanCancel = false;
  private int radius = DensityUtil.dip2px(55f);
  private OnSendAudioMessageListener listener;

  public void setListener(OnSendAudioMessageListener listener) {
    this.listener = listener;
  }

  @Override
  public void onUpdate(int volume, int time) {
    if (time >= BmobRecordManager.MAX_RECORD_TIME){
      recordManager.stopRecord();
    }
    for (int i = 0; i < volumes.length - 1; i++) {
      volumes[i] = volumes[i + 1];
    }
    recordTime = time;
    volumes[volumes.length - 1] = volume;
    invalidate();

  }

  @Override
  public void onStop(String filePath,int time) {
    sendAudioMessage(time,filePath);
    recordTime = 0;
  }

  public interface OnSendAudioMessageListener {
    void OnSendAudioMessage(int recordTime, String path);
  }

  public VoiceRecordView(Context context) {
    this(context, null);
  }

  public VoiceRecordView(Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    recordManager = new AudioRecordManager();
    recordManager.setOnAudioStatusUpdateListener(this);
    volumes = new int[10];
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTextSize(DensityUtil.dip2px(18));
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.voice_icon);
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    drawCircleButton(canvas);
    drawTopTip(canvas);
  }

  private void drawTopTip(Canvas canvas) {
    paint.setColor(UiHelper.getColor(R.color.colorPrimaryPress));
    int top = getHeight() / 4 - DensityUtil.dip2px(10f);
    if (isCanCancel){
      String title = "松手取消";
      paint.setColor(Color.parseColor("#fe3131"));
      float textWidth = paint.measureText(title);
      canvas.drawText(title, (getWidth() - textWidth) / 2, top, paint);
    }else if (isPress) {
      paint.setStrokeWidth(5f);
      String title =
          String.format(getContext().getString(R.string.voice_record_tip), recordTime / 60,
              recordTime % 60);
      float textWidth = paint.measureText(title);
      Rect rect = new Rect();
      paint.getTextBounds(title, 0, title.length(), rect);
      float textHeight = rect.height();
      canvas.drawText(title, (getWidth() - textWidth) / 2, top, paint);
      int start = getWidth() / 2 - DensityUtil.dip2px(60f);
      int height = (int) (top - textHeight / 2);
      int distance = DensityUtil.dip2px(4.5f);
      for (int volume : volumes) {
        volume++;
        canvas.drawLine(start, height - 2 * volume, start, height + 2 * volume, paint);
        start += distance;
      }
      start = getWidth() / 2 + DensityUtil.dip2px(60f);
      for (int volume : volumes) {
        volume++;
        canvas.drawLine(start, height - 2 * volume, start, height + 2 * volume, paint);
        start -= distance;
      }
    } else  {
      String title = "按住说话";
      float textWidth = paint.measureText(title);
      canvas.drawText(title, (getWidth() - textWidth) / 2, top, paint);
    }
  }

  private void drawCircleButton(Canvas canvas) {
    if (isPress) {
      paint.setColor(UiHelper.getColor(R.color.colorPrimaryPress));
    } else {
      paint.setColor(UiHelper.getColor(R.color.colorPrimary));
    }
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
    canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2,
        (getHeight() - bitmap.getHeight()) / 2, paint);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_UP:
        return onActionUp(event);
      case MotionEvent.ACTION_MOVE:
        return onActionMove(event);
      case MotionEvent.ACTION_DOWN:
        return onActionDown(event);
    }
    return super.onTouchEvent(event);
  }

  private boolean onActionUp(MotionEvent event) {
    if (isCenter(event)) {
      recordManager.stopRecord();
    } else {
      cancelSendMessage();
    }
    recordTime = 0;
    isPress = false;
    isCanCancel = false;
    invalidate();
    return true;
  }

  private void cancelSendMessage() {
    recordManager.cancelRecord();
  }


  private void sendAudioMessage(int recordTime,String filePath) {
    if (recordTime < BmobRecordManager.MIN_RECORD_TIME) {
      ToastHelper.toast("按键时间太短");
    } else if (listener != null) {
      listener.OnSendAudioMessage(recordTime, filePath);
    }
  }

  private boolean onActionDown(MotionEvent event) {
    if (isCenter(event)) {
      isPress = true;
      startRecord();
      invalidate();
      return true;
    }
    return false;
  }

  private void startRecord() {
    recordManager.startRecord();
    LogHelper.log("startRecording");
  }

  private boolean isCenter(MotionEvent event) {
    float distanceX = event.getX() - getWidth() / 2;
    float distanceY = event.getY() - getHeight() / 2;
    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    return distance <= radius;
  }

  private boolean onActionMove(MotionEvent event) {
    isCanCancel = !isCenter(event);
    invalidate();
    return true;
  }
}
