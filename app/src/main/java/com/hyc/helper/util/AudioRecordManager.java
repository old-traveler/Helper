package com.hyc.helper.util;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import cn.bmob.newim.core.BmobRecordManager;
import com.hyc.helper.helper.LogHelper;
import java.io.File;
import java.io.IOException;

public class AudioRecordManager {

  //文件路径
  private String filePath;
  //文件夹路径
  private String FolderPath;
  private MediaRecorder mMediaRecorder;

  private OnAudioStatusUpdateListener audioStatusUpdateListener;

  public AudioRecordManager() {
    this(Environment.getExternalStorageDirectory() + "/record/");
  }

  private AudioRecordManager(String filePath) {
    File path = new File(filePath);
    if (!path.exists()) {
      if (!path.mkdirs()) {
        throw new RuntimeException("创建文件失败");
      }
    }
    this.FolderPath = filePath;
  }

  private long startTime;

  public void startRecord() {
    if (mMediaRecorder == null) {
      mMediaRecorder = new MediaRecorder();
    }
    try {
      mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
      mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
      mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      filePath = FolderPath + System.currentTimeMillis() + ".amr";
      mMediaRecorder.setOutputFile(filePath);
      mMediaRecorder.setMaxDuration(BmobRecordManager.MAX_RECORD_TIME * 1000);
      mMediaRecorder.prepare();
      mMediaRecorder.start();
      startTime = System.currentTimeMillis();
      updateMicStatus();
    } catch (IllegalStateException | IOException e) {
      LogHelper.log(e.getMessage());
    }
  }

  /**
   * 停止录音
   */
  public void stopRecord() {
    if (mMediaRecorder == null) {
      return;
    }
    long endTime = System.currentTimeMillis();
    audioStatusUpdateListener.onStop(filePath, (int) ((endTime - startTime) / 1000));
    try {
      mMediaRecorder.stop();
      mMediaRecorder.reset();
      mMediaRecorder.release();
      mMediaRecorder = null;
      filePath = "";
    } catch (RuntimeException e) {
      if (mMediaRecorder != null) {
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
      }
      File file = new File(filePath);
      if (file.exists() && file.delete()) {
        LogHelper.log("stop file delete   " + e.getMessage());
      }
      filePath = "";
    }
  }

  /**
   * 取消录音
   */
  public void cancelRecord() {
    if (mMediaRecorder == null) {
      return;
    }
    try {
      mMediaRecorder.stop();
      mMediaRecorder.reset();
      mMediaRecorder.release();
      mMediaRecorder = null;
    } catch (RuntimeException e) {
      mMediaRecorder.reset();
      mMediaRecorder.release();
      mMediaRecorder = null;
    }
    File file = new File(filePath);
    if (file.exists() && file.delete()) {
      LogHelper.log("file delete");
    }

    filePath = "";
  }

  private final Handler mHandler = new Handler();
  private Runnable mUpdateMicStatusTimer = this::updateMicStatus;

  public void setOnAudioStatusUpdateListener(
      OnAudioStatusUpdateListener audioStatusUpdateListener) {
    this.audioStatusUpdateListener = audioStatusUpdateListener;
  }

  /**
   * 更新麦克状态
   */
  private void updateMicStatus() {
    if (mMediaRecorder != null) {
      int BASE = 5;
      double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
      double db;// 分贝
      if (ratio > 1) {
        db = 2 * Math.log10(ratio);
        if (null != audioStatusUpdateListener) {
          audioStatusUpdateListener.onUpdate((int) db,
              (int) ((System.currentTimeMillis() - startTime) / 1000));
        }
      }
      // 间隔取样时间
      int SPACE = 100;
      mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
    }
  }

  public interface OnAudioStatusUpdateListener {
    /**
     * 录音中...
     *
     * @param db 当前声音分贝
     * @param time 录音时长
     */
    void onUpdate(int db, int time);

    /**
     * 停止录音
     *
     * @param filePath 保存路径
     */
    void onStop(String filePath, int time);
  }
}

