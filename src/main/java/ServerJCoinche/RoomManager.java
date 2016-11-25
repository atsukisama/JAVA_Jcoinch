package ServerJCoinche;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rusig_n on 20/11/2016.
 */

public class RoomManager {
    List<Client> ClientList = new ArrayList<Client>();
    List<Room> RoomList = new ArrayList<Room>();

    public RoomManager() {

    }

    public void AddCLient(ChannelHandlerContext chx) {
        Client client = new Client(chx);
        ClientList.add(client);
        //client.writeClient("Wait for playing...");
        if (ClientList.size() >= 4) {
            Room room = new Room(RoomList.size());
            int k = 0;
            for (Iterator<Client> i = ClientList.iterator(); i.hasNext(); ) {
                if (k < 4) {
                    room.AddClient(i.next());
                    i.remove();
                }
                k++;
            }
            RoomList.add(room);
        }
    }

    public void SendMsgToRoom(ChannelHandlerContext chx, String msg) {
        System.out.print(msg);
        for (Iterator<Room> i = RoomList.iterator(); i.hasNext(); ) {
            Room room = i.next();
            for (Iterator<Client> j = room.GetClientList().iterator(); j.hasNext(); ) {
                if (j.next().GetChx() == chx) {
                    room.GetMsg(msg, chx);
                    return;
                }
            }
        }
    }

}
