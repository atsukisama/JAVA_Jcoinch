package ServerJCoinche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rusig_n on 24/11/2016.
 */
public class CardDetail {

    static HashMap<String, ArrayList<String>> mapCard = new HashMap<String, ArrayList<String>>();
    static List<String> allCard = new ArrayList<String>();
    static int test = 0;

    public CardDetail()
    {

    }

    public static void InitCardDetail() {
        allCard.add("7S");
        allCard.add("8S");
        allCard.add("9S");
        allCard.add("10S");
        allCard.add("VS");
        allCard.add("DS");
        allCard.add("RS");
        allCard.add("AS");
        allCard.add("7D");
        allCard.add("8D");
        allCard.add("9D");
        allCard.add("10D");
        allCard.add("VD");
        allCard.add("DD");
        allCard.add("RD");
        allCard.add("AD");
        allCard.add("7H");
        allCard.add("8H");
        allCard.add("9H");
        allCard.add("10H");
        allCard.add("VH");
        allCard.add("DH");
        allCard.add("RH");
        allCard.add("AH");
        allCard.add("7C");
        allCard.add("8C");
        allCard.add("9C");
        allCard.add("10C");
        allCard.add("VC");
        allCard.add("DC");
        allCard.add("RC");
        allCard.add("AC");
    }

    public static boolean isACard(String card) {
        return allCard.contains(card);
    }
}
