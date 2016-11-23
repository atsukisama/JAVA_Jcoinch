package ServerJCoinche;

import io.netty.channel.ChannelHandlerContext;
import jdk.nashorn.internal.ir.WhileNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by rusig_n on 20/11/2016.
 */

public class Room {

    int id;
    int turn;
    int team1score = 0;
    int Team2score = 0;
    int bet = 0;
    String atout;
    Boolean isOnBet = false;
    Boolean isOnPlay = false;
    BetRound betRound;


    List<Client> ClientList = new ArrayList<Client>();
    List<String> CardOnBoard = new ArrayList<String>();

    public Room(int id) {
        this.id = id;
        turn = 0;
    }

    public void AddClient(Client client) {
        ClientList.add(client);
        client.SetId(ClientList.size() - 1);
        if (ClientList.size() == 4) {
            InitRoom();
        }
    }

    public void GetMsg(String str, ChannelHandlerContext chx) {
        if (isOnBet) {
            betRound.GeMsg(str, GetIdChx(chx));

        } else {

        }

    }

    public List<Client> GetClientList() {
        return ClientList;
    }

    private Client GetIdChx(ChannelHandlerContext chx) {
        Client res = null;
        for (Iterator<Client> i = ClientList.iterator(); i.hasNext(); ) {
            Client cl = i.next();
            if (cl.chx.name() == chx.name())
                return (cl);
        }
        return res;
    }

    public void SendMsgToAll(String msg) {
        for (Client val : ClientList) {
            val.writeClient(msg);
        }
    }
    private void InitRoom() {
        for (Client val : ClientList) {
            val.writeClient("Welcome to room " + id + " You are the player " + ClientList.indexOf(val) + " you are in team " + (ClientList.indexOf(val) % 2 == 0 ? "1" : "2"));
        }
        RandomDistrib();
        SendHandCard();
        betRound = new BetRound(this);
        isOnBet = true;

    }

    private void RunBetTime() {

    }

    private void SendHandCard() {
        for (Iterator<Client> i = ClientList.iterator(); i.hasNext(); ) {
            String toSend = "Your hand is => ";
            Client cl = i.next();
            for (Iterator<String> j = cl.GetCardOnHand().iterator(); j.hasNext(); ) {
                toSend += j.next();
                toSend += " ";
            }
            cl.writeClient(toSend);
        }
    }

    private void RandomDistrib() {
        List<String> CardPack = new ArrayList<String>();
        CardPack.add("7S");
        CardPack.add("8S");
        CardPack.add("9S");
        CardPack.add("10S");
        CardPack.add("VS");
        CardPack.add("DS");
        CardPack.add("RS");
        CardPack.add("AS");
        CardPack.add("7D");
        CardPack.add("8D");
        CardPack.add("9D");
        CardPack.add("10D");
        CardPack.add("VD");
        CardPack.add("DD");
        CardPack.add("RD");
        CardPack.add("AD");
        CardPack.add("7H");
        CardPack.add("8H");
        CardPack.add("9H");
        CardPack.add("10H");
        CardPack.add("VH");
        CardPack.add("DH");
        CardPack.add("RH");
        CardPack.add("AH");
        CardPack.add("7C");
        CardPack.add("8C");
        CardPack.add("9C");
        CardPack.add("10C");
        CardPack.add("VC");
        CardPack.add("DC");
        CardPack.add("RC");
        CardPack.add("AC");

        for (Iterator<Client> i = ClientList.iterator(); i.hasNext(); ) {
            int max = 32;
            Client cl = i.next();
            for (int k = 0; k < 8; k++) {
                Random rand = new Random();

                int rd = rand.nextInt(max);
                while (rd > CardPack.size())
                    rd = rand.nextInt(max);
                if (rd < CardPack.size()) {
                    cl.addCard(CardPack.get(rd));
                    CardPack.remove(rd);
                } else {
                    cl.addCard(CardPack.get(0));
                    CardPack.remove(0);
                }
                max--;
            }

        }
    }

    private void RunOneRound() {

    }

    public void SetBet(Boolean val) {
        isOnBet = val;
    }

}
