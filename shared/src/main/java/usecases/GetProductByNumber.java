package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.StockDAO;
import middle.StockException;

public class GetProductByNumber {
    private final StockDAO stockDAO;

    public GetProductByNumber(StockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    public Product run(String productNumber) throws ProductDoesNotExistException, StockException {
        if (!stockDAO.exists(productNumber)) {
            throw new ProductDoesNotExistException();
        }
        return stockDAO.getDetails(productNumber);
    }
}
