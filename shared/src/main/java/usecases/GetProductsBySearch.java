package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.StockException;
import middle.StockReader;

import java.util.List;

public class GetProductsBySearch {
    private final StockReader stockReader;

    public GetProductsBySearch(StockReader stockReader) {
        this.stockReader = stockReader;
    }

    public List<Product> run(String productNumber) throws ProductDoesNotExistException, StockException {
        List<Product> products = stockReader.searchByDescription(productNumber);
        if (products.isEmpty()) {
            throw new ProductDoesNotExistException();
        }
        return products;
    }
}
