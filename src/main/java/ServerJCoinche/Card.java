package ServerJCoinche;

/**
 * Created by rusig_n on 23/11/2016.
 */
public class Card {

    String card;
    int val;
    int trumpVal;

    public Card(String card, int val, int trumpVal) {
        this.card = card;
        this.val = val;
        this.trumpVal = trumpVal;
    }

    public String GetCard() {
        return card;
    }

    public int GetVal() {
        return val;
    }

    public int GetTrumpVal() {
        return trumpVal;
    }

}
