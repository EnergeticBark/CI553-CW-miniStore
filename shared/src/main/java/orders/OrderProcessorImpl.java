package orders;

import catalogue.Basket;
import middle.DAOException;
import orders.exceptions.OrderException;
import orders.remote.RemoteOrderProcessor;


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
    private final OrderDAO orderDAO;

    private static int nextOrderNumber = 1; // Start at order 1

    public OrderProcessorImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Generates a unique order number
     *   would be good to recycle numbers after 999
     * @return A unique order number
     */
    private synchronized int uniqueNumber() {
        return nextOrderNumber++;
    }

    /**
     * Add a new order to the order processing system
     * @param bought A new order that is to be processed
     */
    @Override
    public synchronized void newOrder(Basket bought) throws OrderException {
        try {
            orderDAO.create(new Order(uniqueNumber(), bought));
        } catch (DAOException e) {
            throw new OrderException(e.getMessage());
        }
    }

    /**
     * Returns an order to pack from the warehouse.
     * @return An order to pack or null if no order
     */
    @Override
    public synchronized Order getOrderToPack() throws OrderException {
        try {
            System.out.println("looking for order");
            for (Order order: orderDAO.getAll()) {
                System.out.println(order.getOrderNumber());
                System.out.println(order.getState());
                if (order.getState() == Order.State.Waiting) {
                    // Found order waiting.
                    order.setState(Order.State.BeingPacked);
                    orderDAO.update(order);
                    return order;
                }
            }
        } catch (DAOException e) {
            throw new OrderException(e.getMessage());
        }
        return null;
    }

    /**
     * Informs the order processing system that the order has been
     * packed and the products are now being delivered to the
     * collection desk
     * @param  orderNum The order that has been packed
     * @return true OrderProcessorImpl in system, false no such order
     */
    @Override
    public synchronized boolean informOrderPacked(int orderNum) throws OrderException {
        try {
            System.out.println("packed WOOO");
            Order order = orderDAO.get(String.valueOf(orderNum));
            if (order.getState() == Order.State.BeingPacked) {
                order.setState(Order.State.ToBeCollected);
                orderDAO.update(order);
            }
            return true;
        } catch (DAOException e) {
            throw new OrderException(e.getMessage());
        }
    }

    /**
     * Informs the order processing system that the order has been
     * collected by the customer
     * @return true If order is in the system, otherwise false
     */
    @Override
    public synchronized boolean informOrderCollected(int orderNum) {
        /*for (int i = 0; i < orderDAO.size(); i++) {
            if (orderDAO.get(i).getOrderNumber() == orderNum
                    && orderDAO.get(i).getState() == Order.State.ToBeCollected) {
                orderDAO.remove(i);
                return true;
            }
        }*/
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
    @Override
    public synchronized Map<String, List<Integer>> getOrderState() {
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
        /*return orderDAO.stream()
                .filter(order -> order.getState() == inState)
                .map(Order::getOrderNumber)
                .collect(Collectors.toList());*/
        return null;
    }
}
