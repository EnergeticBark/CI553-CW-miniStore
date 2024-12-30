package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.StockException;
import middle.StockReadWriter;

public class RestockProduct {
    private final StockReadWriter stockReadWriter;

    public RestockProduct(StockReadWriter stockReadWriter) {
        this.stockReadWriter = stockReadWriter;
    }

    public Product run(String productNumber, int amount) throws StockException, ProductDoesNotExistException {
        Product inventory = new GetProductByNumber(stockReadWriter).run(productNumber);
        new AddStock().run(inventory, amount);

        stockReadWriter.modifyStock(inventory);
        return inventory;
    }
}
