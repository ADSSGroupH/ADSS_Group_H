import java.util.*;

public class OrderMenu {
    private final Scanner scanner;
    private final SupplierService supplierService;
    private final OrderService orderService;

    public OrderMenu(Scanner scanner, SupplierService supplierService, OrderService orderService) {
        this.scanner = scanner;
        this.supplierService = supplierService;
        this.orderService = orderService;
    }

    public void display() {
        while (true) {
            System.out.println("\n=== Order Management Menu ===");
            System.out.println("1. Show all orders");
            System.out.println("2. Place new order");
            System.out.println("3. View order details");
            System.out.println("4. Update order status");
            System.out.println("0. Back to main menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showAllOrders();
                case "2" -> placeNewOrder();
                case "3" -> viewOrderDetails();
                case "4" -> updateOrderStatus();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void showAllOrders() {
        for (Order order : orderService.getAllOrders()) {
            System.out.println(order);
        }
    }

    private void placeNewOrder() {
        System.out.print("Enter Supplier ID: ");
        String supplierId = scanner.nextLine();
        Supplier supplier = supplierService.getSupplierById(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        Map<String, Integer> orderedItems = new HashMap<>();
        float total = 0;

        System.out.println("Enter items to order (type 'done' to finish):");
        while (true) {
            System.out.print("Item ID: ");
            String itemId = scanner.nextLine();
            if (itemId.equalsIgnoreCase("done")) break;

            System.out.print("Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            AgreementItem matchedItem = null;
            for (Agreement agreement : supplier.getAgreements()) {
                for (AgreementItem ai : agreement.getItems().keySet()) {
                    if (ai.getCatalogNumber().equals(itemId)) {
                        matchedItem = ai;
                        break;
                    }
                }
                if (matchedItem != null) break;
            }

            if (matchedItem == null) {
                System.out.println("Item " + itemId + " is not available from this supplier.");
                continue;
            }

            float price = matchedItem.getPrice(quantity);
            float itemTotal = price * quantity;
            total += itemTotal;
            orderedItems.put(itemId, quantity);

            System.out.println("✔️ Item added: " + itemId + " | Unit Price: " + price + " | Total: " + itemTotal);
        }

        if (orderedItems.isEmpty()) {
            System.out.println("No valid items in order. Order cancelled.");
            return;
        }

        System.out.print("Order ID: ");
        String orderId = scanner.nextLine();

        System.out.print("Order Date (yyyy-mm-dd): ");
        String orderDate = scanner.nextLine();

        Order order = new Order(orderId, supplier, orderedItems, orderDate, (int) total, OrderStatus.PENDING);
        orderService.placeOrder(order);
        System.out.println("Order placed successfully. Total: " + total);
    }

    private void viewOrderDetails() {
        System.out.print("Enter Order ID: ");
        String orderId = scanner.nextLine();
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            System.out.println(order);
        } else {
            System.out.println("Order not found.");
        }
    }

    private void updateOrderStatus() {
        System.out.print("Enter Order ID: ");
        String orderId = scanner.nextLine();
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Cannot update. Order is already " + order.getStatus());
            return;
        }

        System.out.println("Current status: " + order.getStatus());
        System.out.println("1. Mark as READY");
        System.out.println("2. Mark as COLLECTED");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> orderService.updateOrderStatus(order, OrderStatus.READY);
            case "2" -> orderService.updateOrderStatus(order, OrderStatus.COLLECTED);
            default -> System.out.println("Invalid choice.");
        }

        System.out.println("Order status updated to: " + order.getStatus());
    }
}
