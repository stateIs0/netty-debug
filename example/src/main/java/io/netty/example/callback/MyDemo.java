package io.netty.example.callback;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author stateis0
 */
public class MyDemo {

  public static void main(String[] args) {

    // 占位对象
    final MyPromise myPromise = new MyPromise();

    final Dto dto = new Dto();

    // 线程池
    Executor executor = Executors.newFixedThreadPool(1);

    // 异步执行任务，
    executor.execute(new MyRunnable<String>(myPromise) {
      @Override
      public String doWork() {
        return dto.doSomething();
      }
    });

    // 添加一个监听器
    myPromise.addListener(new MyListener() {
      // 当任务完成后，就执行此方法。
      @Override
      public void operationComplete(MyPromise promise) {
        // 获取结果
        String result;
        // 如果任务成功执行了
        if (promise.isSuccess()) {
          // 获取结果并打印
          result = (String) promise.get();
          System.out.println("operationComplete ----> " + result);
        }
        // 如果失败了, 打印异常堆栈
        else {
          ((Exception) promise.get()).printStackTrace();
        }
      }
    });
  }

}

class Dto {

  public String doSomething() {
    System.out.println("doSomething");
//    throw new RuntimeException("cxs");
    return "result is success";
  }
}
