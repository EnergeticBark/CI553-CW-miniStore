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

/** Implements CRUD access to {@link Order}s held in a relational database. */
public class SQLOrderDAO implements OrderDAO, RemoteOrderDAO {
    final private Connection connection; // Connection to the database.

    /**
     * Connects to the database. Uses a factory method to help set up the connection.
     * @throws DAOException if there was a problem.
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
     * @param orderNumber the order number to check for.
     * @return whether an {@link Order} with the provided order number exists in the database
     */
    @Override
    public boolean exists(int orderNumber) throws DAOException {
        final String query = "SELECT state FROM OrderTable WHERE orderNo = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderNumber);
            ResultSet rs = statement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new DAOException("SQL exists: " + e.getMessage());
        }
    }

    /**
     * Fetches an {@link Order} assumed to exist in the database.
     * @param orderNumber the order number of the order to retrieve.
     * @return the order with the provided order number.
     */
    @Override
    public Order get(int orderNumber) throws DAOException {
        final String orderQuery = "SELECT state FROM OrderTable WHERE orderNo = ?";
        final String orderLineQuery = """
            SELECT OrderLineTable.productNo, quantity, description, picture, price
            FROM OrderLineTable, ProductTable
            WHERE OrderLineTable.orderNo = ? AND OrderLineTable.productNo = ProductTable.productNo""";

        try (
                PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
                PreparedStatement orderLineStatement = connection.prepareStatement(orderLineQuery)
        ) {
            orderStatement.setInt(1, orderNumber);
            ResultSet rs = orderStatement.executeQuery();

            rs.next();
            orderLineStatement.setInt(1, orderNumber);
            ResultSet lineRs = orderLineStatement.executeQuery();
            Basket basket = new Basket();
            while (lineRs.next()) {
                final Product product = new Product(
                        lineRs.getInt("productNo"),
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

    /**
     * Saves the provided {@link Order} in the database, allowing it to be retrieved later using
     * {@link OrderDAO#get(int)} or {@link OrderDAO#getAll()}.
     * @param order The entity to save, e.g. a new Product or Order.
     */
    @Override
    public void create(Order order) throws DAOException {
        final String orderQuery = "INSERT INTO OrderTable VALUES (?, ?)";
        final String orderLineQuery = "INSERT INTO OrderLineTable VALUES (?, ?, ?)";

        try (
                PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
                PreparedStatement orderLineStatement = connection.prepareStatement(orderLineQuery)
        ) {
            orderStatement.setInt(1, order.getOrderNumber());
            int stateID = switch (order.getState()) {
                case Order.State.Waiting -> 1;
                case Order.State.BeingPacked -> 2;
                case Order.State.ToBeCollected -> 3;
            };
            orderStatement.setInt(2, stateID);
            orderStatement.executeUpdate();

            for (Product product: order.getBasket()) {
                orderLineStatement.setInt(1, order.getOrderNumber());
                orderLineStatement.setInt(2, product.getProductNumber());
                orderLineStatement.setInt(3, product.getQuantity());
                orderLineStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("SQL create: " + e.getMessage());
        }
    }

    /**
     * Modifies the details of an {@link Order} assumed to exist in database.
     * @param replacement new version of the order to store.
     */
    @Override
    public void update(Order replacement) throws DAOException {
        /*
         * We could update everything in place, but it would be much more complicated since each Order might have
         * several rows in OrderLines. It's much easier to just delete and then re-create the whole Order.
         */
        final String orderQuery = "DELETE FROM OrderTable WHERE orderNo = ?";
        final String orderLineQuery = "DELETE FROM OrderLineTable WHERE orderNo = ?";

        try (
                PreparedStatement orderStatement = connection.prepareStatement(orderQuery);
                PreparedStatement orderLineStatement = connection.prepareStatement(orderLineQuery)
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

    /** {@return a list of every order stored in the database} */
    @Override
    public synchronized List<Order> getAll() throws DAOException {
        final String orderQuery = "SELECT orderNo FROM OrderTable";
        ArrayList<Order> orders = new ArrayList<>();

        try (PreparedStatement orderStatement = connection.prepareStatement(orderQuery)) {
            ResultSet rs = orderStatement.executeQuery();
            while (rs.next()) {
                int orderNumber = rs.getInt("orderNo");
                Order order = get(orderNumber);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DAOException("SQL getAll: " + e.getMessage());
        }

        return orders;
    }

    /** {@return A unique order number} */
    @Override
    public synchronized int getNextOrderNumber() throws DAOException {
        final String orderQuery = "VALUES (NEXT VALUE FOR orderSeq)";
        try (PreparedStatement orderStatement = connection.prepareStatement(orderQuery)) {
            ResultSet rs = orderStatement.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DAOException("SQL getNextOrderNumber: " + e.getMessage());
        }
    }
}
