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
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import java.io.UnsupportedEncodingException;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

  private final ByteBuf firstMessage;

  /**
   * Creates a client-side handler.
   */
  public EchoClientHandler() throws InterruptedException {
    firstMessage = Unpooled.buffer(EchoClient.SIZE);
    for (int i = 0; i < firstMessage.capacity(); i++) {

      firstMessage.writeByte((byte) i);
    }
  }


  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws InterruptedException {
    final ByteBuf buf = Unpooled.copiedBuffer(("Hello I am Client".getBytes()));
    ctx.writeAndFlush(buf);

  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg)
      throws UnsupportedEncodingException {
//    ByteBuf buf = (ByteBuf) msg;
//    byte[] req = new byte[buf.readableBytes()];
//    buf.readBytes(req);
//    String body = new String(req, "UTF-8");
    System.err.println(msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }


  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    if (evt instanceof IdleStateEvent) {
      // 发送心跳消息，并在发送失败时关闭连接
      ctx.writeAndFlush("hello")
          .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
      return;
    }
    super.userEventTriggered(ctx, evt);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}
