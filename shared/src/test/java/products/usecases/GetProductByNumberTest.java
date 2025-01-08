package products.usecases;

import org.junit.jupiter.api.Test;
import products.Product;
import products.exceptions.ProductDoesNotExistException;

import static org.junit.jupiter.api.Assertions.*;

class GetProductByNumberTest {
    @Test
    void doesNotExist() {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "Example Product 1", 24, 0));
        assertThrows(
                ProductDoesNotExistException.class,
                () -> new GetProductByNumber(mockProductDAO).run(0)
        );
    }

    @Test
    void doesExist() {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(14, "Example Product 1", 24, 0));
        assertDoesNotThrow(() -> new GetProductByNumber(mockProductDAO).run(14));
    }
}