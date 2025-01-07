package orders;

import catalogue.Basket;
import orders.exceptions.OrderException;

/**
 * Defines the interface for accessing the order processing system.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public interface OrderProcessor {
    // Used by Cashier
    void newOrder(Basket bought) throws OrderException;

    // Used by Packer
    Order getOrderToPack() throws OrderException;
    void informOrderPacked(int orderNum) throws OrderException;
}
