package dbAccess;

import catalogue.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReader;

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
public class DBStockReader implements StockReader {
    final private Connection theCon; // Connection to database

    /**
     * Connects to database
     * Uses a factory method to help set up the connection
     * @throws StockException if problem
     */
    public DBStockReader() throws StockException {
        try {
            DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
            dbDriver.loadDriver();

            theCon = DriverManager.getConnection(
                    dbDriver.urlOfDatabase(),
                    dbDriver.username(),
                    dbDriver.password()
            );
            
            theCon.setAutoCommit(true);
        } catch (SQLException e) {
            throw new StockException("SQL problem:" + e.getMessage());
        } catch (Exception e) {
            throw new StockException("Can not load database driver.");
        }
    }

    /**
     * Returns a connection object that is used to process
     * requests to the DataBase
     * @return a connection object
     */
    protected Connection getConnectionObject() {
        return theCon;
    }

    /**
     * Checks if the product exits in the stock list
     * @param pNum The product number
     * @return true if exists otherwise false
     */
    public synchronized boolean exists(String pNum) throws StockException {
        final String query = "SELECT price FROM ProductTable WHERE ProductTable.productNo = ?";

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setString(1, pNum);
            ResultSet rs = statement.executeQuery();

            boolean res = rs.next();
            DEBUG.trace("DB DBStockReader: exists(%s) -> %s", pNum, ( res ? "T" : "F" ));
            return res;
        } catch (SQLException e) {
            throw new StockException("SQL exists: " + e.getMessage());
        }
    }

    public synchronized List<Product> searchByDescription(String searchQuery) throws StockException {
        // Make the search case-insensitive by converting the description and search query to uppercase.
        final String query = """
                SELECT StockTable.productNo, description, price, stockLevel
                FROM ProductTable, StockTable
                WHERE UPPER(description) LIKE UPPER(?)
                AND StockTable.productNo = ProductTable.productNo
                """;
        List<Product> foundProducts = new ArrayList<>();

        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            statement.setString(1, "%" + searchQuery + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Product product = new Product("0", "", "", 0.00, 0);
                product.setProductNum(rs.getString("productNo"));
                product.setDescription(rs.getString("description"));
                product.setPicture(rs.getString("picture"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("stockLevel"));

                foundProducts.add(product);
            }
        } catch (SQLException e) {
            throw new StockException("SQL searchByDescription: " + e.getMessage());
        }

        return foundProducts;
    }

    /**
     * Returns details about the product in the stock list.
     *  Assumed to exist in database.
     * @param pNum The product number
     * @return Details in an instance of a Product
     */
    public synchronized Product getDetails(String pNum) throws StockException {
        final String query = """
                SELECT description, picture, price, stockLevel
                FROM ProductTable, StockTable
                WHERE ProductTable.productNo = ?
                AND StockTable.productNo = ProductTable.productNo
                """;
        try (PreparedStatement statement = getConnectionObject().prepareStatement(query)) {
            Product dt = new Product("0", "", "",0.00, 0);
            statement.setString(1, pNum);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                dt.setProductNum(pNum);
                dt.setDescription(rs.getString("description"));
                dt.setPicture(rs.getString("picture"));
                dt.setPrice(rs.getDouble("price"));
                dt.setQuantity(rs.getInt("stockLevel"));
            }
            return dt;
        } catch (SQLException e) {
            throw new StockException("SQL getDetails: " + e.getMessage());
        }
    }
}
