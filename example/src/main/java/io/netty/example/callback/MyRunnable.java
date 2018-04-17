package io.netty.example.callback;

/**
 * 一个任务类，通过重写 doWork 方法执行任务
 * @param <V> 返回值类型
 * @author stateis0
 */
public abstract class MyRunnable<V> implements Runnable {

  final MyPromise myPromise;

  protected MyRunnable(MyPromise myPromise) {
    this.myPromise = myPromise;
  }

  @Override
  public void run() {
    try {
      V v = doWork();
      myPromise.setSuccess(v);
    } catch (Exception e) {
      myPromise.setFail(e);
    }
  }

  /**
   * 子类需要重写此方法。并返回值，这个值由 Promise 的 get 方法返回。
   */
  public abstract V doWork();
}
