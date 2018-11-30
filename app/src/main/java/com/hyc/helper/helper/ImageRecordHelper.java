package com.hyc.helper.helper;

import android.annotation.SuppressLint;
import com.hyc.helper.bean.ImageMessageRecord;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ImageRecordHelper {

  public static void saveCompressRecord(String path,String compressPath){
    ImageMessageRecord record = new ImageMessageRecord();
    record.setOriginalPath(path);
    record.setCompressPath(compressPath);
    DbInsertHelper.insertImageRecord(record);
  }

  public static Disposable saveCloudRecord(String compressPath,String couldPath){
    return DbSearchHelper.getOriginalPath(compressPath)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(record -> {
          record.setCloudPath(couldPath);
          try {
            DbUpdateHelper.updateImageRecord(record);
          }catch (Exception e){
            LogHelper.log(e.getMessage());
          }

        });
  }

}
