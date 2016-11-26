package ClientJCoinche;


import Commun.TransfertClass;
import java.util.*;
import com.google.common.base.Strings;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ClientGui {

    String          roomId  = "";
    Integer         clientId = -1;
    String          teamId = "";
    Integer         valueTurn = 0;
    String          valueBet[] = new String[2];
    String          playerAction[] = new String[4];
    String          cardHand[] = new String[9];
    String          cardTable[] = new String[6];
    String          dispGui = "";
    Boolean         gameStart = false;

    public ClientGui() {

    }
    private String[] createCardGui(String card) {
        String cardGui[] = new String[5];
        String type = null;
        String suit = null;
        if (card.toCharArray()[0] == '1') {
            type = "10";
            suit = card.substring(2, 3);
        } else {
            type = card.substring(0, 1);
            suit = card.substring(1, 2);
        }
        switch (suit) {
            case "D"    :   suit = "♦";
                            break;
            case "S"    :   suit = "♠";
                            break;
            case "H"    :   suit = "♥";
                            break;
            case "C"    :   suit = "♣";
                            break;
        }
        cardGui[0] = "┌─────┐";
        cardGui[1] = "│" + type + String.format("%"+ (5 - type.length()) +"s", " ") + "│";
        cardGui[2] = "│  " + suit + "  │";
        cardGui[3] = "│" + String.format("%"+ (5 - type.length()) +"s", " ") + type + "│";
        cardGui[4] = "└─────┘";

        return cardGui;
    }

    private String[] createCardInfoTrail(String cardHand[]) {
        String cardInfo[] = new String[3];
        for (Integer z = 0; z < cardInfo.length; z++) {
            cardInfo[z] = "";
        } 
        Integer i = 0;
        while (cardHand[i] != null) {
            cardInfo[0] += "┌──^──┐";
            cardInfo[1] += "│ " + cardHand[i] + String.format("%"+ (4 - cardHand[i].length()) +"s", " ") + "│";
            cardInfo[2] += "└─────┘";
            i++;
        }
        return cardInfo;
    }

    private String[] createCardTrail(String cardHand[]) {
        String cardtrail[] = new String[5];
        for (Integer y = 0; y < cardtrail.length; y++) {
            cardtrail[y] = "";
        }
        Integer i = 0;
        while (cardHand[i] != null) {
            String card[] = createCardGui(cardHand[i]);
            for (Integer z = 0; z < card.length; z++) {
                cardtrail[z] += card[z];
            }
            i++;
        }
        return cardtrail;
    }

    private void displayGameRound(String table[]) {
        String card[] = createCardTrail(cardTable);
        Integer padding = 21 - card[0].length();
        Integer left = (clientId + 1) % 4;
        Integer right = (clientId + 3) % 4;

        table[4] += "     │" + card[0] + Strings.repeat(" ", padding) + "│     ";
        table[5] += "┌──┐ │" + card[1] + Strings.repeat(" ", padding) + "│ ┌──┐";
        table[6] += "│P" + Integer.toString(left) + "│ │" + card[2] + Strings.repeat(" ", padding) + "│ │P" + Integer.toString(right) + "│";
        table[7] += "└──┘ │" + card[3] + Strings.repeat(" ", padding) + "│ └──┘";
        table[8] += "     │" + card[4] + Strings.repeat(" ", padding) + "│     ";
    }

    private void displayBetRound(String table[]) {
        String bet = playerAction[(clientId + 2) % 4];
        table[4] += "     " + "│" + String.format("%8s", " ");
        if (bet == null) {
            table[4] += String.format("%5s", " ");
        } else {
            table[4] += bet;
        }
        table[4] += String.format("%8s", " ") + "│     ";
        table[5] += "┌──┐ │" + String.format("%21s", " ") + "│ ┌──┐";
        Integer left = (clientId + 1) % 4;
        Integer right =(clientId + 3) % 4;
        table[6] += "│P" + Integer.toString(left) + "│ │";
        bet = playerAction[left];
        if (bet == null) {
            table[6] += String.format("%5s", " ");
        } else {
            table[6] += bet;
        }
        table[6] += String.format("%11s", " ");
        bet = playerAction[right];
        if (bet == null) {
            table[6] += String.format("%5s", " "); 
        } else if (bet.toCharArray()[bet.length() - 1] == ' ') {
            table[6] += " " + bet.substring(0, bet.length() - 1);
        } else {
            table[6] += bet;
        }
        table[6] += "│ │P" + Integer.toString(right) + "│";
        table[7] += "└──┘ │" + String.format("%21s", " ") + "│ └──┘";
        table[8] += "     │" + String.format("%21s", " ") + "│     ";
    }
    private String[] displayTable() {
        String line = String.format("%14s", " ");
        String table[] = new String[10];
        for (Integer i = 0; i < table.length; i++) {
            table[i] = "";
        }
        table[0] += line + "┌──┐" + line + " ";
        table[1] += line + "│P" + Integer.toString((clientId + 2) % 4) + "│" + line + " ";
        table[2] += line + "└──┘" + line + " ";
        table[3] += "     ┌─────────────────────┐     ";
        if (!gameStart) {
            displayBetRound(table);
        } else {
            displayGameRound(table);
        }
        table[9] += "     └─────────────────────┘     ";
        return table;
    }
    private void display(String cmd[]) {
        System.out.print("\u001b[2J\u001b[H");
        System.out.flush();

        System.out.println("+" + Strings.repeat("-", 56) + "+");
        String table[] = displayTable();
        for (Integer i = 0; i < table.length; i++) {
            System.out.println("|" + String.format("%11s", " ") + table[i] + Strings.repeat(" ", 12) + "|");
        }

        String hand[] = createCardTrail(cardHand);
        for (Integer i = 0; i < hand.length; i++) {
            Integer l = hand[i].length();
            Integer left = (56 - l) / 2;
            Integer right = 56 - (left + l); 
            System.out.println("|" + Strings.repeat(" ", left) + hand[i] + Strings.repeat(" ", right) + "|");
        }

        String handinfo[] = createCardInfoTrail(cardHand);
        for (Integer i = 0; i < handinfo.length; i++) {
            Integer l = hand[i].length();
            Integer left = (56 - l) / 2;
            Integer right = 56 - (left + l);
            System.out.println("|" + Strings.repeat(" ", left) + handinfo[i] + Strings.repeat(" ", right) + "|");
        }

        System.out.println("+" + Strings.repeat("-", 56) + "+");

        valueTurn = Integer.parseInt(cmd[1]);

        if (valueTurn == clientId) {
           if (gameStart == false) {
               System.out.print("Place your bet => ");
           } else {
               System.out.print("Place your card => ");
           }
        } else {
            System.out.print("Waiting for player " + Integer.toString(valueTurn) + "...");
        }
    }

    private void setBet(String cmd[]) {
        if (cmd[1].equals("FINAL")) {
            gameStart = true;
        }
        if (valueBet[0] == null ||
            valueBet[1] == null ||
        (!(valueBet[0].equals(cmd[2])) || !(valueBet[1].equals(cmd[3])))) {
            valueBet[0] = cmd[2];
            valueBet[1] = cmd[3];
            playerAction[valueTurn] = cmd[2] + " " + cmd[3];
            if (playerAction[valueTurn].length() < 5) {
                playerAction[valueTurn] += " ";
            }
        }
        else {
            playerAction[valueTurn] = "pass ";
        }
    }

    private void setHand(String cmd[]) {
        cardHand[0] = null;
        for (Integer i = 1; i < cmd.length; i++) {
            cardHand[i - 1] = cmd[i];
            cardHand[i] = null;
        }
    }

    private void setTable(String cmd[]) {
        cardTable[0] = null;
        if (cmd.length < 5) {
            for (Integer i = 1; i < cmd.length; i++) {
                cardTable[i - 1] = cmd[i];
                cardTable[i] = null;
            }
        }
    }

    public void ParseMsg( String str) {
        String cmd[] = str.split(" ");
        switch (cmd[0]) {
            case "/ROOM"    :   roomId = cmd[1];
                                break;
            case "/PLAYER"  :   clientId = Integer.parseInt(cmd[1]);
                                break;
            case "/TEAM"    :   teamId = cmd[1];
                                break;
            case "/BET"     :   setBet(cmd);
                                break;
            case "/HAND"    :   setHand(cmd);
                                break;
            case "/TABLE"   :   setTable(cmd);
                                break;
            case "/TURN"    :   display(cmd);
                                break;
            default         :   System.out.print(str + "\n");
                                break;
        }
    }
}