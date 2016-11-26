package ClientJCoinche;


import Commun.TransfertClass;
import java.util.*;
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
    String          cardTable[] = new String[5];
    String          dispGui = "";
    Boolean         gameStart = false;
    // List<String>    cardHand = new ArrayList<String>();
    // List<String>    cardTable = new ArrayList<String>();

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
    private void displayTop() {
        //String disp = "";
        String line = String.format("%14s", " ");
        dispGui += line + "┌──┐\n";
        dispGui += line + "│P" + Integer.toString((clientId + 2) % 4) + "│\n";
        dispGui += line + "└──┘\n";
        //System.out.print(dispGui);
    }
    private void displayBetRound() {
        String bet = playerAction[(clientId + 2) % 4];
        dispGui += "     " + "│" + String.format("%8s", " ");
        if (bet == null) {
            dispGui += String.format("%5s", " ");
        } else {
            dispGui += bet;
        }
        dispGui += String.format("%8s", " ") + "│\n";
        dispGui += "┌──┐ │" + String.format("%21s", " ") + "│ ┌──┐\n";
        Integer left = (clientId + 1) % 4;
        Integer right =(clientId + 3) % 4;
        dispGui += "│P" + Integer.toString(left) + "│ │";
        bet = playerAction[left];
        if (bet == null) {
            dispGui += String.format("%5s", " ");
        } else {
            dispGui += bet;
        }
        dispGui += String.format("%11s", " ");
        bet = playerAction[right];
        if (bet == null) {
            dispGui += String.format("%5s", " "); 
        } else if (bet.toCharArray()[bet.length() - 1] == ' ') {
            dispGui += " " + bet.substring(0, bet.length() - 1);
        } else {
            dispGui += bet;
        }
        dispGui += "│ │P" + Integer.toString(right) + "│\n";
        dispGui += "└──┘ │" + String.format("%21s", " ") + "│ └──┘\n";
        dispGui += "     │" + String.format("%21s", " ") + "│\n";
    }
    private void displayMiddle() {
        dispGui += "     ┌─────────────────────┐\n";
        if (!gameStart) {
            displayBetRound();
        }
        dispGui += "     └─────────────────────┘\n";
    }
    private void display(String cmd[]) {
        dispGui = "";
        System.out.print("\u001b[2J\u001b[H");
        System.out.flush();

        displayTop();
        displayMiddle();
        System.out.print(dispGui);
        valueTurn = Integer.parseInt(cmd[1]);

        System.out.println(Arrays.toString(cardHand));
        String hand[] = createCardTrail(cardHand);
        for (Integer i = 0; i < hand.length; i++) {
            System.out.println(hand[i]);
        }

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
        for (Integer i = 1; i < cmd.length; i++) {
            cardHand[i - 1] = cmd[i];
        }
    }

    public void ParseMsg( String str) {
        //System.out.print("server sent => " + str + "\n");
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
            case "/TURN"    :   display(cmd);
                                break;
            default         :   System.out.print(str + "\n");
                                break;
        }
        // for (String val: cardHand) {
        //     System.out.print(val + "\n");
        // }
    }

}