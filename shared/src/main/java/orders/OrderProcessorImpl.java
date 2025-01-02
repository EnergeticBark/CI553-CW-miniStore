package orders;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.OrderException;
import middle.OrderProcessor;
import remote.RemoteOrderProcessor;

import java.util.stream.Collectors;

import java.util.*;

/**
 * The order processing system.<BR>
 * Manages the progression of customer orders, 
 *  instances of a Basket as they are progressed through the system.
 * These stages are:
 * <BR><B>Waiting to be processed<BR>
 * Currently being packed<BR>
 * Waiting to be collected<BR></B>
 * @author  Mike Smith University of Brighton
 * @version 3.0
 */
public class OrderProcessorImpl implements OrderProcessor, RemoteOrderProcessor {
    // Active orders in the miniStore system
    private final ArrayList<Order> orders = new ArrayList<>();
    private static int nextOrderNumber = 1; // Start at order 1

    /**
     * Used to generate debug information
     * @param  basket an instance of a basket
     * @return Description of contents
     */
    private String asString(Basket basket) {
        StringBuilder sb = new StringBuilder(1024);
        Formatter fr = new Formatter(sb);
        fr.format("#%d (", basket.getOrderNum());
        for (Product pr: basket) {
            fr.format("%-15.15s: %3d ", pr.getDescription(), pr.getQuantity());
        }
        fr.format(")");
        fr.close();
        return sb.toString();
    }

    /**
     * Generates a unique order number
     *   would be good to recycle numbers after 999
     * @return A unique order number
     */
    public synchronized int uniqueNumber() throws OrderException {
        return nextOrderNumber++;
    }

    /**
     * Add a new order to the order processing system
     * @param bought A new order that is to be processed
     */
    public synchronized void newOrder(Basket bought) throws OrderException {
        DEBUG.trace("DEBUG: New order");
        orders.add(new Order(bought));
        for (Order order: orders) {
            DEBUG.trace("OrderProcessorImpl: " + asString(order.getBasket()));
        }
    }

    /**
     * Returns an order to pack from the warehouse.
     * @return An order to pack or null if no order
     */
    public synchronized Basket getOrderToPack() throws OrderException {
        DEBUG.trace("DEBUG: Get order to pack");
        Basket foundWaiting = null;
        for (Order order: orders) {
            if (order.getState() == Order.State.Waiting) {
                foundWaiting = order.getBasket();
                order.setState(Order.State.BeingPacked);
                break;
            }
        }
        return foundWaiting;
    }

    /**
     * Informs the order processing system that the order has been
     * packed and the products are now being delivered to the
     * collection desk
     * @param  orderNum The order that has been packed
     * @return true OrderProcessorImpl in system, false no such order
     */
    public synchronized boolean informOrderPacked(int orderNum) throws OrderException {
        DEBUG.trace("DEBUG: OrderProcessorImpl packed [%d]", orderNum);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getBasket().getOrderNum() == orderNum
                    && orders.get(i).getState() == Order.State.BeingPacked) {
                orders.get(i).setState(Order.State.ToBeCollected);
                return true;
            }
        }
        return false;
    }

    /**
     * Informs the order processing system that the order has been
     * collected by the customer
     * @return true If order is in the system, otherwise false
     */
    public synchronized boolean informOrderCollected(int orderNum) throws OrderException {
        DEBUG.trace("DEBUG: OrderProcessorImpl collected [%d]", orderNum);
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getBasket().getOrderNum() == orderNum
                    && orders.get(i).getState() == Order.State.ToBeCollected) {
                orders.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns information about all the orders (there order number)
     * in the order processing system
     * This consists of a map with the following keys:
     *<PRE>
     * Key "Waiting"        a list of orders waiting to be processed
     * Key "BeingPacked"    a list of orders that are currently being packed
     * Key "ToBeCollected"  a list of orders that can now be collected
     * Associated with each key is a List&lt;Integer&gt; of order numbers.
     * Note: Each order number will be unique number.
     * </PRE>
     * @return a Map with the keys: "Waiting", "BeingPacked", "ToBeCollected"
     */
    public synchronized Map<String, List<Integer>> getOrderState() throws OrderException {
        //DEBUG.trace( "DEBUG: get state of order system" );
        Map<String, List<Integer>> res = new HashMap<>();

        res.put("Waiting", orderNums(Order.State.Waiting));
        res.put("BeingPacked", orderNums(Order.State.BeingPacked));
        res.put("ToBeCollected", orderNums(Order.State.ToBeCollected));

        return res;
    }

    /**
     * Return the list of order numbers in selected state
     * @param inState The state to find order numbers in
     * @return A list of order numbers
     */
    private List<Integer> orderNums(Order.State inState) {
        return orders.stream()
                .filter(order -> order.getState() == inState)
                .map(order -> order.getBasket().getOrderNum())
                .collect(Collectors.toList());
    }
}
