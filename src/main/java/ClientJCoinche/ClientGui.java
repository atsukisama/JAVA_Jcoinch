package ClientJCoinche;


import Commun.TransfertClass;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ClientGui {

    int roomId  = -1;
    int clientId = -1;

    public ClientGui() {

    }

    public int GetRoomId(){
        return roomId;
    }

    public int GetClientId(){
        return clientId;
    }
    public void SetRoomId(int id){
        roomId = id;
    }
    public void SetClientId(int id){
        roomId  = id;
    }
    
    public void DispCardOnHand(String cards) {

    }

    public void DispCardOnTable(String cards) {
        
    }

    public void ParseMsg( String str) {
        System.out.print("server sent => " + str + "\n");
    }

}