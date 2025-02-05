package orders.usecases;

import dao.DAOException;
import orders.Order;
import orders.OrderDAO;
import orders.exceptions.OrderInvalidStateException;

/** Use case that changes an order's state to {@code ToBeCollected}, then saves it in the persistence layer. */
public class InformOrderPacked {
    private final OrderDAO orderDAO;

    public InformOrderPacked(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Informs the order processing system that the order has been packed and the products are now being delivered to
     * the collection desk.
     * @param order the order that has been packed
     */
    public void run(Order order) throws OrderInvalidStateException, DAOException {
        if (order.getState() != Order.State.BeingPacked) {
            throw new OrderInvalidStateException("InformOrderPacked: order state was not 'BeingPacked'");
        }

        order.setState(Order.State.ToBeCollected);
        orderDAO.update(order);
    }
}

