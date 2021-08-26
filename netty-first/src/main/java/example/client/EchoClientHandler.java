package example.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

public class EchoClientHandler implements ChannelInboundHandler {
  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelRegistered");
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelUnregistered");
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelActive");
    ctx.writeAndFlush("Hi");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelInactive");
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println("channelRead");
    System.out.println(msg.toString());
    ctx.close();
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelReadComplete");
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    System.out.println("userEventTriggered");
  }

  @Override
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelWritabilityChanged");
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    System.out.println("handlerAdded");
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    System.out.println("handlerRemoved");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println("exceptionCaught");
  }
}
