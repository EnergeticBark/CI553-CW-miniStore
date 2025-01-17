package orders.usecases;

import dao.DAOException;
import orders.Order;
import orders.OrderDAO;

import java.util.Optional;

/** Use case that retrieves an order to pack from the persistence layer. */
public class GetOrderToPack {
    private final OrderDAO orderDAO;

    public GetOrderToPack(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /** {@return An order ready to be packed or null if no order are waiting} */
    public Order run() throws DAOException {
        Optional<Order> unpackedOrder = orderDAO.getAll()
                .stream()
                .filter((order) -> order.getState() == Order.State.Waiting)
                .findAny();
        if (unpackedOrder.isEmpty()) {
            return null;
        }

        // Found an order waiting.
        Order order = unpackedOrder.get();
        order.setState(Order.State.BeingPacked);
        orderDAO.update(order);
        return order;
    }
}
