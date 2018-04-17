package io.netty.example.callback;


/**
 * 监听器
 * @author stateis0
 */
public interface MyListener {

  /**
   * 子类需要重写此方法，在异步任务完成之后会回调此方法。
   * @param promise 异步结果占位符。
   */
  void operationComplete(MyPromise promise);

}
