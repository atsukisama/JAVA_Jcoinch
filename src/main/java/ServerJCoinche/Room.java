package ServerJCoinche;

import jdk.nashorn.internal.ir.WhileNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rusig_n on 20/11/2016.
 */

public class Room {

    int id;
    int turn;

    List<Client> ClientList = new ArrayList<Client>();
    List<String> CardOnBoard = new ArrayList<String>();

    public Room(int id) {
        this.id = id;
        turn = 0;
    }

    public void AddClient(Client client)
    {
         ClientList.add(client);
        if (ClientList.size() == 4)
        {
            InitRoom();
        }
    }

    public void GetMsg(String str)
    {

    }

    public List<Client> GetClientList()
    {
        return ClientList;
    }

    private void InitRoom()
    {
        for (Client val:ClientList)
        {
            val.writeClient("Welcome to room " + id + " You are the player" + ClientList.indexOf(val));
        }
    }

    private void RunOneRound()
    {

    }

}
