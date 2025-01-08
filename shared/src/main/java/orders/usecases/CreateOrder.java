package orders.usecases;

import catalogue.Basket;
import dao.DAOException;
import orders.Order;
import orders.OrderDAO;

public class CreateOrder {
    private final OrderDAO orderDAO;

    public CreateOrder(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Add a new order to the order processing system
     * @param bought A new order that is to be processed
     */
    public void run(Basket bought) throws DAOException {
        int orderNumber = orderDAO.getNextOrderNumber();
        orderDAO.create(new Order(orderNumber, bought));
    }
}
