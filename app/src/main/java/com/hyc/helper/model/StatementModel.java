package com.hyc.helper.model;

import com.hyc.helper.bean.BaseRequestBean;
import com.hyc.helper.bean.StatementBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.RequestHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class StatementModel {

  public void getStatementByPage(int page,String number,Observer<StatementBean> observer){
    RequestHelper.getRequestApi().getStatement(number,page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public Disposable likeStatement(UserBean userBean,String statementId){
    return RequestHelper.getRequestApi().likeStatement(
        userBean.getData().getStudentKH(),userBean.getRemember_code_app(),statementId)
        .subscribeOn(Schedulers.io())
        .subscribe();
  }

  public Disposable commentStatement(String number,String code,String comment,String monment_id,Consumer<BaseRequestBean> consumer){
    return RequestHelper.getRequestApi().commentStatement(number,code,comment,monment_id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(consumer);
  }

}
