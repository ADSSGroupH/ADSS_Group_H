package dto;

public class OrderDto {
    private String orderId;
    private String supplierId;        // מייצג את שם או מזהה הספק
    private String items;             // "itemId1-2,itemId2-5"
    private String orderDate;
    private String arrivalDate;
    private double totalPrice;
    private String status;            // Enum כ-String: "PENDING", "APPROVED" וכו'

    public OrderDto() {}

    public OrderDto(String orderId, String supplierId, String items,
                    String orderDate, String arrivalDate,
                    double totalPrice, String status) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.items = items;
        this.orderDate = orderDate;
        this.arrivalDate = arrivalDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
