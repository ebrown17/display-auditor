package server;

import java.util.concurrent.LinkedBlockingQueue;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import protobuf.JdssAuditor;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

  private static final int WRITE_IDLE_TIME = 10;
  private LinkedBlockingQueue<JdssAuditor.DisplayData> msgQueue;
  public ServerChannelInitializer(LinkedBlockingQueue<JdssAuditor.DisplayData> msgQueue) {
	  this.msgQueue=msgQueue;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline p = ch.pipeline();

    // TODO implement heartbeat protocol
    /*
     * p.addLast("idleStateHandler", new IdleStateHandler(0, WRITE_IDLE_TIME, 0));
     * p.addLast("heartBeatHandler", new ServerHeartbeatHandler());
     */

    p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
    p.addLast("protobufDecoder", new ProtobufDecoder(JdssAuditor.DisplayData.getDefaultInstance()));
    p.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
    p.addLast("protobufEncoder", new ProtobufEncoder());
    p.addLast(new ServerDataHandler(msgQueue));

  }

}
