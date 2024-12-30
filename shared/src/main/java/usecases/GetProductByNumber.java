package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.StockException;
import middle.StockReader;

public class GetProductByNumber {
    private final StockReader stockReader;

    public GetProductByNumber(StockReader stockReader) {
        this.stockReader = stockReader;
    }

    public Product run(String productNumber) throws ProductDoesNotExistException, StockException {
        if (!stockReader.exists(productNumber)) {
            throw new ProductDoesNotExistException();
        }
        return stockReader.getDetails(productNumber);
    }
}
