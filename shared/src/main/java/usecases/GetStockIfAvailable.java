package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import exceptions.ProductOutOfStockException;
import middle.StockException;
import middle.StockReader;

public class GetStockIfAvailable {
    StockReader stockReader;

    public GetStockIfAvailable(StockReader stockReader) {
        this.stockReader = stockReader;
    }

    public Product run(String productNumber, int quantity) throws
            ProductDoesNotExistException,
            ProductOutOfStockException,
            StockException {
        if (!stockReader.exists(productNumber)) {
            throw new ProductDoesNotExistException();
        }

        Product product = stockReader.getDetails(productNumber);
        if (product.getQuantity() < quantity) {
            throw new ProductOutOfStockException(product.getDescription());
        }
        product.setQuantity(quantity);

        return product;
    }
}
