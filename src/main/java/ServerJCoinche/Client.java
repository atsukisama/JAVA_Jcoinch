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
    List<Card> CardtOnHand = new ArrayList<Card>();

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

    public void addCard(Card card)
    {
        CardtOnHand.add(card);
    }

    public void celarCard()
    {
        CardtOnHand.clear();
    }

    public List<Card> GetCardOnHand() {
        return CardtOnHand;
    }

    public Boolean GetPass() {
        return pass;
    }

    public void SetPass(Boolean val) {
        pass = val;
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

    public Boolean GotTrump(String trump) {
        for (Card val : CardtOnHand) {
            if (val.GetCard().contains(trump))
                return true;
        }
        return false;
    }

    public Boolean GotCard(String card) {
        for (Card val : CardtOnHand) {
            if (val.GetCard().contains(card))
                return true;
        }
        return false;
    }

    public Card SendCard(String card) {
        for (Card val : CardtOnHand) {
            if (val.GetCard().contains(card)) {
                return val;
            }
        }
        return null;
    }

    public void ErasCard(String card) {
        for (Card val : CardtOnHand) {
            if (val.GetCard().contains(card)) {
                CardtOnHand.remove(val);
                return;
            }
        }
    }
}
