package com.hyc.helper.helper;

import com.hyc.helper.bean.BigImageLoadRecordBean;
import com.hyc.helper.bean.CourseInfoBean;
import com.hyc.helper.bean.ExamInfoBean;
import com.hyc.helper.bean.GradeInfoBean;
import com.hyc.helper.bean.ImageMessageRecord;
import com.hyc.helper.bean.StatementInfoBean;
import com.hyc.helper.bean.WebUrlBean;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.List;

public class DbInsertHelper {

  public static Observable<Boolean> insertCourseInfo(List<CourseInfoBean> courseInfoBeans) {
    return Observable.create(emitter -> {
      DaoHelper.getDefault()
          .getDaoSession()
          .getCourseInfoBeanDao()
          .insertInTx(courseInfoBeans);
      emitter.onNext(true);
      emitter.onComplete();
    });
  }

  public static void insertImageRecord(ImageMessageRecord record) {
    DaoHelper.getDefault().getDaoSession().getImageMessageRecordDao().insertOrReplace(record);
  }

  public static Observable<Boolean> insertBigImageLoadRecord(BigImageLoadRecordBean bean) {
    return Observable.create(emitter -> {
      DaoHelper.getDefault()
          .getDaoSession()
          .getBigImageLoadRecordBeanDao()
          .insertOrReplace(bean);
      emitter.onNext(true);
      emitter.onComplete();
    });
  }

  public static Observable<Boolean> insertExamInfo(List<ExamInfoBean> examInfoBeans) {
    return Observable.create(emitter -> {
      DaoHelper.getDefault()
          .getDaoSession()
          .getExamInfoBeanDao()
          .insertInTx(examInfoBeans);
      emitter.onNext(true);
      emitter.onComplete();
    });
  }

  public static Observable<Boolean> insertGradeInfo(List<GradeInfoBean> gradeInfoBeans) {
    return Observable.create(emitter -> {
      DaoHelper.getDefault()
          .getDaoSession()
          .getGradeInfoBeanDao()
          .insertInTx(gradeInfoBeans);
      emitter.onNext(true);
      emitter.onComplete();
    });
  }

  public static Observable<Boolean> insertCollectUrl(String title, String url) {
    return Observable.create(emitter -> {
      try {
        DaoHelper.getDefault()
            .getDaoSession()
            .getWebUrlBeanDao()
            .insertInTx(new WebUrlBean(title, url, System.currentTimeMillis()));
        emitter.onNext(true);
      }catch (Exception e){
        emitter.onNext(false);
      }

      emitter.onComplete();
    });
  }

  public static Observable<Boolean> insertStatementInfo(StatementInfoBean statementInfoBean) {
    return Observable.create(emitter -> {
      DaoHelper.getDefault()
          .getDaoSession()
          .getStatementInfoBeanDao()
          .insertInTx(statementInfoBean);
      emitter.onNext(true);
      emitter.onComplete();
    });
  }
}
