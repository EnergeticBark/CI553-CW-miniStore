package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.ProductDAO;
import middle.DAOException;

public class RestockProduct {
    private final ProductDAO stockDAO;

    public RestockProduct(ProductDAO stockReadWriter) {
        this.stockDAO = stockReadWriter;
    }

    public Product run(String productNumber, int amount) throws DAOException, ProductDoesNotExistException {
        Product inventory = new GetProductByNumber(stockDAO).run(productNumber);
        new AddStock().run(inventory, amount);

        stockDAO.update(inventory);
        return inventory;
    }
}
