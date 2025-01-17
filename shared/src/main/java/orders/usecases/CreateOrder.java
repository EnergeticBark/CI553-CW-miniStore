package orders.usecases;

import catalogue.Basket;
import dao.DAOException;
import orders.Order;
import orders.OrderDAO;

/** Use case that adds a new order to the persistence layer. */
public class CreateOrder {
    private final OrderDAO orderDAO;

    public CreateOrder(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Adds a new order to the persistence layer.
     * @param bought the basket containing the products that were ordered.
     */
    public void run(Basket bought) throws DAOException {
        int orderNumber = orderDAO.getNextOrderNumber();
        orderDAO.create(new Order(orderNumber, bought));
    }
}
