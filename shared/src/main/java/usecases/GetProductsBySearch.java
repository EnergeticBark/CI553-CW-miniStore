package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.ProductDAO;
import middle.DAOException;

import java.util.List;

public class GetProductsBySearch {
    private final ProductDAO stockDAO;

    public GetProductsBySearch(ProductDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    public List<Product> run(String productNumber) throws ProductDoesNotExistException, DAOException {
        List<Product> products = stockDAO.search(productNumber);
        if (products.isEmpty()) {
            throw new ProductDoesNotExistException();
        }
        return products;
    }
}
