package products.usecases;

import products.Product;
import products.exceptions.ProductDoesNotExistException;
import products.ProductDAO;
import dao.DAOException;

public class RestockProduct {
    private final ProductDAO productDAO;

    public RestockProduct(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product run(String productNumber, int amount) throws DAOException, ProductDoesNotExistException {
        Product inventory = new GetProductByNumber(productDAO).run(productNumber);
        new AddStock().run(inventory, amount);

        productDAO.update(inventory);
        return inventory;
    }
}
