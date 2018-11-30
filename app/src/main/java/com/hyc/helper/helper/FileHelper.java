package com.hyc.helper.helper;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import com.hyc.helper.HelperApplication;
import com.hyc.helper.bean.ImageSizeBean;
import com.hyc.helper.bean.ImageUploadBean;
import com.hyc.helper.bean.LocalImageBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.util.Sha1Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileHelper {

  public static void getAllPictures(
      ObservableEmitter<LocalImageBean> emitter) {
    List<LocalImageBean> allPictureInfo = new ArrayList<>();
    LocalImageBean pictureInfo;
    ContentResolver cr = HelperApplication.getContext().getContentResolver();
    Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        new String[] {
            MediaStore.Audio.Media._ID,
            MediaStore.Images.Media.DATA
        },
        null,
        null,
        null);

    if (cursor != null && cursor.moveToFirst()) {
      do {
        pictureInfo = new LocalImageBean();
        pictureInfo.setImageId(cursor.getInt(0));
        pictureInfo.setImageRealPath(cursor.getString(1));
        allPictureInfo.add(pictureInfo);
      } while (cursor.moveToNext());
      cursor.close();
    }

    for (int i = allPictureInfo.size() - 1; i >= 0; i--) {
      pictureInfo = allPictureInfo.get(i);
      int media_id = pictureInfo.getImageId();
      cursor = cr.query(
          Thumbnails.EXTERNAL_CONTENT_URI,
          new String[] {
              Thumbnails.IMAGE_ID,
              Thumbnails.DATA,
          },
          Thumbnails.IMAGE_ID + "=" + media_id,
          null,
          null);
      if (cursor != null && cursor.moveToFirst()) {
        do {
          pictureInfo.setThumbnailPath(cursor.getString(1));
          if (pictureInfo != null){
            emitter.onNext(pictureInfo);
          }
        } while (cursor.moveToNext());
        cursor.close();
      } else {
        if (pictureInfo != null){
          emitter.onNext(pictureInfo);
        }
      }
    }
    emitter.onComplete();
    allPictureInfo.clear();
  }

  public static Observable<LocalImageBean> getLocalImages() {
    return Observable.create(FileHelper::getAllPictures);
  }

  public static ImageSizeBean getImageSize(String path) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    return new ImageSizeBean(options.outWidth, options.outHeight);
  }

  public static boolean isGifImage(String path) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    String type = options.outMimeType;
    return !TextUtils.isEmpty(type)&&type.equals("image/gif");
  }

  @SuppressLint("CheckResult")
  public static void uploadImage(UserBean userBean,String type, List<File> files,
      io.reactivex.Observer<ImageUploadBean> observer) {
    String env = Sha1Utils.getEnv(userBean);
    for (File file : files) {
      RequestBody requestFile =
          RequestBody.create(MediaType.parse("multipart/form-data"), file);
      MultipartBody.Part body =
          MultipartBody.Part.createFormData("file", file.getName(), requestFile);
      RequestHelper.getRequestApi()
          .uploadImage(userBean.getData().getStudentKH(), userBean.getRemember_code_app(), env,type,body)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(observer);
    }
  }

  public static String getFilePath(Context context, Uri uri) {
    if (null == uri) return null;

    final String scheme = uri.getScheme();
    String data = null;

    if (scheme == null) {
      data = uri.getPath();
    } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
      data = uri.getPath();
    } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
      Cursor cursor = context.getContentResolver()
          .query(uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
      if (null != cursor) {
        if (cursor.moveToFirst()) {
          int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
          if (index > -1) {
            data = cursor.getString(index);
          }
        }
        cursor.close();
      }
    }
    return data;
  }

  public static boolean fileIsExist(String path){
    return new File(path).exists();
  }

}
