package Commun;

import java.io.Serializable;

/**
 * Created by rusig_n on 21/11/2016.
 */

public class TransfertClass implements Serializable {
    public String msg;
    public int id;

    public TransfertClass(String msg, int id)
    {
        this.msg = msg;
        this.id = id;
    }
}
