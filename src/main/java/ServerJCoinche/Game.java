package ServerJCoinche;

import java.util.*;

/**
 * Created by rusig_n on 23/11/2016.
 */
public class Game {

    int turn = 0;
    Room room;
    Map<Card, Integer> CardOnTable = new HashMap<Card, Integer>();
    int Team1Score = 0;
    int Team2Score = 0;

    public Game(Room room) {
        this.room = room;
    }

    public void GeMsg(String msg, Client cl) {
        if (turn == cl.GetId()) {
            if (!VerifMsg(msg, cl)) {
                return;
            } else {
                ChangeTurn();
            }
        } else {
            //cl.writeClient("It's not your turn");
            cl.writeClient("/ERROR 1");
        }
    }

    private Boolean VerifMsg(String msg, Client cl) {
        if (CardDetail.isACard(msg.replace("\r\n", ""))) {
            if (!cl.GotCard(msg)) {
                //cl.writeClient("You dont have this card !!!");
                cl.writeClient("/ERROR 3");
                return false;
            }
            if (!msg.contains(room.GetAtout()) && cl.GotTrump(room.GetAtout())) {
                //cl.writeClient("Play a card with a suite equal to the trump !!!");
                //cl.writeClient("/ERROR 4")
                return false;
            }
            CardOnTable.put(cl.SendCard(msg), (turn % 2 == 0 ? 1 : 2));
            cl.ErasCard(msg);
            return true;
        }
        //cl.writeClient("Its not a card!!!");
        cl.writeClient("/ERROR 5");
        return false;
    }

    private void ChangeTurn() {
        turn++;
        turn %= 4;
        if (turn == 0) {
            int score = 0;
            int max = 0;
            int team = 0;
            for (Map.Entry<Card, Integer> entree : CardOnTable.entrySet()) {
                if (entree.getKey().GetCard().contains(room.GetAtout())) {
                    score += entree.getKey().GetTrumpVal();
                    if (entree.getKey().GetTrumpVal() > max)
                    {
                        max = entree.getKey().GetTrumpVal();
                        team = entree.getValue();
                    }
                } else {
                    score += entree.getKey().GetVal();
                    if (entree.getKey().GetVal() > max)
                    {
                        max = entree.getKey().GetVal();
                        team = entree.getValue();
                    }
                }
            }
            if (team == 1)
                Team1Score += score;
            else
                Team2Score += score;
            room.SendHandCard();
            //room.SendMsgToAll("Its turn of player => " + turn);
            room.SendMsgToAll("/TURN " + turn);
            CardOnTable.clear();
            if (room.GetClientList().get(turn).GetCardOnHand().size() == 0) {
                if ((Team1Score > Team2Score && Team1Score > room.bet) || (Team1Score < Team2Score && Team2Score < room.bet)) {
                    room.SetTeam1Score(room.GetTeam1Score() + Team1Score + room.GetBet());
                    room.SendMsgToAll("Team1 win this round !!");
                }
                 else {
                    room.SetTeam2Score(room.GetTeam2Score() + Team2Score + room.GetBet());
                    room.SendMsgToAll("Team2 win this round !!");
                }
                room.SendMsgToAll("Team1 => " + room.GetTeam1Score() + " Team2 => " + room.GetTeam2Score());
                room.SeIsOnPlay(false);
                if (room.GetTeam1Score() > 1000 || room.GetTeam2Score() > 1000)
                    room.SendMsgToAll("fini");
                else
                    room.ReRound();
            }
        } else {
            //String str = "Card on table => ";
            String str = "/TABLE ";
            for (Map.Entry<Card, Integer> entree : CardOnTable.entrySet()) {
                str += entree.getKey().GetCard();
                str += " ";
            }
            room.SendMsgToAll(str);
            //room.SendMsgToAll("Its turn of player => " + turn);
            room.SendMsgToAll("/TURN " + turn);
        }
    }
}
