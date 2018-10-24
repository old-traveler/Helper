package com.hyc.helper.model;

import com.hyc.helper.bean.ExamBean;
import com.hyc.helper.bean.ExamInfoBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.DbDeleteHelper;
import com.hyc.helper.helper.DbInsertHelper;
import com.hyc.helper.helper.DbSearchHelper;
import com.hyc.helper.helper.LogHelper;
import com.hyc.helper.helper.RequestHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class ExamModel {

  public void getExam(UserBean userBean,Observer<ExamBean> observer){
    RequestHelper.getRequestApi().getExam(userBean.getData().getStudentKH(),userBean.getRemember_code_app())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  private Disposable insertExamIntoDb(List<ExamInfoBean> examInfoBeans){
    return DbInsertHelper.insertExamInfo(examInfoBeans)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> LogHelper.log("插入数据完成"));
  }

  public Disposable refreshLocalDb(List<ExamInfoBean> examInfoBeans){
    return DbDeleteHelper.deleteUserExamInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(aBoolean -> insertExamIntoDb(examInfoBeans));
  }

  public void getExamInfoFromCache(Observer<ExamBean> observer){
    DbSearchHelper.searchExamInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

}
