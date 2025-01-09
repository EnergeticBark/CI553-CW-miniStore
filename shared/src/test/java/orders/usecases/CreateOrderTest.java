package orders.usecases;

import catalogue.Basket;
import dao.DAOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrderTest {
    @Test
    void create() throws DAOException {
        MockOrderDAO mockOrderDAO = new MockOrderDAO();
        Basket basket = new Basket();
        new CreateOrder(mockOrderDAO).run(basket);

        assertDoesNotThrow(() -> mockOrderDAO.get(1));
    }
}