package example;

import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles a server-side channel.
 */
public class EchoHandler extends ChannelInboundHandlerAdapter { // (1)

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
    ctx.write(msg); // (1)
    ctx.flush(); // (2)
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}