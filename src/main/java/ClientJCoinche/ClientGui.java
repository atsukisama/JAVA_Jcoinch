package ClientJCoinche;


import Commun.TransfertClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public class ClientGui {

    String          roomId  = "";
    String          clientId = "";
    String          teamId = "";
    String          valueTurn = "0";
    String          valueBet[] = new String[2];
    String          playerAction[] = new String[4];
    String          dispGui = "";
    // List<String>    cardHand = new ArrayList<String>();
    // List<String>    cardTable = new ArrayList<String>();

    public ClientGui() {

    }

    private void displayTop() {
        //String disp = "";
        String line = String.format("%14s", " ");
        dispGui += line + "┌──┐\n";
        dispGui += line + "│P" + Integer.toString((Integer.parseInt(clientId) + 2) % 4) + "│\n";
        dispGui += line + "└──┘\n";
        //System.out.print(dispGui);
    }
    private void displayMiddle() {
        dispGui += "     ┌─────────────────────┐\n";
    }
    private void display() {
        dispGui = "";
        System.out.print("\u001b[2J\u001b[H");
        System.out.flush();

        //System.out.print(playerAction.toString());
        //System.out.println(Arrays.toString(playerAction));
        // System.out.print("Actual bet => " + valueBet[0] + " " + valueBet[1] + "\n");
        // while (i < 4) {
        //     System.out.print("Player " + Integer.toString(i) + " => " + playerAction[i] + "\n");
        //     i++;
        // }
        //System.out.print("Display\n");
        displayTop();
        displayMiddle();
        System.out.print(dispGui);
    }

    private void setBet(String cmd[]) {
        if (valueBet[0] == null ||
            valueBet[1] == null ||
        (!(valueBet[0].equals(cmd[2])) || !(valueBet[1].equals(cmd[3])))) {
            valueBet[0] = cmd[2];
            valueBet[1] = cmd[3];
            playerAction[Integer.parseInt(valueTurn)] = cmd[2] + " " + cmd[3];
        }
        else {
            playerAction[Integer.parseInt(valueTurn)] = "pass";
        }
    }

    public void ParseMsg( String str) {
        //System.out.print("server sent => " + str + "\n");
        String cmd[] = str.split(" ");
        switch (cmd[0]) {
            case "/ROOM"    :   roomId = cmd[1];
                                break;
            case "/PLAYER"  :   clientId = cmd[1];
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