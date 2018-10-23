package com.hyc.helper.model;

import android.content.Context;
import android.net.Uri;
import com.hyc.helper.bean.FindPeopleBean;
import com.hyc.helper.bean.ImageUploadBean;
import com.hyc.helper.bean.UserBean;
import com.hyc.helper.helper.FileHelper;
import com.hyc.helper.helper.RequestHelper;
import com.hyc.helper.helper.SpCacheHelper;
import com.hyc.helper.helper.UploadImageObserver;
import com.hyc.helper.util.Sha1Utils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

  private static WeakReference<UserBean> curUserBean;

  public void login(String account, String password, Observer<UserBean> observer) {
    RequestHelper.getRequestApi().login(account, password)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
  }

  public void cacheUserInfo(UserBean userBean) {
    SpCacheHelper.putClassIntoSp("user", userBean);
    curUserBean = new WeakReference<>(userBean);
  }

  public UserBean getCurUserInfo() {
    if (curUserBean == null || curUserBean.get() == null) {
      UserBean userBean = SpCacheHelper.getClassFromSp("user", UserBean.class);
      curUserBean = new WeakReference<>(userBean);
    }
    return curUserBean.get();
  }

  public String getStudentId() {
    return getCurUserInfo().getData().getStudentKH();
  }

  public Observable<FindPeopleBean> findUserInfoByName(String name) {
    UserBean curUser = getCurUserInfo();
    return RequestHelper.getRequestApi()
        .findPeople(curUser.getData().getStudentKH(), curUser.getRemember_code_app(),
            Sha1Utils.getEnv(curUser), name)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public void updateUserHeadImage(Context context,UserBean bean,Uri uri,UploadImageObserver observer){
    List<File> files = new ArrayList<>(1);
    files.add(new File(FileHelper.getFilePath(context,uri)));
    FileHelper.uploadImage(bean,"3",files,observer);
  }

  public void updateLocalUserHeadImage(String image){
    UserBean bean = getCurUserInfo();
    bean.getData().setHead_pic_thumb(image);
    cacheUserInfo(bean);
  }

}
