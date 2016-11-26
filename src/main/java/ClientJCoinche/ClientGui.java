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
    String          valueTurn = "0";
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

    private void displayTop() {
        //String disp = "";
        String line = String.format("%14s", " ");
        dispGui += line + "┌──┐\n";
        dispGui += line + "│P" + Integer.toString((clientId + 2) % 4) + "│\n";
        dispGui += line + "└──┘\n";
        //System.out.print(dispGui);
    }
    private void displayMiddle() {
        dispGui += "     ┌─────────────────────┐\n";
        if (!gameStart) {
            String bet = playerAction[(clientId + 2) % 4];
            dispGui += "     " + "│" + String.format("%8s", " ");
            if (bet == null) {
                dispGui += String.format("%5s", " ");
            }
            else {
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
            } else if (bet.toCharArray()[0] == ' ') {
                dispGui += bet.substring(1) + " ";
            } else {
                dispGui += bet;
            }
            dispGui += String.format("%11s", " ");
            bet = playerAction[right];
            if (bet == null) {
               dispGui += String.format("%5s", " ");
            } else {
                dispGui += bet;
            }
            dispGui += "│ │P" + Integer.toString(right) + "│\n";
            dispGui += "└──┘ │" + String.format("%21s", " ") + "│ └──┘\n";
            dispGui += "     │" + String.format("%21s", " ") + "│\n";
            dispGui += "     └─────────────────────┘\n";

        }
    }
    private void display() {
        dispGui = "";
        System.out.print("\u001b[2J\u001b[H");
        System.out.flush();

        displayTop();
        displayMiddle();
        System.out.print(dispGui);
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
            playerAction[Integer.parseInt(valueTurn)] = cmd[2] + " " + cmd[3];
            if (playerAction[Integer.parseInt(valueTurn)].length() < 5) {
                playerAction[Integer.parseInt(valueTurn)] += " ";
            }
        }
        else {
            playerAction[Integer.parseInt(valueTurn)] = "pass ";
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
            case "/TURN"    :   display();
                                valueTurn = cmd[1];
                                //System.out.print("TURN => " + cmd[1] + "\n");
                                break;
            default         :   System.out.print(str + "\n");
                                break;
        }
        // for (String val: cardHand) {
        //     System.out.print(val + "\n");
        // }
    }

}