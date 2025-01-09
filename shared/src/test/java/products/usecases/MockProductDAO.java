package products.usecases;

import dao.DAOException;
import products.Product;
import products.ProductDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockProductDAO implements ProductDAO {
    List<Product> products = new ArrayList<>();

    public boolean exists(int identifier) {
        return products.stream().anyMatch(product -> product.getProductNumber() == identifier);
    }

    @Override
    public List<Product> search(String searchQuery)  {
        return products.stream()
                .filter((product) -> product.getDescription()
                        .toUpperCase()
                        .contains(searchQuery.toUpperCase()))
                .collect(Collectors.toList());
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
}
