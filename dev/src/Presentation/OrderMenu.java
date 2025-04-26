package Presentation;

import Domain.*;
import controller.SystemController;

import java.time.LocalDate;
import java.util.*;

public class OrderMenu {
    private final Scanner scanner;
    private final SystemController controller;

    // Constructor - setting up the order menu with scanner and controller
    public OrderMenu(Scanner scanner, SystemController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    // Display the order management main menu
    public void display() {
        while (true) {
            System.out.println("\n=== Order Management Menu ===");
            System.out.println("1. Show all orders");
            System.out.println("2. Place new order");
            System.out.println("3. View order details");
            System.out.println("4. Update order");
            System.out.println("5. Show orders by specific supplier");
            System.out.println("0. Back to main menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showAllOrders();
                case "2" -> placeNewOrder();
                case "3" -> viewOrderDetails();
                case "4" -> updateOrderMenu();
                case "5" -> showOrdersBySupplier();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Show all orders in the system
    private void showAllOrders() {
        List<Order> orders = controller.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found in the system.");
            return;
        }
        for (Order order : orders) {
            printOrderDetails(order);
        }
    }

    // Create and place a new order
    private void placeNewOrder() {
        System.out.print("Enter Supplier ID: ");
        String supplierId = scanner.nextLine();
        Supplier supplier = controller.getSupplierById(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        Map<String, Integer> orderedItems = new HashMap<>();
        float total = 0;

        System.out.println("Start adding items to the order.");
        do {
            System.out.print("Item ID: ");
            String itemId = scanner.nextLine();

            AgreementItem matchedItem = null;
            for (Agreement agreement : supplier.getAgreements()) {
                for (AgreementItem ai : agreement.getItems().keySet()) {
                    if (ai.getItemId().equals(itemId)) {
                        matchedItem = ai;
                        break;
                    }
                }
                if (matchedItem != null) break;
            }

            if (matchedItem == null) {
                System.out.println("Item " + itemId + " is not available from this supplier.");
            } else {
                System.out.print("Quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                float basePrice = matchedItem.getPrice(1);
                float price = matchedItem.getPrice(quantity);
                float discount = matchedItem.getDiscount();
                String name = matchedItem.getName();
                float itemTotal = price * quantity;
                total += itemTotal;
                orderedItems.put(itemId, quantity);
                System.out.printf("✔️ Item added: %s | Name: %s | Quantity: %d | Base Price per unit: %.2f | Discount: %.2f | Total: %.2f\n",
                        itemId, name, quantity, basePrice, discount, itemTotal);
            }

            System.out.print("Add another item? (y/n): ");
        } while (scanner.nextLine().trim().equalsIgnoreCase("y"));

        if (orderedItems.isEmpty()) {
            System.out.println("No valid items in order. Order cancelled.");
            return;
        }

        System.out.print("Order ID: ");
        String orderId = scanner.nextLine();

        String orderDate = LocalDate.now().toString();

        Order order = controller.CreateOrder(orderId, supplier, orderedItems, orderDate, (int) total, OrderStatus.PENDING);
        controller.placeOrder(order);
        System.out.println("Order placed successfully. Total: " + total);
    }

    // View details of a specific order
    private void viewOrderDetails() {
        System.out.print("Enter Order ID: ");
        String orderId = scanner.nextLine();
        Order order = controller.getOrderById(orderId);
        if (order != null) {
            printOrderDetails(order);
        } else {
            System.out.println("Order not found.");
        }
    }

    // Display options to update an existing order
    private void updateOrderMenu() {
        System.out.print("Enter Order ID: ");
        String orderId = scanner.nextLine();
        Order order = controller.getOrderById(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return;
        }

        while (true) {
            System.out.println("\n=== Update Order ===");
            System.out.println("1. Modify items (if status is PENDING)");
            System.out.println("2. Change order status");
            System.out.println("0. Back");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    if (order.getStatus() != OrderStatus.PENDING) {
                        System.out.println("Only PENDING orders can be modified.");
                        return;
                    }
                    updateOrderItems(order);
                }
                case "2" -> updateOrderStatus(order);
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Update items within a PENDING order
    private void updateOrderItems(Order order) {
        Map<String, Integer> items = order.getItems();

        while (true) {
            printCurrentOrderItems(items);
            System.out.println("1. Add/Update item quantity");
            System.out.println("2. Remove item");
            System.out.println("0. Done");
            System.out.print("Your choice: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> handleAddOrUpdateItem(order, items);
                case "2" -> handleRemoveItem(items);
                case "0" -> {
                    System.out.println("Finished updating items.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Print current items and their quantities in the order
    private void printCurrentOrderItems(Map<String, Integer> items) {
        System.out.println("\nCurrent items:");
        items.forEach((k, v) -> System.out.println("- Item ID: " + k + ", Quantity: " + v));
    }

    // Handle adding a new item or updating quantity in an order
    private void handleAddOrUpdateItem(Order order, Map<String, Integer> items) {
        System.out.print("Enter item ID: ");
        String itemId = scanner.nextLine().trim();

        if (itemId.isEmpty()) {
            System.out.println("Item ID cannot be empty.");
            return;
        }

        if (!isItemAvailableFromSupplier(order.getSupplier(), itemId)) {
            System.out.println("This item is not available from the supplier.");
            return;
        }

        System.out.print("Enter new quantity: ");
        String quantityInput = scanner.nextLine().trim();

        try {
            int quantity = Integer.parseInt(quantityInput);
            if (quantity < 0) {
                System.out.println("Quantity cannot be negative.");
            } else {
                items.put(itemId, quantity);
                System.out.println("✔️ Item updated.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Please enter a valid integer.");
        }
    }

    // Handle removing an item from an order
    private void handleRemoveItem(Map<String, Integer> items) {
        System.out.print("Enter item ID to remove: ");
        String itemId = scanner.nextLine();
        if (items.remove(itemId) != null) {
            System.out.println("Item removed.");
        } else {
            System.out.println("Item not found.");
        }
    }

    // Check if an item is available from the supplier
    private boolean isItemAvailableFromSupplier(Supplier supplier, String itemId) {
        for (Agreement agreement : supplier.getAgreements()) {
            for (AgreementItem ai : agreement.getItems().keySet()) {
                if (ai.getItemId().equals(itemId)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Update the status of an order (PENDING, READY, COLLECTED)
    private void updateOrderStatus(Order order) {
        System.out.println("Current status: " + order.getStatus());
        System.out.println("1. Mark as PENDING");
        System.out.println("2. Mark as READY");
        System.out.println("3. Mark as COLLECTED");
        String choice = scanner.nextLine();

        OrderStatus newStatus;
        switch (choice) {
            case "1" -> newStatus = OrderStatus.PENDING;
            case "2" -> newStatus = OrderStatus.READY;
            case "3" -> newStatus = OrderStatus.COLLECTED;
            default -> {
                System.out.println("Invalid choice.");
                return;
            }
        }

        if (controller.updateOrderStatus(order, newStatus)) {
            System.out.println("Order status updated to: " + order.getStatus());
        } else {
            System.out.println("Order status update failed. Only orders with status PENDING can be updated.");
        }
    }

    // Print full details of a specific order
    private void printOrderDetails(Order order) {
        System.out.println("\n------------------------");
        System.out.println("Supplier id: " +  order.getSupplier().getSupplierId());
        System.out.println("Supplier name: " + order.getSupplier().getName());
        System.out.println("Supplier address: " + order.getSupplier().getDeliveryAddress());
        System.out.println("Contact person: " + order.getSupplier().getOneContactPerson());
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Order Date: " + order.getOrderDate());
        System.out.println("Status: " + order.getStatus());
        System.out.println("Items:");
        for (Map.Entry<String, Integer> entry : order.getItems().entrySet()) {
            System.out.println("  - Item ID: " + entry.getKey() + ", Quantity: " + entry.getValue());
        }
        System.out.println("Total Price: " + order.getTotalPrice());
        System.out.println("------------------------\n");
    }

    // Show all orders placed with a specific supplier
    private void showOrdersBySupplier() {
        System.out.print("Enter Supplier ID: ");
        String supplierId = scanner.nextLine();

        List<Order> orders = controller.getAllOrders();
        List<Order> filtered = new ArrayList<>();

        for (Order order : orders) {
            if (order.getSupplier().getSupplierId().equals(supplierId)) {
                filtered.add(order);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No orders found for this supplier.");
        } else {
            System.out.println("Orders for supplier ID: " + supplierId);
            for (Order order : filtered) {
                printOrderDetails(order);
            }
        }
    }
}
