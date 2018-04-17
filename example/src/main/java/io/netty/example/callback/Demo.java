package io.netty.example.callback;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class Demo {

  public static void main(String[] args) {

    NioEventLoopGroup loop = new NioEventLoopGroup();

    final DefaultPromise promise = new DefaultPromise(loop.next());

    loop.next().execute(new Runnable() {
      @Override
      public void run() {
        System.out.println("hello");
        promise.setSuccess("success");
      }
    });

    promise.addListener(new GenericFutureListener<Future<? extends String>>() {
      @Override
      public void operationComplete(Future future) throws Exception {
        if (future.isSuccess()) {
          System.out.println("success !!!!!!");
          System.out.println("do something");
        } else {
          System.out.println("fail !!!!");
          System.out.println("throw Exception");
        }
      }
    });


  }

}
