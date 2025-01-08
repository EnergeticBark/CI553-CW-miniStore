package orders.dataaccess;

import catalogue.Basket;
import dataaccess.DBAccess;
import dataaccess.DBAccessFactory;
import dao.DAOException;
import orders.Order;
import orders.OrderDAO;
import orders.remote.RemoteOrderDAO;
import products.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLOrderDAO implements OrderDAO, RemoteOrderDAO {
    final private Connection connection; // Connection to database

    /**
     * Connects to database
     * Uses a factory method to help set up the connection
     * @throws DAOException if problem
     */
    public SQLOrderDAO() throws DAOException {
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

    @Override
    public boolean exists(String pNum) throws DAOException {
        return false;
    }

    @Override
    public List<Order> search(String searchQuery) throws DAOException {
        return List.of();
    }

    @Override
    public Order get(String identifier) throws DAOException {
        final String orderQuery = "SELECT state FROM OrderTable WHERE orderNo = ?";
        final String orderLineQuery = """
            SELECT OrderLineTable.productNo, quantity, description, picture, price
            FROM OrderLineTable, ProductTable
            WHERE OrderLineTable.orderNo = ? AND OrderLineTable.productNo = ProductTable.productNo""";

        int orderNumber = Integer.parseInt(identifier);

        try (
                PreparedStatement orderStatement = getConnectionObject().prepareStatement(orderQuery);
                PreparedStatement orderLineStatement = getConnectionObject().prepareStatement(orderLineQuery)
        ) {
            orderStatement.setInt(1, orderNumber);
            ResultSet rs = orderStatement.executeQuery();

            rs.next();
            orderLineStatement.setInt(1, orderNumber);
            ResultSet lineRs = orderLineStatement.executeQuery();
            Basket basket = new Basket();
            while (lineRs.next()) {
                final Product product = new Product(
                        lineRs.getString("productNo"),
                        lineRs.getString("description"),
                        lineRs.getString("picture"),
                        lineRs.getDouble("price"),
                        lineRs.getInt("quantity")
                );
                basket.add(product);
            }
            
            int orderStateID = rs.getInt("state");
            Order.State state = switch (orderStateID) {
                case 1 -> Order.State.Waiting;
                case 2 -> Order.State.BeingPacked;
                case 3 -> Order.State.ToBeCollected;
                default -> throw new DAOException("SQL get: unexpected state " + orderStateID);
            };

            return new Order(orderNumber, state, basket);
        } catch (SQLException e) {
            throw new DAOException("SQL get: " + e.getMessage());
        }
    }

    @Override
    public void create(Order order) throws DAOException {
        final String orderQuery = "INSERT INTO OrderTable VALUES (?, ?)";
        final String orderLineQuery = "INSERT INTO OrderLineTable VALUES (?, ?, ?)";

        try (
                PreparedStatement orderStatement = getConnectionObject().prepareStatement(orderQuery);
                PreparedStatement orderLineStatement = getConnectionObject().prepareStatement(orderLineQuery)
        ) {
            orderStatement.setInt(1, order.getOrderNumber());
            orderStatement.setInt(2, order.getStateID());
            orderStatement.executeUpdate();

            for (Product product: order.getBasket()) {
                orderLineStatement.setInt(1, order.getOrderNumber());
                orderLineStatement.setString(2, product.getProductNumber());
                orderLineStatement.setInt(3, product.getQuantity());
                orderLineStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("SQL create: " + e.getMessage());
        }
    }

    @Override
    public void update(Order replacement) throws DAOException {
        /*
        We could update everything in place, but it would be much more complicated since each Order might have several
        rows in OrderLines. It's much easier to just delete and then re-create the whole Order.
         */
        final String orderQuery = "DELETE FROM OrderTable WHERE orderNo = ?";
        final String orderLineQuery = "DELETE FROM OrderLineTable WHERE orderNo = ?";

        try (
                PreparedStatement orderStatement = getConnectionObject().prepareStatement(orderQuery);
                PreparedStatement orderLineStatement = getConnectionObject().prepareStatement(orderLineQuery)
        ) {
            orderLineStatement.setInt(1, replacement.getOrderNumber());
            orderLineStatement.executeUpdate();
            orderStatement.setInt(1, replacement.getOrderNumber());
            orderStatement.executeUpdate();

            create(replacement);
        } catch (SQLException e) {
            throw new DAOException("SQL create: " + e.getMessage());
        }
    }

    @Override
    public synchronized List<Order> getAll() throws DAOException {
        final String orderQuery = "SELECT orderNo FROM OrderTable";
        ArrayList<Order> orders = new ArrayList<>();

        try (PreparedStatement orderStatement = getConnectionObject().prepareStatement(orderQuery)) {
            ResultSet rs = orderStatement.executeQuery();
            while (rs.next()) {
                int orderNumber = rs.getInt("orderNo");
                Order order = get(String.valueOf(orderNumber));

                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DAOException("SQL getAll: " + e.getMessage());
        }

        return orders;
    }
}
