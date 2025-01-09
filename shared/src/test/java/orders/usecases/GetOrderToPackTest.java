package orders.usecases;

import catalogue.Basket;
import dao.DAOException;
import orders.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GetOrderToPackTest {

    @Test
    void noneInQueue() throws DAOException {
        MockOrderDAO mockOrderDAO = new MockOrderDAO();
        assertNull(new GetOrderToPack(mockOrderDAO).run());
    }

    @Test
    void noneWaitingInQueue() throws DAOException {
        MockOrderDAO mockOrderDAO = new MockOrderDAO();
        mockOrderDAO.create(new Order(1, Order.State.BeingPacked, new Basket()));
        mockOrderDAO.create(new Order(2, Order.State.ToBeCollected, new Basket()));
        assertNull(new GetOrderToPack(mockOrderDAO).run());
    }

    @Test
    void oneWaitingInQueue() throws DAOException {
        MockOrderDAO mockOrderDAO = new MockOrderDAO();
        mockOrderDAO.create(new Order(1, Order.State.BeingPacked, new Basket()));
        mockOrderDAO.create(new Order(2, Order.State.ToBeCollected, new Basket()));
        mockOrderDAO.create(new Order(3, Order.State.Waiting, new Basket()));
        assertNotNull(new GetOrderToPack(mockOrderDAO).run());
    }
}