package com.hyc.helper.helper;

import com.hyc.helper.bean.BigImageLoadRecordBean;
import com.hyc.helper.bean.CourseBean;
import com.hyc.helper.bean.CourseInfoBean;
import com.hyc.helper.gen.BigImageLoadRecordBeanDao;
import com.hyc.helper.gen.CourseInfoBeanDao;
import io.reactivex.Observable;
import java.util.List;

public class DbSearchHelper {

  public static Observable<CourseBean> searchCourseInfo(String studentId) {
    return Observable.create(emitter -> {
      List<CourseInfoBean> courseInfoBeans = DaoHelper.getDefault()
          .getDaoSession()
          .getCourseInfoBeanDao()
          .queryBuilder()
          .where(CourseInfoBeanDao.Properties.Xh.eq(studentId))
          .build().list();
      CourseBean courseBean = new CourseBean();
      if (courseInfoBeans != null && courseInfoBeans.size() > 0) {
        courseBean.setCode(Constant.REQUEST_SUCCESS);
      } else {
        courseBean.setCode(Constant.NEED_API_DATA);
      }
      courseBean.setData(courseInfoBeans);
      emitter.onNext(courseBean);
      emitter.onComplete();
    });
  }

  public static Observable<BigImageLoadRecordBean> searchBigImageLoadRecord(String originUrl){
    return Observable.create(emitter -> {
      BigImageLoadRecordBean bean = DaoHelper.getDefault()
          .getDaoSession()
          .getBigImageLoadRecordBeanDao()
          .queryBuilder()
          .where(BigImageLoadRecordBeanDao.Properties.OriginUrl.eq(originUrl))
          .build().unique();
      emitter.onNext(bean);
      emitter.onComplete();
    });
  }

}
