package ClientJCoinche;

/**
 * Created by rusig_n on 20/11/2016.
 */

import Commun.TransfertClass;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class TelnetClientHandler extends SimpleChannelInboundHandler<String> {

    ClientGui clientGui = new ClientGui();
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        clientGui.ParseMsg(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}