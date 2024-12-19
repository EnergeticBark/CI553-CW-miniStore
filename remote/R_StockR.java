package remote;

import dbAccess.StockR;
import middle.StockException;

import java.rmi.RemoteException;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

/**
 * Implements Read access to the stock list,
 * the stock list is held in a relational DataBase.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class R_StockR extends StockR implements RemoteStockR_I {
    public R_StockR() throws RemoteException, StockException {
        super();
    }
}
