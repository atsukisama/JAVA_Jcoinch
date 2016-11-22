package ServerJCoinche;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.BooleanSupplier;

/**
 * Created by rusig_n on 22/11/2016.
 */

public class BetRound
{
    int bet;
    Boolean isOnBet;
    String atout;
    int turn;

    public BetRound() {

    }

    public void GeMsg(String msg, Client cl) {
        if (turn == cl.GetId())
        {
            if (!VerifMsg(msg, cl))
            {
                cl.writeClient("Bad cmd retry");
                return;
            }
            else
            {
            }


        }
        else
        {
            cl.writeClient("It's not your turn");
        }
    }

    public Boolean GetIsOnBet() {
        return isOnBet;
    }

    private Boolean VerifMsg(String msg, Client cl) {
        String[] strtab = msg.split(" ");
        if (strtab.length != 2) {
            if (strtab[0] == "pass") {
                cl.pass = true;
                return true;
            }
            return false;
        }
        int newBet;
        try {
            newBet = Integer.parseInt(strtab[0]);
        } catch (NumberFormatException e) {
            return false;
        }
        if (strtab[1].contains("H") || strtab[1].contains("D") || strtab[1].contains("C") || strtab[1].contains("S")) {
            if (bet < newBet) {
                bet = newBet;
                atout = strtab[1];
            }
        }
        else
            return false;
        return true;
    }

    private  void  ChangeRound() {
        int i = turn;

        i++;
        i = i % 4;
        for (int j = 0; j < 4; j++) {
            
        }
    }

}
