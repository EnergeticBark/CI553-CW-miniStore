package usecases;

import catalogue.Product;
import exceptions.ProductDoesNotExistException;
import middle.ProductDAO;
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
