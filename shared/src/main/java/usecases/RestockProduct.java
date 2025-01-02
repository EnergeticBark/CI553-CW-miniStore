package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.DAO;
import middle.StockException;

public class RestockProduct {
    private final DAO<Product> stockDAO;

    public RestockProduct(DAO<Product> stockReadWriter) {
        this.stockDAO = stockReadWriter;
    }

    public Product run(String productNumber, int amount) throws StockException, ProductDoesNotExistException {
        Product inventory = new GetProductByNumber(stockDAO).run(productNumber);
        new AddStock().run(inventory, amount);

        stockDAO.update(inventory);
        return inventory;
    }
}
