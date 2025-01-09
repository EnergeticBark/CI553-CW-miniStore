package products.usecases;

import dao.DAOException;
import org.junit.jupiter.api.Test;
import products.Product;
import products.exceptions.ProductOutOfStockException;

import static org.junit.jupiter.api.Assertions.*;

class BuyStockTest {

    @Test
    void buyMultiple() throws DAOException, ProductOutOfStockException {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "40 inch LED HD TV", 269.00, 50));
        mockProductDAO.create(new Product(2, "DAB Radio", 29.99, 1));
        Product product = new BuyStock(mockProductDAO).run(1, 15);
        assertEquals(15, product.getQuantity());
    }

    @Test
    void outOfStock() {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "40 inch LED HD TV", 269.00, 50));
        mockProductDAO.create(new Product(2, "DAB Radio", 29.99, 1));
        assertThrows(
                ProductOutOfStockException.class,
                () -> new BuyStock(mockProductDAO).run(1, 65)
        );
    }

    @Test
    void correctRemaining() throws ProductOutOfStockException, DAOException {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "40 inch LED HD TV", 269.00, 50));
        mockProductDAO.create(new Product(2, "DAB Radio", 29.99, 1));
        Product _ = new BuyStock(mockProductDAO).run(1, 15);

        int remaining = mockProductDAO.get(1).getQuantity();
        assertEquals(35, remaining);
    }
}