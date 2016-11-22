package ServerJCoinche;

import Commun.TransfertClass;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rusig_n on 20/11/2016.
 */

public class Client {
    int id;
    String msg;
    Boolean pass = false;
    int bet = 0;
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

    public void writeClient(String str) {
        chx.write(str + "\r\n");
        chx.flush();
    }

    public void addCard(String card)
    {
        CardtOnHand.add(card);
    }

    public void celarCard()
    {
        CardtOnHand.clear();
    }

    public List<String> GetCardOnHand() {
        return CardtOnHand;
    }

    public Boolean GetPass() {
        return pass;
    }

    public void ChangePass() {
        pass = !pass;
    }

    public int GetBet() {
        return bet;
    }

    public  void SetBet(int bet) {
        this.bet = bet;
    }

    public int GetId()    {
        return id;
    }

    public void SetId(int id) {
        this.id = id;
    }
}
