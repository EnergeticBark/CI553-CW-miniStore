package products.dataaccess;

import dataaccess.DBAccess;
import dataaccess.DBAccessFactory;
import products.Product;
import dao.DAOException;
import products.ProductDAO;
import products.remote.RemoteProductDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// There can only be 1 ResultSet opened per statement
// so no simultaneous use of the statement object
// hence the synchronized methods

// mySQL
//    no spaces after SQL statement ;

/** Implements CRUD access to {@link Product}s held in a relational database. */
public class SQLProductDAO implements ProductDAO, RemoteProductDAO {
    final private Connection connection; // Connection to the database.

    /**
     * Connects to the database. Uses a factory method to help set up the connection.
     * @throws DAOException if there was a problem.
     */
    public SQLProductDAO() throws DAOException {
        try {
            DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
            dbDriver.loadDriver();

            connection = DriverManager.getConnection(
                    dbDriver.urlOfDatabase(),
                    dbDriver.username(),
                    dbDriver.password()
            );

            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DAOException("SQL problem:" + e.getMessage());
        } catch (Exception e) {
            throw new DAOException("Can not load database driver.");
        }
    }

    /**
     * @param productNumber the product number to check for.
     * @return whether an {@link Product} with the provided product number exists in the database
     */
    @Override
    public synchronized boolean exists(int productNumber) throws DAOException {
        final String query = "SELECT price FROM ProductTable WHERE productNo = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productNumber);
            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new DAOException("SQL exists: " + e.getMessage());
        }
    }

    /**
     * Fetches an {@link Product} assumed to exist in the database.
     * @param productNumber the product number of the product to retrieve.
     * @return the product with the provided product number.
     */
    @Override
    public synchronized Product get(int productNumber) throws DAOException {
        final String query = """
                SELECT description, picture, price, stockLevel
                FROM ProductTable, StockTable
                WHERE ProductTable.productNo = ?
                AND StockTable.productNo = ProductTable.productNo
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productNumber);
            ResultSet rs = statement.executeQuery();

            rs.next();
            return new Product(
                    productNumber,
                    rs.getString("description"),
                    rs.getString("picture"),
                    rs.getDouble("price"),
                    rs.getInt("stockLevel")
            );
        } catch (SQLException e) {
            throw new DAOException("SQL get: " + e.getMessage());
        }
    }

    /** {@return a list of every product stored in the database} */
    @Override
    public synchronized List<Product> getAll() throws DAOException {
        final String productQuery = "SELECT productNo FROM ProductTable";
        ArrayList<Product> products = new ArrayList<>();

        try (PreparedStatement productStatement = connection.prepareStatement(productQuery)) {
            ResultSet rs = productStatement.executeQuery();
            while (rs.next()) {
                int productNumber = rs.getInt("productNo");
                Product product = get(productNumber);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new DAOException("SQL getAll: " + e.getMessage());
        }

        return products;
    }

    /**
     * Saves the provided {@link Product} in the database, allowing it to be retrieved later using
     * {@link ProductDAO#get(int)}, {@link ProductDAO#getAll()}, or {@link ProductDAO#search(String)}.
     * @param product the product to save.
     */
    @Override
    public synchronized void create(Product product) throws DAOException {
        final String productQuery = "INSERT INTO ProductTable VALUES (?, ?, ?, ?)";
        final String stockQuery = "INSERT INTO StockTable VALUES (?, ?)";

        try (
                PreparedStatement productStatement = connection.prepareStatement(productQuery);
                PreparedStatement stockStatement = connection.prepareStatement(stockQuery)
        ) {
            productStatement.setInt(1, product.getProductNumber());
            productStatement.setString(2, product.getDescription());
            productStatement.setString(3, "images/Pic" + product.getProductNumber() + ".jpg");
            productStatement.setDouble(4, product.getPrice());
            productStatement.executeUpdate();

            stockStatement.setInt(1, product.getProductNumber());
            stockStatement.setInt(2, product.getQuantity());
            stockStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("SQL create: " + e.getMessage());
        }
    }

    /**
     * Modifies the details of an {@link Product} assumed to exist in database.
     * @param replacement new version of the product to store.
     */
    @Override
    public synchronized void update(Product replacement) throws DAOException {
        final String productQuery = "UPDATE ProductTable SET description = ?, price = ? WHERE productNo = ?";
        final String stockQuery = "UPDATE StockTable SET stockLevel = ? WHERE productNo = ?";

        try (
                PreparedStatement productStatement = connection.prepareStatement(productQuery);
                PreparedStatement stockStatement = connection.prepareStatement(stockQuery)
        ) {
            productStatement.setString(1, replacement.getDescription());
            productStatement.setDouble(2, replacement.getPrice());
            productStatement.setInt(3, replacement.getProductNumber());
            productStatement.executeUpdate();

            stockStatement.setInt(1, replacement.getQuantity());
            stockStatement.setInt(2, replacement.getProductNumber());
            stockStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("SQL modifyStock: " + e.getMessage());
        }
    }

    /**
     * Searches for products based on keywords in their descriptions.
     * @param searchQuery the search query e.g. "TV", "Radio" or "Watch".
     * @return a possibly empty list of search results.
     * @throws DAOException if there was an issue.
     */
    @Override
    public synchronized List<Product> search(String searchQuery) throws DAOException {
        // Make the search case-insensitive by converting the description and search query to uppercase.
        final String query = """
                SELECT StockTable.productNo, description, picture, price, stockLevel
                FROM ProductTable, StockTable
                WHERE UPPER(description) LIKE UPPER(?)
                AND StockTable.productNo = ProductTable.productNo
                """;
        List<Product> foundProducts = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                final Product product = new Product(
                        rs.getInt("productNo"),
                        rs.getString("description"),
                        rs.getString("picture"),
                        rs.getDouble("price"),
                        rs.getInt("stockLevel")
                );

                foundProducts.add(product);
            }
        } catch (SQLException e) {
            throw new DAOException("SQL searchByDescription: " + e.getMessage());
        }

        return foundProducts;
    }
}
