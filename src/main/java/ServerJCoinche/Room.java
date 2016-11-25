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
    int team2score = 0;
    int bet = 0;
    String atout;
    Boolean isOnBet = false;
    Boolean isOnPlay = false;
    BetRound betRound;
    Game game;


    List<Client> ClientList = new ArrayList<Client>();
    List<Card> CardOnBoard = new ArrayList<Card>();
    List<Card> Team1Card = new ArrayList<Card>();
    List<Card> Team2Card = new ArrayList<Card>();

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

        } else if (isOnPlay) {
            game.GeMsg(str, GetIdChx(chx));
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
            //val.writeClient("Welcome to room " + id + " You are the player " + ClientList.indexOf(val) + " you are in team " + (ClientList.indexOf(val) % 2 == 0 ? "1" : "2"));
            val.writeClient("/ROOM " + id);
            val.writeClient("/PLAYER " + ClientList.indexOf(val));
            val.writeClient("/TEAM " + (ClientList.indexOf(val) % 2 == 0 ? "1" : "2"));
        }
        ReRound();
    }
    public void ReRound() {
        for (Client val : ClientList) {
            val.SetPass(false);
        }
        RandomDistrib();
        SendHandCard();
        betRound = new BetRound(this);
        isOnBet = true;
        isOnPlay = false;
        game = new Game(this);
    }

    public void SendHandCard() {
        for (Iterator<Client> i = ClientList.iterator(); i.hasNext(); ) {
            //String toSend = "Your hand is => ";
            String toSend = "/HAND ";
            Client cl = i.next();
            for (Iterator<Card> j = cl.GetCardOnHand().iterator(); j.hasNext(); ) {
                toSend += j.next().GetCard();
                toSend += " ";
            }
            cl.writeClient(toSend);
        }
    }

    private void RandomDistrib() {
        List<Card> CardPack = new ArrayList<Card>();
        CardPack.add(new Card("7S", 0, 0));
        CardPack.add(new Card("8S", 0, 0));
        CardPack.add(new Card("9S", 0, 14));
        CardPack.add(new Card("10S", 10, 10));
        CardPack.add(new Card("VS", 2, 20));
        CardPack.add(new Card("DS", 3, 3));
        CardPack.add(new Card("RS", 4, 4));
        CardPack.add(new Card("AS", 11, 11));
        CardPack.add(new Card("7D", 0, 0));
        CardPack.add(new Card("8D", 0, 0));
        CardPack.add(new Card("9D", 0, 14));
        CardPack.add(new Card("10D", 10, 10));
        CardPack.add(new Card("VD", 2, 20));
        CardPack.add(new Card("DD", 3, 3));
        CardPack.add(new Card("RD", 4, 4));
        CardPack.add(new Card("AD", 11, 11));
        CardPack.add(new Card("7H", 0, 0));
        CardPack.add(new Card("8H", 0, 0));
        CardPack.add(new Card("9H", 0, 14));
        CardPack.add(new Card("10H", 10, 10));
        CardPack.add(new Card("VH", 2, 20));
        CardPack.add(new Card("DH", 3, 3));
        CardPack.add(new Card("RH", 4, 4));
        CardPack.add(new Card("AH", 11, 11));
        CardPack.add(new Card("7C", 0, 0));
        CardPack.add(new Card("8C", 0, 0));
        CardPack.add(new Card("9C", 0, 14));
        CardPack.add(new Card("10C", 10, 10));
        CardPack.add(new Card("VC", 2, 20));
        CardPack.add(new Card("DC", 3, 3));
        CardPack.add(new Card("RC", 4, 4));
        CardPack.add(new Card("AC", 11, 11));

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

    public void SetBetRound(Boolean val) {
        isOnBet = val;
    }

    public void SetAtout(String atout) {
        this.atout = atout;
    }

    public String GetAtout() {
        return atout;
    }

    public void SeBet(int bet) {
        this.bet = bet;
    }

    public void SeIsOnPlay(Boolean val) {
        this.isOnPlay = val;
    }

    public int GetTeam1Score() {
        return team1score;
    }

    public int GetTeam2Score() {
        return team2score;
    }

    public void SetTeam1Score(int score) {
        team1score = score;
    }

    public void SetTeam2Score(int score) {
        team2score = score;
    }

    public int GetBet() {
        return bet;
    }
}
