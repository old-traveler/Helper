package com.hyc.helper.helper;

import com.hyc.helper.bean.ImageMessageRecord;

public class DbUpdateHelper {

  public static void updateImageRecord(ImageMessageRecord record) {
    DaoHelper.getDefault().getDaoSession().getImageMessageRecordDao().update(record);
  }
}
