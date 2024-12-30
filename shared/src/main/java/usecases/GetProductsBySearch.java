package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.StockException;
import middle.StockDAO;

import java.util.List;

public class GetProductsBySearch {
    private final StockDAO stockDAO;

    public GetProductsBySearch(StockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    public List<Product> run(String productNumber) throws ProductDoesNotExistException, StockException {
        List<Product> products = stockDAO.searchByDescription(productNumber);
        if (products.isEmpty()) {
            throw new ProductDoesNotExistException();
        }
        return products;
    }
}
