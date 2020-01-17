package com.hyc.helper.util;

import android.annotation.SuppressLint;
import com.hyc.helper.annotation.Subscribe;
import com.hyc.helper.bean.MessageEvent;
import com.hyc.helper.helper.LogHelper;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class RxBus {

  private volatile static RxBus rxBus;

  private HashMap<String, ArrayList<Object>> subscribeMap;

  private RxBus() {
    throw new IllegalArgumentException("must call getDefault() to get instance");
  }

  public static RxBus getDefault() {
    if (rxBus == null) {
      synchronized (RxBus.class) {
        if (rxBus == null) {
          rxBus = new RxBus();
        }
      }
    }
    return rxBus;
  }

  public void register(Object object) {
    Subscribe subscribe = getSubscribe(object);
    if (subscribe == null) {
      throw new RuntimeException("you must defined a onEvent()");
    }
    if (subscribeMap == null) {
      subscribeMap = new HashMap<>();
    }
    for (String s : subscribe.eventType()) {
      ArrayList<Object> arrayList = subscribeMap.get(s);
      if (arrayList == null) {
        arrayList = new ArrayList<>();
      } else if (arrayList.contains(object)) {
        throw new RuntimeException("the object have subscribed");
      }
      arrayList.add(object);
      subscribeMap.put(s, arrayList);
    }
    LogHelper.log("register " + object.getClass().getSimpleName());
  }

  public boolean isRegister(Object object) {
    Subscribe subscribe = getSubscribe(object);
    if (subscribe == null) {
      throw new RuntimeException("you must defined a onEvent()");
    }
    if (subscribeMap == null) {
      return false;
    }
    if (subscribe.eventType().length > 0) {
      ArrayList<Object> arrayList = subscribeMap.get(subscribe.eventType()[0]);
      return arrayList != null && arrayList.contains(object);
    } else {
      return false;
    }
  }

  public synchronized void unregister(Object object) {
    Subscribe subscribe = getSubscribe(object);
    if (subscribe == null) {
      throw new RuntimeException("the object not is a subscriber");
    }
    String[] type = subscribe.eventType();
    try {
      for (String s : type) {
        if (subscribeMap.get(s) != null) {
          subscribeMap.get(s).remove(object);
        }
      }
    } catch (Exception e) {
      LogHelper.log(e.getMessage());
    }
    LogHelper.log("unregister " + object.getClass().getSimpleName());
  }

  public void post(MessageEvent messageEvent) {
    if (subscribeMap != null && subscribeMap.get(messageEvent.getType()) != null) {
      for (Object o : subscribeMap.get(messageEvent.getType())) {
        dispatchMessage(o, messageEvent);
      }
    }
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @SuppressLint("CheckResult")
  private void dispatchMessage(Object o, MessageEvent messageEvent) {
    Subscribe subscribe = getSubscribe(o);
    if (subscribe == null) {
      return;
    }
    try {
      Method subscribeMethod = o.getClass().getMethod("onEvent", MessageEvent.class);
      Flowable.just(subscribeMethod)
          .map(method -> {
            method.invoke(o, messageEvent);
            return o;
          })
          .subscribeOn(getScheduler(subscribe.threadMode()))
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(o1 -> LogHelper.log(o1.getClass().getSimpleName()),
              throwable -> LogHelper.log(throwable.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Scheduler getScheduler(int threadMode) {
    if (threadMode == ThreadMode.IO) {
      return Schedulers.io();
    } else if (threadMode == ThreadMode.MAIN) {
      return AndroidSchedulers.mainThread();
    }
    return AndroidSchedulers.mainThread();
  }

  private Subscribe getSubscribe(Object object) {
    Method method;
    try {
      method = object.getClass().getMethod("onEvent", MessageEvent.class);
      if (method != null) {
        return method.getAnnotation(Subscribe.class);
      }
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    return null;
  }
}
