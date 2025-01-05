package products.usecases;

import products.Product;
import products.exceptions.ProductDoesNotExistException;
import products.ProductDAO;
import middle.DAOException;

public class GetProductByNumber {
    private final ProductDAO stockDAO;

    public GetProductByNumber(ProductDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    public Product run(String productNumber) throws ProductDoesNotExistException, DAOException {
        if (!stockDAO.exists(productNumber)) {
            throw new ProductDoesNotExistException();
        }
        return stockDAO.get(productNumber);
    }
}
