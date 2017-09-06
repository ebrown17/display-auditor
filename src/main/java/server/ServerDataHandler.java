package server;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import protobuf.JdssAuditor;
import protobuf.ProtobufMessage;

public class ServerDataHandler extends SimpleChannelInboundHandler<JdssAuditor.DisplayData> {

  private ChannelHandlerContext ctx;
  private final Logger logger = LoggerFactory.getLogger("server.ServerDataHandler");
  private final static ProtobufMessage.ProtobufData heartbeat =
      ProtobufMessage.ProtobufData.newBuilder().setDataString("HeartBeat").build();

  private LinkedBlockingQueue<JdssAuditor.DisplayData> msgQueue;
  
  public ServerDataHandler(LinkedBlockingQueue<JdssAuditor.DisplayData> msgQueue){
	  this.msgQueue=msgQueue;
  }
  
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, JdssAuditor.DisplayData msg) throws Exception {
    logger.trace("channelRead0 {} sent: {}", ctx.channel().remoteAddress(), msg.toString());
    msgQueue.offer(msg);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    this.ctx = ctx;
    logger.info("channelActive remote peer: {} connected", ctx.channel().remoteAddress());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    logger.info("channelInactive remote peer: {} disconnected", ctx.channel().remoteAddress());
  }

  public void sendheartBeat() {
    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
      logger.debug("sendheartBeat > sending... {} ", heartbeat);
      ctx.writeAndFlush(heartbeat);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    logger.warn("Exception in connection from {} cause {}", ctx.channel().remoteAddress(), cause.toString());
    ctx.close();
  }

}
