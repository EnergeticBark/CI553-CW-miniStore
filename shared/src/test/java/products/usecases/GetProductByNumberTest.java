package products.usecases;

import dao.DAOException;
import org.junit.jupiter.api.Test;
import products.Product;
import products.ProductDAO;
import products.exceptions.ProductDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GetProductByNumberTest {
    ProductDAO mockProductDAO = new ProductDAO() {
        List<Product> products = new ArrayList<>();

        public boolean exists(int identifier) {
            return products.stream().anyMatch(product -> product.getProductNumber() == identifier);
        }

        @Override
        public List<Product> search(String searchQuery)  {
            return List.of();
        }

        @Override
        public Product get(int identifier) throws DAOException {
            Optional<Product> found = products.stream()
                    .filter(product -> product.getProductNumber() == identifier)
                    .findFirst();
            if (found.isEmpty()) {
                throw new DAOException("get: No such product.");
            }
            return found.get();
        }

        @Override
        public void create(Product newEntity) {
            products.add(newEntity);
        }

        @Override
        public void update(Product replacement) {
            products = products.stream()
                    .filter(product -> product.getProductNumber() != replacement.getProductNumber())
                    .collect(Collectors.toList());
            products.add(replacement);
        }
    };


    @Test
    void doesNotExist() throws DAOException {
        mockProductDAO.create(new Product(1, "Example Product 1", 24, 0));
        assertThrows(
                ProductDoesNotExistException.class,
                () -> new GetProductByNumber(mockProductDAO).run(0)
        );
    }

    @Test
    void doesExist() throws DAOException {
        mockProductDAO.create(new Product(14, "Example Product 1", 24, 0));
        assertDoesNotThrow(() -> new GetProductByNumber(mockProductDAO).run(14));
    }
}