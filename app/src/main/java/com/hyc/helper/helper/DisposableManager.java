package com.hyc.helper.helper;

import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

public class DisposableManager {

  private List<Disposable> disposableList;

  public DisposableManager(int size){
    disposableList = new ArrayList<>(size);
  }

  public DisposableManager(){

  }

  public void addDisposable(Disposable disposable){
    if (disposableList == null){
      disposableList = new ArrayList<>();
    }
    disposableList.add(disposable);
  }

  public void addDisposable(int index,Disposable disposable){
    if (disposableList == null){
      disposableList = new ArrayList<>();
    }
    disposableList.add(index,disposable);
  }

  public void cancelDisposable(int index){
    if (disposableList != null && index >= 0 && index < disposableList.size()){
      Disposable disposable = disposableList.get(index);
      if (disposable != null && !disposable.isDisposed()){
        disposable.dispose();
      }
    }
  }

  public void cancelAllDisposable(){
    if (disposableList == null || disposableList.size() == 0){
      return;
    }
    for (Disposable disposable : disposableList) {
      if (disposable != null && !disposable.isDisposed()){
        disposable.dispose();
      }
    }
    disposableList.clear();
  }


}
