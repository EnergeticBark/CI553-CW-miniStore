package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.DAO;
import middle.StockException;

public class GetProductByNumber {
    private final DAO<Product> stockDAO;

    public GetProductByNumber(DAO<Product> stockDAO) {
        this.stockDAO = stockDAO;
    }

    public Product run(String productNumber) throws ProductDoesNotExistException, StockException {
        if (!stockDAO.exists(productNumber)) {
            throw new ProductDoesNotExistException();
        }
        return stockDAO.get(productNumber);
    }
}
