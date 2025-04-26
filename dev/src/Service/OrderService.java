package Service;

import Domain.Order;
import Domain.OrderStatus;
import Domain.Supplier;

import java.util.*;

public class OrderService {
    private final List<Order> orders = new ArrayList<>();

    // Place a new order into the system
    public void placeOrder(Order order) {
        orders.add(order);
    }

    // Create a new order object
    public Order CreateOrder(String orderId, Supplier supplier, Map<String, Integer> items,
                             String orderDate, int totalPrice, OrderStatus status) {
        return new Order(orderId, supplier, items, orderDate, totalPrice, status);
    }

    // Get a list of all orders
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    // Find an order by its ID
    public Order getOrderById(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    // Update the status of an order (only if it's currently PENDING)
    public void updateOrderStatus(Order order, OrderStatus status) {
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(status);
        }
    }
}
