package products.usecases;

import dao.DAOException;
import org.junit.jupiter.api.Test;
import products.Product;
import products.exceptions.ProductDoesNotExistException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetProductsBySearchTest {

    @Test
    void findOne() throws DAOException, ProductDoesNotExistException {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "40 inch LED HD TV", 269.00, 1));
        mockProductDAO.create(new Product(2, "DAB Radio", 29.99, 1));
        List<Product> results = new GetProductsBySearch(mockProductDAO).run("tv");
        assertEquals(1, results.size());
    }

    @Test
    void findNone() {
        MockProductDAO mockProductDAO = new MockProductDAO();
        mockProductDAO.create(new Product(1, "40 inch LED HD TV", 269.00, 1));
        mockProductDAO.create(new Product(2, "DAB Radio", 29.99, 1));
        assertThrows(
                ProductDoesNotExistException.class,
                () -> new GetProductsBySearch(mockProductDAO).run("mp3")
        );
    }
}