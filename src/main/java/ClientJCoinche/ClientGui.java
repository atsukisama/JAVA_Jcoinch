package ClientJCoinche;


import Commun.TransfertClass;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    String          scoreTeam = "0";
    String          error = "";
    String          valueBet[] = new String[3];
    String          playerAction[] = new String[4];
    String          cardHand[] = new String[9];
    String          cardTable[] = new String[6];
    String          dispGui = "";
    Boolean         gameStart = false;

    public ClientGui() {

    }

    private String[] messageToBox(String message) {
        String box[] = new String[3];

        box[0] = "┌" + Strings.repeat("─", message.length()) + "┐";
        box[1] = "│" + message + "│";
        box[2] = "└" + Strings.repeat("─", message.length()) + "┘";

        return box;
    }

    private String suitSymbol(String card) {
        String suit = null;
        switch (card) {
            case "D"    :   suit = "♦";
                            break;
            case "S"    :   suit = "♠";
                            break;
            case "H"    :   suit = "♥";
                            break;
            case "C"    :   suit = "♣";
                            break;
        }
        return suit;
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

    private String[] infoAtout() {
        String box[] = new String[2];
        box[0] = valueBet[2] + " : " + valueBet[0] + suitSymbol(valueBet[1]) + "|";
        box[1] = Strings.repeat("-", (box[0].length() - 1)) + "+";
        return box;
    }

    private void displayTop() {
        String info[] = messageToBox(" Player " + clientId + " | Team " + teamId + " ");
        String logo[] = messageToBox(" JCOINCHE ");
        String score[] = messageToBox(" Score : " + scoreTeam + " ");
        Integer left = ((58 - logo[0].length()) / 2) - info[0].length() - 1;
        if (left < 0) {
            left = 0;
        }
        Integer right = 58 - (logo[0].length() + info[0].length() + left);
        right -= score[0].length();
        if (right < 0) {
            right = 0;
        }

        for (Integer i = 0; i < info.length; i++) {
            System.out.println(info[i] + Strings.repeat(" ", left) + logo[i] + Strings.repeat(" ", right) + score[i]);
        }
    }

    private void display(String cmd[]) {
        System.out.print("\u001b[2J\u001b[H");
        System.out.flush();
        displayTop();
        System.out.println("+" + Strings.repeat("-", 56) + "+");
        String table[] = displayTable();
        Integer z = 0;
        for (Integer i = 0; i < table.length; i++) {
            if (!gameStart) {
                System.out.println("|" + String.format("%11s", " ") + table[i] + Strings.repeat(" ", 12) + "|");
            } else if (gameStart && z < 2) {
                String infoBoxAtout[] = infoAtout();
                System.out.println("|" + infoBoxAtout[z] + Strings.repeat(" ", 11 - infoBoxAtout[z].length()) + table[i] + Strings.repeat(" ", 12) + "|");
                z++;
            } else {
                System.out.println("|" + String.format("%11s", " ") + table[i] + Strings.repeat(" ", 12) + "|");
            }
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
    }

    private void displayPrompt() {
        if (valueTurn == clientId) {
           if (gameStart == false) {
               System.out.print(error + "[Place your bet]> ");
           } else {
               System.out.print(error + "[Place your card]> ");
           }
        } else {
            String message = "[Waiting for Player " + Integer.toString(valueTurn) + "...]";
            Integer padding = (58 - message.length()) / 2;
            System.out.print(Strings.repeat(" ", padding) + message + Strings.repeat(" ", padding));
        }
    }

    private void displayWin(String cmd[]) {
        System.out.print("\u001b[2J\u001b[H");
        System.out.flush();
        displayTop();
        String teamWin[] = messageToBox(" TEAM " + cmd[1] + " WIN ");
        String nextRound[] = messageToBox(" THANKS FOR PLAYING ");
        System.out.println("+" + Strings.repeat("-", 56) + "+");
        for (Integer i = 0; i < 6; i++) {
            System.out.println("|" + Strings.repeat(" ", 56) + "|");
        }
        Integer padding = (56 - teamWin[0].length()) / 2;
        for (Integer i = 0; i < teamWin.length; i++) {
            System.out.println("|" + Strings.repeat(" ", padding) + teamWin[i] + Strings.repeat(" ", padding) + "|");
        }
        padding = (56 - nextRound[0].length()) / 2;
        for (Integer i = 0; i < nextRound.length; i++) {
            System.out.println("|" + Strings.repeat(" ", padding) + nextRound[i] + Strings.repeat(" ", padding) + "|");
        }
        for (Integer i = 0; i < 6; i++) {
            System.out.println("|" + Strings.repeat(" ", 56) + "|");
        }
        System.out.println("+" + Strings.repeat("-", 56) + "+");
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
            valueBet[2] = "P" + Integer.toString(valueTurn);
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
        } else {
            System.out.println(Arrays.toString(cmd));
        }
    }

    private void setScore(String cmd[]) {
        if (cmd[1].equals(teamId)) {
            scoreTeam = cmd[2];
        }
    }

    private void setError(String cmd[]) {
        String message = "";
        switch (cmd[1]) {
            case "2"    :   message = "[Wrong bet]";   
                            break;
            case "3"    :   message = "[Card not in hand]";
                            break;
            case "4"    :   message = "[Can't play that]";
                            break;
        }
        error = "\033[1;31m" + message + "\033[0m";
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
                                valueTurn = Integer.parseInt(cmd[1]);
                                displayPrompt();
                                break;
            case "/QUIT"    :   ClientJCoinche.closeClient();
                                break;
            case "/SCORE"   :   setScore(cmd);
                                break;
            case "/WIN"     :   displayWin(cmd);
                                break;
            case "/ERROR"   :   setError(cmd);
                                display(cmd);
                                displayPrompt();
                                error = "";
                                break;
            case "/RESTART" :   gameStart = false;
                                for (Integer i = 0; i < 4; i++) {
                                    playerAction[i] = null;
                                }
                                break;
            default         :   System.out.print(str + "\n");
                                break;
        }
    }
}