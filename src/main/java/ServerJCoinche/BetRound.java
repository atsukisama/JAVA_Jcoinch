package ServerJCoinche;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.BooleanSupplier;

/**
 * Created by rusig_n on 22/11/2016.
 */

public class BetRound {
    int bet;
    String atout;
    int turn = 0;
    Room room;
    int first = 0;
    int max = 160;

    public BetRound(Room rom) {
        this.room = rom;
        room.SendMsgToAll("Its turn of player => " + turn);
    }

    public void GeMsg(String msg, Client cl) {
        if (turn == cl.GetId()) {
            if (!VerifMsg(msg, cl)) {
                cl.writeClient("Bad cmd retry");
                return;
            } else {
                room.SendMsgToAll("Current bet => " + bet + "trump => " + atout);
                ChangeRound();
            }


        } else {
            cl.writeClient("It's not your turn");
        }
    }

    private Boolean VerifMsg(String msg, Client cl) {
        String[] strtab = msg.split(" ");
        if (strtab[0].contains("pass")) {
            cl.pass = true;
            return true;
        }
        if (strtab.length != 2) {

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
                if (newBet - bet != 10 && first > 0) {
                    return false;
                }
                if (first == 0) {
                    if (newBet >= 80 && newBet % 10 == 0) {
                        bet = newBet;
                        atout = strtab[1];
                        first++;
                    }
                } else if (newBet >= 80 && newBet % 10 == 0) {
                    bet = newBet;
                    atout = strtab[1];
                }


            } else
                return false;
        } else
            return false;
        return true;
    }

    private void ClearBetRoundClass() {
        bet = 0;
        atout = "";
        first = 0;
        max = 160;
    }
    private void ChangeRound() {
        int i = turn;

        i++;
        i = i % 4;
        for (int j = 0; j < 3; j++) {
            if (!room.GetClientList().get(i).GetPass()) {
                turn = i;
                room.SendMsgToAll("Its turn of player => " + turn);
                return;
            }
            i++;
            i = i % 4;
        }
        room.SendMsgToAll("Final bet => " + bet + " trump => " + atout);
        room.SetBet(false);
        ClearBetRoundClass();
    }
}
