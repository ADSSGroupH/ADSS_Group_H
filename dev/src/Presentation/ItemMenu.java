package Presentation;

import Domain.*;

import java.text.ParseException;
import java.util.*;
//
//public class ItemMenu {
//    private final Scanner scanner;
//    private final SystemController controller;
//
//    // Constructor - setting up the item menu with scanner and controller
//    public ItemMenu(Scanner scanner, SystemController controller) {
//        this.scanner = scanner;
//        this.controller = controller;
//    }
//
//    // Display the item management main menu
//    public void display() {
//        while (true) {
//            System.out.println("\n=== Item Management Menu ===");
//            System.out.println("1. Show all items");
//            System.out.println("2. Add new item");
//            System.out.println("3. View suppliers for item");
//            System.out.println("4. Edit item details");
//            System.out.println("0. Back to main menu");
//            System.out.print("Your choice: ");
//
//            String choice = scanner.nextLine();
//            switch (choice) {
//                case "1" -> showAllItems();
//                case "2" -> addNewItem();
//                case "3" -> viewSuppliersForItem();
//                case "4" -> editItem();
//                case "0" -> { return; }
//                default -> System.out.println("Invalid choice.");
//            }
//        }
//    }
//
//    // Edit the details (name or ID) of an existing item
//    private void editItem() {
//        System.out.print("Enter Item ID to edit: ");
//        String itemId = scanner.nextLine();
//
//        Product product = controller.getItemById(itemId);
//        if (product == null) {
//            System.out.println("Item not found.");
//            return;
//        }
//
//        System.out.println("\nEditing item: " + product.getName() + " (ID: " + product.getPid() + ")");
//        while (true) {
//            System.out.println("\nWhat would you like to update?");
//            System.out.println("1. Change item name");
//            System.out.println("2. Change item ID");
//            System.out.println("0. Back");
//            System.out.print("Your choice: ");
//
//            String choice = scanner.nextLine().trim();
//            switch (choice) {
//                case "1" -> {
//                    System.out.print("Enter new name: ");
//                    String newName = scanner.nextLine().trim();
//                    if (!newName.isEmpty()) {
//                        product.setName(newName);
//                        System.out.println("✔️ Name updated to " + newName);
//                    } else {
//                        System.out.println("Name cannot be empty.");
//                    }
//                }
//                case "2" -> {
//                    System.out.print("Enter new ID: ");
//                    String newId = scanner.nextLine().trim();
//                    if (!newId.isEmpty()) {
//                        controller.updateProductId(product, newId);
//                        System.out.println("✔️ ID updated to " + newId);
//                    } else {
//                        System.out.println("ID cannot be empty.");
//                    }
//                }
//                case "0" -> {
//                    System.out.println("Returning to item menu.");
//                    return;
//                }
//                default -> System.out.println("Invalid choice.");
//            }
//        }
//    }
//
//    // Show all items currently in the system
//    private void showAllItems() {
//        Collection<Product> items = controller.getAllItems();
//        if (items.isEmpty()) {
//            System.out.println("No items in the system.");
//        } else {
//            int i = 1;
//            for (Product item : items) {
//                System.out.println("Item " + i + " - ");
//                System.out.println(item);
//                i++;
//            }
//        }
//    }
//
//    // Add a new item into the system
//    private void addNewItem() {
//        System.out.print("Enter Item ID: ");
//        String id = scanner.nextLine();
//        System.out.print("Enter Item Name: ");
//        String name = scanner.nextLine();
//
//        boolean success = controller.addItem(id, name);
//        if (success) {
//            System.out.println("Item added successfully.");
//        } else {
//            System.out.println("Item already exists.");
//        }
//    }
//
//    // View all suppliers that sell a specific item
//    private void viewSuppliersForItem() {
//        System.out.print("Enter Item ID: ");
//        String itemId = scanner.nextLine();
//
//        Map<Supplier, String> suppliers = controller.getSuppliersForItem(itemId);
//
//        if (suppliers.isEmpty()) {
//            System.out.println("No suppliers found for this item.");
//        } else {
//            System.out.println("Suppliers for item " + itemId + ":");
//
//            for (Map.Entry<Supplier, String> entry : suppliers.entrySet()) {
//                Supplier supplier = entry.getKey();
//                String catalogNumber = entry.getValue();
//                float price = -1;
//
//                // Try to find the price from the supplier's agreements
//                for (Agreement agreement : supplier.getAgreements()) {
//                    for (AgreementItem ai : agreement.getItems().keySet()) {
//                        if (ai.getItemId().equals(itemId)) {
//                            price = ai.getPrice(1);
//                            break;
//                        }
//                    }
//                }
//
//                if (price != -1) {
//                    System.out.printf("Supplier id: %s | Name: %s | Catalog Number: %s | Price: %.2f\n",
//                            supplier.getSupplierId(),
//                            supplier.getName(),
//                            catalogNumber,
//                            price);
//                } else {
//                    System.out.printf("Supplier id: %s | Name: %s | Catalog Number: %s | Price: Not found\n",
//                            supplier.getSupplierId(),
//                            supplier.getName(),
//                            catalogNumber);
//                }
//            }
//        }
//    }
//}

public class ItemMenu {
    private final Scanner scanner;
    private final SystemController controller;

    public ItemMenu(Scanner scanner, SystemController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    public void display() throws ParseException {
        int choice;
        do {
            System.out.println("\n=== Item Management Menu ===");
            System.out.println("1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. Show item details");
            System.out.println("4. Mark item as defective");
            System.out.println("5. Mark item as expired");
            System.out.println("0. Back to Main Menu");
            choice = controller.promptInt(scanner);
            switch (choice) {
                case 1 -> controller.addItem();
                case 2 -> controller.removeItem();
                case 3 -> controller.showItemDetails();
                case 4 -> controller.markDefective();
                case 5 -> controller.markExpired();
                case 0 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }
}
