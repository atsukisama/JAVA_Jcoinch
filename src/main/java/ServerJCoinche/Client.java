package ServerJCoinche;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rusig_n on 20/11/2016.
 */
public class Client {
    int id;
    String msg;
    ChannelHandlerContext chx;
    List<String> CardtOnHand = new ArrayList<String>();

    public Client(ChannelHandlerContext chx)
    {
        this.chx = chx;
    }

    public ChannelHandlerContext GetChx()
    {
        return chx;
    }
    public void writeClient(String str)
    {
        chx.write(str);
        chx.flush();
    }
}
