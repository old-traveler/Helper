package com.hyc.helper.helper;

import android.media.MediaPlayer;
import android.os.Environment;
import com.hyc.helper.base.util.ToastHelper;
import com.hyc.helper.bean.BigImageLoadRecordBean;
import com.hyc.helper.model.ImageModel;
import java.io.File;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class VoicePlayer implements MediaPlayer.OnCompletionListener {

  private boolean isPrepare = false;

  private OkHttpClient okHttpClient;

  private Call call;

  private ImageModel imageModel;

  private static VoicePlayer voicePlayer;

  private MediaPlayer mediaPlayer;

  private MediaPlayer.OnCompletionListener onCompletionListener;

  private VoicePlayer() {
    mediaPlayer = new MediaPlayer();
    okHttpClient = new OkHttpClient();
    imageModel = new ImageModel();
  }

  public static VoicePlayer getDefault() {
    if (voicePlayer == null) {
      synchronized (VoicePlayer.class) {
        if (voicePlayer == null) {
          voicePlayer = new VoicePlayer();
        }
      }
    }
    return voicePlayer;
  }

  public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
    if (onCompletionListener != null && onCompletionListener != listener){
      onCompletionListener.onCompletion(mediaPlayer);
    }
    if (mediaPlayer != null) {
      mediaPlayer.setOnCompletionListener(listener);
      this.onCompletionListener = listener;
    }
  }

  public void play(String url) {
    if (isPrepare) {
      return;
    }
    if (call != null && !call.isCanceled()) {
      call.cancel();
    }
    if (url.contains("&")) {
      String[] urls = url.split("&");
      if (FileHelper.fileIsExist(urls[0])) {
        play(new File(urls[0]), null);
        return;
      }
      url = urls[1];
    }
    final String finalUrl = url;
    imageModel.getBigImageLoadRecord(finalUrl, bean -> play(new File(bean.getFilePath()), finalUrl), throwable -> downTheVoice(finalUrl));
  }

  public boolean isPlaying() {
    return mediaPlayer != null && mediaPlayer.isPlaying();
  }

  public void stopPlay() {
    if (isPlaying()) {
      mediaPlayer.stop();
      mediaPlayer.release();
    }
    if (onCompletionListener != null){
      onCompletionListener.onCompletion(null);
    }
  }

  private void play(File file, String url) {
    if (file.exists()) {
      if (mediaPlayer == null) {
        mediaPlayer = new MediaPlayer();
      }
      if (mediaPlayer.isPlaying()) {
        mediaPlayer.stop();
      }
      try {
        mediaPlayer.reset();
        mediaPlayer.setDataSource(file.getPath());
        isPrepare = true;
        mediaPlayer.prepare();
      } catch (IOException e) {
        e.printStackTrace();
      }
      mediaPlayer.start();
      isPrepare = false;
    } else {
      downTheVoice(url);
    }
  }

  private void downTheVoice(final String url) {
    Request request = new Request.Builder().url(url).build();
    call = okHttpClient.newCall(request);
    call.enqueue(new okhttp3.Callback() {
      @Override
      public void onFailure(okhttp3.Call call, IOException e) {
        call.cancel();
        LogHelper.log(e.getMessage());
      }

      @Override
      public void onResponse( okhttp3.Call call
          , okhttp3.Response response) throws IOException {
        deal(response, url);
      }
    });
  }

  private void deal(okhttp3.Response response, String url) throws IOException {
    BufferedSink bufferedSink = null;
    try {
      File dest = new File(Environment.getExternalStorageDirectory() + "/record/"
          , "voice_" + System.currentTimeMillis() + ".amr");
      Sink sink = Okio.sink(dest);
      bufferedSink = Okio.buffer(sink);
      bufferedSink.writeAll(response.body().source());
      bufferedSink.close();
      imageModel.saveBigImageLoadRecord(new BigImageLoadRecordBean(url, dest.getAbsolutePath()));
      play(dest, url);
    } catch (Exception e) {
      e.printStackTrace();
      LogHelper.log(e.getMessage());
    } finally {
      if (bufferedSink != null) {
        bufferedSink.close();
      }
    }
  }

  @Override
  public void onCompletion(MediaPlayer mediaPlayer) {

  }
}
