package middle;

import catalogue.Basket;

import java.util.List;
import java.util.Map;

/**
 * Defines the interface for accessing the order processing system.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public interface OrderProcessor {
    // Used by Cashier
    void newOrder(Basket bought) throws OrderException;
    int uniqueNumber() throws OrderException;

    // Used by Packer
    Basket getOrderToPack() throws OrderException;
    boolean informOrderPacked(int orderNum) throws OrderException;

    // Not being used in this version.
    boolean informOrderCollected(int orderNum) throws OrderException; // Collection
    Map<String,List<Integer>> getOrderState() throws OrderException; // Display
}
