package io.netty.example.callback;

import java.util.ArrayList;
import java.util.List;

/**
 * 异步执行结果占位符
 *
 * @author stateis0
 */
public class MyPromise {

  /** 监听器集合*/
  List<MyListener> listeners = new ArrayList<MyListener>();

  /** 是否成功*/
  boolean success;

  /** 执行结果**/
  Object result;

  /** 设置事变计数器**/
  int failCount;

  /**
   * 设置成功，并通知所有监听器。
   * @param result 结果
   * @return 是否成功
   */
  public boolean setSuccess(Object result) {
    if (success) {
      return false;
    }

    success = true;
    this.result = result;

    signalListeners();
    return true;
  }

  /**
   * 通知所有监听器，回调监听器方法。
   */
  private void signalListeners() {
    for (MyListener l : listeners) {
      l.operationComplete(this);
    }
  }

  /**
   * 设置失败
   * @param e 异常对象
   * @return 设置是否成功
   */
  public boolean setFail(Exception e) {
    if (failCount > 0) {
      return false;
    }
    ++failCount;
    result = e;
    signalListeners();
    return true;
  }

  /**
   * 是否成功执行
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * 添加监听器
   * @param myListener 监听器
   */
  public void addListener(MyListener myListener) {
    listeners.add(myListener);
  }

  /**
   * 删除监听器
   * @param myListener 监听器
   */
  public void removeListener(MyListener myListener) {
    listeners.remove(myListener);
  }

  /**
   * 获取执行结果
   */
  public Object get() {
    return result;
  }
}
