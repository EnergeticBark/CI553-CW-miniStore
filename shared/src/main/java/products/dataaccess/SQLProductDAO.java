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

/**
 * Implements read access to the stock list
 * The stock list is held in a relational database
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */
public class SQLProductDAO implements ProductDAO, RemoteProductDAO {
    final private Connection connection; // Connection to database

    /**
     * Connects to database
     * Uses a factory method to help set up the connection
     * @throws DAOException if problem
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
     * Returns a connection object that is used to process
     * requests to the DataBase
     * @return a connection object
     */
    protected Connection getConnectionObject() {
        return connection;
    }

    /**
     * Checks if the product exits in the stock list
     * @param productNumber The product number
     * @return true if exists otherwise false
     */
    @Override
    public synchronized boolean exists(int productNumber) throws DAOException {
        final String query = "SELECT price FROM ProductTable WHERE ProductTable.productNo = ?";

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setInt(1, productNumber);
            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new DAOException("SQL exists: " + e.getMessage());
        }
    }

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

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
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

    /**
     * Returns details about the product in the stock list.
     * Assumed to exist in database.
     * @param productNumber The product number
     * @return Details in an instance of a Product
     */
    @Override
    public synchronized Product get(int productNumber) throws DAOException {
        final String query = """
                SELECT description, picture, price, stockLevel
                FROM ProductTable, StockTable
                WHERE ProductTable.productNo = ?
                AND StockTable.productNo = ProductTable.productNo
                """;

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
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

    @Override
    public synchronized void create(Product product) throws DAOException {
        final String productQuery = "INSERT INTO ProductTable VALUES (?, ?, ?, ?)";
        final String stockQuery = "INSERT INTO StockTable VALUES (?, ?)";

        try (
                PreparedStatement productStatement = getConnectionObject().prepareStatement(productQuery);
                PreparedStatement stockStatement = getConnectionObject().prepareStatement(stockQuery)
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
     * Modifies Stock details for a given product number.
     *  Assumed to exist in database.
     * Information modified: Description, Price
     * @param replacement Product details to change stock list to
     */
    @Override
    public synchronized void update(Product replacement) throws DAOException {
        final String productQuery = "UPDATE ProductTable SET description = ?, price = ? WHERE productNo = ?";
        final String stockQuery = "UPDATE StockTable SET stockLevel = ? WHERE productNo = ?";

        try (
                PreparedStatement productStatement = getConnectionObject().prepareStatement(productQuery);
                PreparedStatement stockStatement = getConnectionObject().prepareStatement(stockQuery)
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
}
