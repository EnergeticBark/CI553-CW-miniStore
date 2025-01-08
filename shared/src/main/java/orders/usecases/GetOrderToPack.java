package orders.usecases;

import dao.DAOException;
import orders.Order;
import orders.OrderDAO;

import java.util.Optional;

public class GetOrderToPack {
    private final OrderDAO orderDAO;

    public GetOrderToPack(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Returns an order to pack from the warehouse.
     * @return An order to pack or null if no order
     */
    public Order run() throws DAOException {
        Optional<Order> unpackedOrder = orderDAO.getAll()
                .stream()
                .filter((order) -> order.getState() == Order.State.Waiting)
                .findAny();
        if (unpackedOrder.isEmpty()) {
            return null;
        }

        // Found order waiting.
        Order order = unpackedOrder.get();
        order.setState(Order.State.BeingPacked);
        orderDAO.update(order);
        return order;
    }
}
