package io.netty.example.echo.cxs;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ContailDemo {

  public static void main1(String[] args) {
    Charset utf8 = Charset.forName("UTF-8");
    ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks", utf8);
    System.out.println((char) buf.getByte(0));

    int readerIndex = buf.readerIndex();
    int writerIndex = buf.writerIndex();

    buf.setByte(0, (byte) 'B');

    String s;

    System.out.println((char) buf.getByte(0));

    assert readerIndex != buf.readerIndex();
    assert writerIndex != buf.writerIndex();
  }

  /**
   * @see io.netty.buffer.ByteBufHolder
   * @see io.netty.buffer.ByteBufAllocator
   * @see io.netty.buffer.PooledByteBufAllocator netty 默认使用池化
   * @see io.netty.buffer.UnpooledByteBufAllocator
   * @see io.netty.buffer.ByteBuf
   *
   * util,allocator,holdre,Unpooled 等辅助工具
   *
   *
   * @param args
   */
  public static void main(String[] args) throws IllegalAccessException, InstantiationException {

    /**
     * 通常，ByteBufAllocator s = channel.alloc()
     * ByteBufAllocator s = channelHandlerContext.alloc()
     * Netty 默认使用池化
     */

    PooledByteBufAllocator pool = new PooledByteBufAllocator();
    pool.buffer();
    pool.heapBuffer();
    pool.directBuffer();
    pool.compositeBuffer();
    pool.ioBuffer();// 当支持 Unsafe 时，返回直接内存的 Bytebuf，否则返回返回基于堆内存，当使用 PreferHeapByteBufAllocator 时返回堆内存

    // 当你无法获取一个 Allocator 引用获取 ByteBuf 时，可以使用 @see io.netty.buffer.Unpooled 获取未池化的 ByteBuf
    Unpooled.buffer();
    Unpooled.directBuffer();
    Unpooled.wrappedBuffer(new byte[1]);
    Unpooled.copiedBuffer(new byte[1]);

    ByteBufUtil.hexDump(ByteBuf.class.newInstance());// 以16进制打印给定内容，可以调试。
    ByteBufUtil.equals(ByteBuf.class.newInstance(), ByteBuf.class.newInstance());
    // 对需要编码的字符串 src 按照 charset 格式进行编码。利用指定的 allocated 生产一个新的 ByteBuf，当然还有 decode 方法
    ByteBufUtil.encodeString(new PooledByteBufAllocator(), CharBuffer.allocate(1), Charset.forName("UTF-8"));//

    // 引用计数对于池化实现至关重要，可以降低内存分配的开销
    ByteBuf buf = Unpooled.buffer();
    boolean count = buf.refCnt() == 1;
    System.out.println(count);
    buf.release();
    System.out.println(buf.refCnt());

    /**
     * 1. 使用不同的读写索引来控制数据访问
     * 2. 使用内存的不同方式-----基于字节数组和直接缓冲区
     * 3. 通过 CompositeByteBuf 生成多个 ByteBuf 的聚合视图
     * 4. 数据访问方法----搜索，切片以及复制
     * 5. 读，写，获取和设置API
     * 6. ByteBufAllocator 池化和引用计数
     */
  }


}
