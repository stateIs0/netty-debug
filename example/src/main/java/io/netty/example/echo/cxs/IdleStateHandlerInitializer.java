package io.netty.example.echo.cxs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.WriteTimeoutException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {

  @Override
  protected void initChannel(Channel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    // IdleStateHandler 将在被触发时发送一个 IdleStateEvent 事件
//    pipeline.addLast(new IdleStateHandler(true,2, 4, 6, TimeUnit.SECONDS));
//    pipeline.addLast(new ReadTimeoutHandler(10));
//    pipeline.addLast("writeTimeoutHandler", new WriteTimeoutHandler(1, TimeUnit.NANOSECONDS));
    pipeline.addLast(new StringDecoder());
    pipeline.addLast(new StringEncoder());

    pipeline.addLast(new HeartbeatHandler());

  }
}

class HeartbeatHandler extends ChannelInboundHandlerAdapter {

  private static final ByteBuf heartbeat_sequence = Unpooled
      .unreleasableBuffer(Unpooled.copiedBuffer("heartbeat",
          Charset.forName("UTF-8")));

  @Override// FileRegion ByteBuf
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.err.println(msg);

    String msg1 = "I am Server ";
    for (int i = 0; i < 1; i++) {
      msg1 += msg1;
    }
    ctx.writeAndFlush(msg1);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    if (evt instanceof IdleStateEvent) {
      // 发送心跳消息，并在发送失败时关闭连接
      if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
        System.out.println("                     READER_IDLE");

      } else if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
        System.err.println("WRITE_IDLE");
//        ctx.writeAndFlush(heartbeat_sequence.duplicate())
//            .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

      } else if (((IdleStateEvent) evt).state() == IdleState.ALL_IDLE) {
        System.err.println("ALL_IDLE");
      }
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//    if (cause instanceof ReadTimeoutException) {
//      System.err.println("ReadTimeOut");
//    }
    cause.printStackTrace();
    if (cause instanceof WriteTimeoutException) {
      System.out.println("hello");
    }

  }
}


