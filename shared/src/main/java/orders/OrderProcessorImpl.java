package orders;

import catalogue.Basket;
import middle.DAOException;
import orders.exceptions.OrderException;
import orders.remote.RemoteOrderProcessor;
import orders.usecases.CreateOrder;
import orders.usecases.GetOrderToPack;
import orders.usecases.InformOrderPacked;

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

    public OrderProcessorImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Add a new order to the order processing system
     * @param bought A new order that is to be processed
     */
    @Override
    public synchronized void newOrder(Basket bought) throws OrderException {
        try {
            new CreateOrder(orderDAO).run(bought);
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
            return new GetOrderToPack(orderDAO).run();
        } catch (DAOException e) {
            throw new OrderException(e.getMessage());
        }
    }

    /**
     * Informs the order processing system that the order has been
     * packed and the products are now being delivered to the
     * collection desk
     * @param  orderNum The order that has been packed
     */
    @Override
    public synchronized void informOrderPacked(int orderNum) throws OrderException {
        try {
            Order order = orderDAO.get(String.valueOf(orderNum));
            new InformOrderPacked(orderDAO).run(order);
        } catch (DAOException e) {
            throw new OrderException(e.getMessage());
        }
    }
}
