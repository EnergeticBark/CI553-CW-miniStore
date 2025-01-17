package products;

import dao.DAO;
import dao.DAOException;

import java.util.List;

public interface ProductDAO extends DAO<Product> {
    /**
     * Searches for products based on keywords in their descriptions.
     * @param searchQuery the search query e.g. "TV", "Radio" or "Watch".
     * @return a possibly empty list of search results.
     * @throws DAOException if there was an issue.
     */
    List<Product> search(String searchQuery) throws DAOException;
}
