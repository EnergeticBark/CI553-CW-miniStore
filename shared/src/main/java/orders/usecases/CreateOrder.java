package orders.usecases;

import catalogue.Basket;
import middle.DAOException;
import orders.Order;
import orders.OrderDAO;

public class CreateOrder {
    private final OrderDAO orderDAO;

    private static int nextOrderNumber = 1; // Start at order 1

    /**
     * Generates a unique order number
     *   would be good to recycle numbers after 999
     * @return A unique order number
     */
    private synchronized int uniqueNumber() {
        return nextOrderNumber++;
    }

    public CreateOrder(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Add a new order to the order processing system
     * @param bought A new order that is to be processed
     */
    public void run(Basket bought) throws DAOException {
        orderDAO.create(new Order(uniqueNumber(), bought));
    }
}
