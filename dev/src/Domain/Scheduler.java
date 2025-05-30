package Domain;

import java.time.LocalDate;
import java.util.Map;

public class Scheduler {

    private final OrderRepository orderRepository;

    public Scheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void updatePeriodicOrders() {
        Map<String, Order> orders = orderRepository.getAll();

        for (Order order : orders.values()) {
            if (order instanceof PeriodicOrder periodicOrder) {

                if (periodicOrder.getNextDeliveryTime() != null &&
                        periodicOrder.getNextDeliveryTime().toLocalDate().equals(LocalDate.now().plusDays(1))) {

                    periodicOrder.setStatus(OrderStatus.READY);
                    periodicOrder.setNextDeliveryTime(
                            periodicOrder.getNextDeliveryTime().plus(periodicOrder.getDeliveryPeriod())
                    );

                    orderRepository.update(periodicOrder); // שימוש במתודה שלך
                }
            }
        }
    }
}
