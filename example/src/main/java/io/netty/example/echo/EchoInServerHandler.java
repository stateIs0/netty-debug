/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoInServerHandler extends SimpleChannelInboundHandler {

  Logger logger = LoggerFactory.getLogger(getClass());

  static Executor businessExecutors = new ThreadPoolExecutor(1, 1,
      0L, TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<Runnable>());

  @Override// 2
  public void channelRead0(ChannelHandlerContext ctx, Object msg)
      throws UnsupportedEncodingException, InterruptedException {
//    ByteBuf buf = (ByteBuf) msg;
//    byte[] req = new byte[buf.readableBytes()];
//    buf.readBytes(req);
//    String body = new String(req, "UTF-8");

    logger.warn(msg + " " + Thread.currentThread().getName());
    String reqString = "Hello I am Server";
    logger.error("EchoInServerHandler printing");
    ByteBuf resp = Unpooled.copiedBuffer(reqString.getBytes());
    ctx.writeAndFlush(resp);
  }
  // 3
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    logger.error("channelReadComplete ");
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}

class EchoOutServerHandler extends ChannelOutboundHandlerAdapter {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Override // 1 // 4
  public void read(ChannelHandlerContext ctx) throws Exception {
    logger.error("out read");
    super.read(ctx);
  }

  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    logger.error("out write");
    super.write(ctx, msg, promise);
  }

  @Override
  public void flush(ChannelHandlerContext ctx) throws Exception {
    logger.error("out flush");
    super.flush(ctx);
  }

}
