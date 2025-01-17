package products.usecases;

import products.Product;
import products.exceptions.ProductDoesNotExistException;
import products.ProductDAO;
import dao.DAOException;

import java.util.List;

/** Use case that retrieves a list products by searching their descriptions in the persistence layer. */
public class GetProductsBySearch {
    private final ProductDAO productDAO;

    public GetProductsBySearch(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Retrieves a list products by searching their descriptions in the persistence layer.
     * @param searchQuery the search query.
     * @return a list of products containing {@code searchQuery} as a substring in their description (case-insensitive).
     * @throws ProductDoesNotExistException if there are no search results for the provided query.
     * @throws DAOException if there was an error in the persistence layer.
     */
    public List<Product> run(String searchQuery) throws ProductDoesNotExistException, DAOException {
        List<Product> products = productDAO.search(searchQuery);
        if (products.isEmpty()) {
            throw new ProductDoesNotExistException();
        }
        return products;
    }
}
