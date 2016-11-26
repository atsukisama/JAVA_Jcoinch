package ServerJCoinche;

import java.util.*;

/**
 * Created by rusig_n on 23/11/2016.
 */
public class Game {

    int turn = 0;
    Room room;
    String firstCard = "";
    int ctp = 0;
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
    
    private String GetSuite(String msg) {
        if (msg.toCharArray()[0] == '1') {
            return msg.substring(2, 3);
        }
        return msg.substring(1, 2);
    }

    private Boolean VerifMsg(String msg, Client cl) {
        if (CardDetail.isACard(msg.replace("\r\n", ""))) {
            if (!cl.GotCard(msg)) {
                //cl.writeClient("You dont have this card !!!");
                cl.writeClient("/ERROR 3");
                return false;
            }
            if (CardOnTable.size() != 0) {
                if ((!GetSuite(msg).equals(GetSuite(firstCard)) && !GetSuite(msg).contains(room.GetAtout()))) {
                    if ((cl.GotTrump(GetSuite(firstCard)) || cl.GotTrump(room.GetAtout()))) {
                        cl.writeClient("/ERROR 4");
                        return false;
                    }
                }
            }
            else
                firstCard = msg;
            CardOnTable.put(cl.SendCard(msg), turn);
            cl.ErasCard(msg);
            return true;
        }
        //cl.writeClient("Its not a card!!!");
        cl.writeClient("/ERROR 5");
        return false;
    }

    private void ChangeTurn() {
        turn++;
        ctp++;
        turn %= 4;
        System.out.print("\n\nCTP = " + ctp + "\n\n");
        if (ctp == 4) {
            ctp = 0;
            firstCard = "";
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
            turn = team;
            if ((team % 2 == 0 ? 1 : 2) == 1)
                Team1Score += score;
            else
                Team2Score += score;
            room.SendHandCard();
            String str = "/TABLE ";
            for (Map.Entry<Card, Integer> entree : CardOnTable.entrySet()) {
                str += entree.getKey().GetCard();
                str += " ";
            }
            room.SendMsgToAll(str);
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
                room.SendMsgToAll("/RESTART");
                room.SeIsOnPlay(false);
                if (room.GetTeam1Score() > 100 || room.GetTeam2Score() > 100)
                    room.SendMsgToAll("fini");
                else
                    room.ReRound();
            }
        } else {
            String str = "/TABLE ";
            for (Map.Entry<Card, Integer> entree : CardOnTable.entrySet()) {
                str += entree.getKey().GetCard();
                str += " ";
            }
            room.SendMsgToAll(str);
            room.SendHandCard();
            room.SendMsgToAll("/TURN " + turn);
        }
    }
}
