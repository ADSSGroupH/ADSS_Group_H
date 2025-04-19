import java.util.*;

public class OrderService {
    private final List<Order> orders = new ArrayList<>();

    public void placeOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Order getOrderById(String orderId) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    public void updateOrderStatus(Order order, OrderStatus status) {
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(status);
        }
    }
}