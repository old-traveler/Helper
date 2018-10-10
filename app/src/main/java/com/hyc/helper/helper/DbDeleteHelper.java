package com.hyc.helper.helper;

import io.reactivex.Observable;

public class DbDeleteHelper {

  public static Observable<Boolean> deleteUserCourseInfo() {
    return Observable.create(emitter -> {
      DaoHelper.getDefault()
          .getDaoSession()
          .getCourseInfoBeanDao()
          .deleteAll();
      emitter.onNext(true);
      emitter.onComplete();
    });
  }
}
