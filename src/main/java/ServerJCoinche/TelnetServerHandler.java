package ServerJCoinche;

/**
 * Created by rusig_n on 20/11/2016.
 */

import Commun.TransfertClass;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;
import java.util.Date;
import java.util.Objects;

@Sharable
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

    RoomManager _roomManager;

    public  TelnetServerHandler()
    {
        _roomManager = new RoomManager();
        System.out.println("TELNET SEVER CONS");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        ctx.write("Welcome to JCoiche game!\r\n");
        ctx.flush();
        _roomManager.AddCLient(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        // Generate and write a response.

        _roomManager.SendMsgToRoom(ctx, request);
        return;

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}