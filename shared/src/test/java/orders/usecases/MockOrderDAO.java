package orders.usecases;

import dao.DAOException;
import orders.Order;
import orders.OrderDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MockOrderDAO implements OrderDAO {
    List<Order> orders = new ArrayList<>();
    int nextOrderNumber = 1;

    public boolean exists(int identifier) {
        return orders.stream().anyMatch(orders -> orders.getOrderNumber() == identifier);
    }

    @Override
    public List<Order> search(String searchQuery)  {
        return List.of();
    }

    @Override
    public Order get(int identifier) throws DAOException {
        Optional<Order> found = orders.stream()
                .filter(order -> order.getOrderNumber() == identifier)
                .findFirst();
        if (found.isEmpty()) {
            throw new DAOException("get: No such order.");
        }
        return found.get();
    }

    @Override
    public void create(Order newEntity) {
        orders.add(newEntity);
    }

    @Override
    public void update(Order replacement) {
        orders = orders.stream()
                .filter(order -> order.getOrderNumber() != replacement.getOrderNumber())
                .collect(Collectors.toList());
        orders.add(replacement);
    }

    @Override
    public List<Order> getAll() {
        return orders;
    }

    @Override
    public int getNextOrderNumber() {
        int orderNumber = nextOrderNumber;
        nextOrderNumber += 1;
        return orderNumber;
    }
}
