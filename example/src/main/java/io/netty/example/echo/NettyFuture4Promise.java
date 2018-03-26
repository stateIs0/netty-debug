package io.netty.example.echo;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import java.util.concurrent.ExecutionException;

public class NettyFuture4Promise {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    NettyFuture4Promise test = new NettyFuture4Promise();
    Promise<String> promise = test.search("Netty In Action");

    promise.addListener(new GenericFutureListener<Future<? super String>>() {
      @Override
      public void operationComplete(Future<? super String> future) throws Exception {
        System.out.println("listener 1");
      }
    });

    promise.addListener(new GenericFutureListener<Future<? super String>>() {
      @Override
      public void operationComplete(Future<? super String> future) throws Exception {
        if (future.isSuccess()) {
          System.out.println("error " + future.get());
        }
        System.out.println("listener 2");
      }
    });

    String result = promise.get();
    System.out.println("price is " + result);
  }

  //
  private Promise<String> search(final String prod) {
    NioEventLoopGroup loop = new NioEventLoopGroup();
    // 创建一个DefaultPromise并返回
    final DefaultPromise<String> promise = new DefaultPromise<String>(loop.next());
    loop.execute(new Runnable() {
      @Override
      public void run() {
        try {
          System.out.println(String.format("	>>search price of %s from internet!", prod));
          Thread.sleep(11);
          promise.setSuccess("$99.99");// 等待5S后设置future为成功，
//          promise.setFailure(new NullPointerException()); //当然，也可以设置失败
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    return promise;
  }

}
