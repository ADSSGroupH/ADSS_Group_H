package Domain;

import java.util.Map;

public class Order {
    private OrderStatus status;

    private String orderId;
    private Supplier supplier;
    private Map<String, Integer> items; // מפתח: מזהה פריט, ערך: כמות
    private String orderDate;
    private double totalPrice;
    private String arrivalDate;


    public Order(String orderId, Supplier supplier, Map<String, Integer> items,
                 String orderDate, int totalPrice, OrderStatus status) {
        this.orderId = orderId;
        this.supplier = supplier;
        this.items = items;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    // Setters
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", supplier=" + (supplier != null ? supplier.getName() : "null") +
                ", items=" + items +
                ", orderDate='" + orderDate + '\'' +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }
}
