package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.DAO;
import middle.StockException;

import java.util.List;

public class GetProductsBySearch {
    private final DAO<Product> stockDAO;

    public GetProductsBySearch(DAO<Product> stockDAO) {
        this.stockDAO = stockDAO;
    }

    public List<Product> run(String productNumber) throws ProductDoesNotExistException, StockException {
        List<Product> products = stockDAO.search(productNumber);
        if (products.isEmpty()) {
            throw new ProductDoesNotExistException();
        }
        return products;
    }
}
