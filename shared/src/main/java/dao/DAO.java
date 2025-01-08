package dao;

import java.util.List;

public interface DAO<T> {
    /**
     * Checks if the product exits in the stock list
     * @param pNum Product number
     * @return true if exists otherwise false
     * @throws DAOException if issue
     */
    boolean exists(String pNum) throws DAOException;

    /**
     * Search for products based on keywords in their descriptions.
     * @param searchQuery The search query e.g. "TV", "Radio" or "Watch".
     * @return A possibly empty list of search results.
     * @throws DAOException If there was an issue.
     */
    List<T> search(String searchQuery) throws DAOException;

    /**
     * Returns details about the product in the stock list
     * @param pNum Product number
     * @return StockNumber, Description, Price, Quantity
     * @throws DAOException if issue
     */
    T get(String identifier) throws DAOException;

    void create(T newEntity) throws DAOException;

    /**
     * Modifies Stock details for a given product number.
     * Information modified: Description, Price
     * @param detail Replace with this version of product
     * @throws DAOException if issue
     */
    void update(T replacement) throws DAOException;
}
