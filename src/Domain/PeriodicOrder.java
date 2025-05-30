package Domain;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Map;

public class PeriodicOrder extends Order {

    private Period deliveryPeriod;
    private LocalDateTime nextDeliveryTime;

    // 🔹 בנאי מלא תואם ל-Order
    public PeriodicOrder(
            String OrderID,
            Supplier supplier,
            Map<String, Integer> products,
            String address,
            OrderStatus status,
            Period deliveryPeriod,
            LocalDateTime nextDeliveryTime
    ) {
        super(OrderID,supplier, products, address, status);
        this.deliveryPeriod = deliveryPeriod;
        this.nextDeliveryTime = nextDeliveryTime;
    }


    public Period getDeliveryPeriod() {
        return deliveryPeriod;
    }

    public void setDeliveryPeriod(Period deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
    }

    public LocalDateTime getNextDeliveryTime() {
        return nextDeliveryTime;
    }

    public void setNextDeliveryTime(LocalDateTime nextDeliveryTime) {
        this.nextDeliveryTime = nextDeliveryTime;
    }

    public void scheduleNextDelivery() {
        if (deliveryPeriod != null && nextDeliveryTime != null) {
            this.nextDeliveryTime = this.nextDeliveryTime.plus(deliveryPeriod);
        }
    }

    public boolean isDueTomorrow() {
        if (nextDeliveryTime == null) return false;
        return nextDeliveryTime.toLocalDate().equals(LocalDateTime.now().plusDays(1).toLocalDate());
    }

    public void markReadyIfDue() {
        if (isDueTomorrow()) {
            this.setStatus(OrderStatus.READY);
            this.scheduleNextDelivery();
        }
    }
}