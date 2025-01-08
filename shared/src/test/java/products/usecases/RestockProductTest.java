package products.usecases;

import dao.DAOException;
import org.junit.jupiter.api.Test;
import products.Product;
import products.exceptions.ProductDoesNotExistException;

import static org.junit.jupiter.api.Assertions.*;

class RestockProductTest {

    @Test
    void doesNotExist() {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "Example Product 1", 24, 0));
        assertThrows(
                ProductDoesNotExistException.class,
                () -> new RestockProduct(mockProductDAO).run(45, 1)
        );
    }

    @Test
    void restockFromZero() throws DAOException, ProductDoesNotExistException {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "Example Product 1", 24, 0));
        Product product = new RestockProduct(mockProductDAO).run(1, 50);
        assertEquals(50, product.getQuantity());
    }

    @Test
    void restock() throws DAOException, ProductDoesNotExistException {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "Example Product 1", 24, 25));
        Product product = new RestockProduct(mockProductDAO).run(1, 45);
        assertEquals(70, product.getQuantity());
    }
}