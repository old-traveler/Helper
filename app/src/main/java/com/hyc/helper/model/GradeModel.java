package com.hyc.helper.model;

import com.hyc.helper.bean.GradeBean;
import com.hyc.helper.bean.GradeInfoBean;
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

public class GradeModel {

  public void getGradeFromApi(UserBean userBean,Observer<GradeBean> observer){
    RequestHelper.getRequestApi().getGradeInfo(userBean.getData().getStudentKH(),userBean.getRemember_code_app())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  private Disposable insertGradeIntoDb(List<GradeInfoBean> gradeInfoBeans){
    return DbInsertHelper.insertGradeInfo(gradeInfoBeans)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aBoolean -> LogHelper.log("插入数据完成"));
  }

  public Disposable refreshLocalDb(List<GradeInfoBean> gradeInfoBeans){
    return DbDeleteHelper.deleteUserGradeInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe(aBoolean -> insertGradeIntoDb(gradeInfoBeans));
  }

  public void getGradeInfoFromCache(Observer<GradeBean> observer){
    DbSearchHelper.searchGradeInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public Disposable deleteGradeInfoFromCache(){
    return DbDeleteHelper.deleteUserGradeInfo()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe();
  }




}
