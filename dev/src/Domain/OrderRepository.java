package Domain;

import java.util.*;

/**
 * Repository for managing Order objects in memory.
 * Singleton pattern ensures a single shared instance.
 */
public class OrderRepository {

    private static OrderRepository instance = null;
    private final Map<String, Order> orders = new HashMap<>();

    private OrderRepository() {}

    public static OrderRepository getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    public boolean search(String id) {
        return orders.containsKey(id);
    }

    public boolean remove(String id) {
        return orders.remove(id) != null;
    }

    public void add(Order order) {
        orders.put(order.getOrderId(), order);
    }

    public void update(Order updatedOrder) {
        if (updatedOrder != null && orders.containsKey(updatedOrder.getOrderId())) {
            orders.put(updatedOrder.getOrderId(), updatedOrder);
        }
    }

    public Order get(String id) {
        return orders.get(id);
    }

    public Map<String, Order> getAll() {
        return orders;
    }

    public Order createOrder(String orderId, Supplier supplier, Map<String, Integer> items,
                             String orderDate, int totalPrice, OrderStatus status) {
        return new Order(orderId, supplier, items, orderDate, status);
    }
}
