package remote;

import dbAccess.StockRW;
import middle.StockException;

import java.rmi.RemoteException;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

/**
 * Implements Read/Write access to the stock list,
 * the stock list is held in a relational DataBase.
 * @author  Mike Smith University of Brighton
 * @version 2.1
 */
public class R_StockRW extends StockRW implements RemoteStockRW_I {

    /**
     * All transactions are done via StockRW to ensure
     * that a single connection to the database is used for all transactions
     * @throws java.rmi.RemoteException if issue
     * @throws middle.StockException if issue
     */
    public R_StockRW() throws RemoteException, StockException {
        super();
    }
}
