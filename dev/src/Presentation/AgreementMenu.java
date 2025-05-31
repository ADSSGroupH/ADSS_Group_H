package Presentation;

import Domain.*;


import java.util.*;

public class AgreementMenu {
    private final Scanner scanner;
    private final SystemController controller;

    // Constructor - setting up the menu with scanner and system controller
    public AgreementMenu(Scanner scanner, SystemController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    // Display the agreement management main menu
    public void display() {
        while (true) {
            System.out.println("\n=== Agreement Management Menu ===");
            System.out.println("1. Show all agreements");
            System.out.println("2. Add new agreement");
            System.out.println("3. Edit existing agreement");
            System.out.println("0. Back to main menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showAllAgreements();
                case "2" -> addAgreement();
                case "3" -> editAgreement();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Show all agreements for all suppliers
    private void showAllAgreements() {
        List<Supplier> suppliers = controller.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers in the system.");
            return;
        }

        for (Supplier supplier : suppliers) {
            System.out.println("Supplier: " + supplier.getName());
            List<Agreement> agreements = supplier.getAgreements();
            if (agreements == null || agreements.isEmpty()) {
                System.out.println("No agreements with suppliers");
            } else {
                int i = 1;
                for (Agreement agreement : agreements) {
                    System.out.println("Agreement Details " + i++ + " :");
                    System.out.println(agreement);
                }
            }
        }
    }

    // Add a new agreement to a supplier
    public void addAgreement() {
        System.out.print("Supplier ID: ");
        String supplierId = scanner.nextLine().trim();
        Supplier supplier = controller.getSupplierById(supplierId);
        if (supplier == null) {
            System.out.println("❌ Supplier not found.");
            return;
        }

        System.out.print("Agreement ID: ");
        String agreementId = scanner.nextLine().trim();
        if (agreementId.isEmpty()) {
            System.out.println("❌ Agreement ID cannot be empty.");
            return;
        }

        boolean supportsDelivery = askYesNo("Supports delivery?");

        List<DeliveryWeekday> days = new ArrayList<>();
        if (supportsDelivery) {
            for (DeliveryWeekday day : DeliveryWeekday.values()) {
                if (askYesNo("Delivery on " + day.name().toLowerCase() + "?")) {
                    days.add(day);
                }
            }
        }

        Map<AgreementItem, Double> items = new HashMap<>();
        int itemCount = 1;

        while (askYesNo("Add product " + itemCount + " to agreement?")) {
            try {
                System.out.print("product ID: ");
                String itemId = scanner.nextLine().trim();
                if (itemId.isEmpty()) {
                    System.out.println("product ID cannot be empty.");
                    continue;
                }

                System.out.print("product Name: ");
                String itemName = scanner.nextLine().trim();
                if (itemName.isEmpty()) {
                    System.out.println("product name cannot be empty.");
                    continue;
                }

                controller.addItem(itemId, itemName); // create or update item in system

                System.out.print("Catalog Number: ");
                String catalog = scanner.nextLine().trim();
                if (catalog.isEmpty()) {
                    System.out.println("Catalog number cannot be empty.");
                    continue;
                }

                System.out.print("Price: ");
                float price = Float.parseFloat(scanner.nextLine().trim());
                if (price < 0) {
                    System.out.println("Price must be non-negative.");
                    continue;
                }

                System.out.print("Discount (0–100): ");
                float discount = Float.parseFloat(scanner.nextLine().trim());
                if (discount < 0 || discount > 100) {
                    System.out.println("Discount must be between 0 and 100.");
                    continue;
                }

                System.out.print("Min quantity for discount: ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity < 0) {
                    System.out.println("Minimum quantity must be non-negative.");
                    continue;
                }

                AgreementItem ai = controller.createAgreementItem(itemId, catalog, price, discount, quantity, itemName);
                items.put(ai, (double) price);
                itemCount++;

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number format. Please enter numeric values.");
            } catch (Exception e) {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }

        boolean success = controller.addAgreementToSupplier(supplierId, agreementId, supportsDelivery, days, items);
        if (success) {
            System.out.println("✅ Agreement added successfully.");
        } else {
            System.out.println("❌ Failed to add agreement. Please try again.");
        }
    }
    // Edit an existing agreement (add, remove, update items or delivery days)
    private void editAgreement() {
        System.out.print("Enter Supplier ID: ");
        String supplierId = scanner.nextLine();
        Supplier supplier = controller.getSupplierById(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        List<Agreement> agreements = supplier.getAgreements();
        if (agreements.isEmpty()) {
            System.out.println("This supplier has no agreements.");
            return;
        }

        System.out.println("\nAvailable agreements:");
        for (int i = 0; i < agreements.size(); i++) {
            System.out.println((i + 1) + ". " + agreements.get(i).getAgreementId());
        }

        int choice = -1;
        while (true) {
            System.out.print("Select agreement to edit (by number): ");
            try {
                choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < agreements.size()) break;
                System.out.println("Invalid choice.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        Agreement selected = agreements.get(choice);

        while (true) {
            System.out.println("\n=== Editing Agreement: " + selected.getAgreementId() + " ===");
            System.out.println("1. Add new product");
            System.out.println("2. Remove product");
            System.out.println("3. View current product");
            System.out.println("4. Update delivery days");
            System.out.println("5. Edit product details");
            System.out.println("0. Back");
            System.out.print("Your choice: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> addItemToAgreement(selected);
                case "2" -> removeItemFromAgreement(selected);
                case "3" -> viewAgreementItems(selected);
                case "4" -> updateDeliveryDays(selected);
                case "5" -> editAgreementItem(selected);
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Add a new item to an existing agreement
    private void addItemToAgreement(Agreement agreement) {
        System.out.print("product ID: ");
        String itemId = scanner.nextLine();
        System.out.print("product Name: ");
        String itemName = scanner.nextLine();
        controller.addItem(itemId, itemName);

        System.out.print("Catalog Number: ");
        String catalog = scanner.nextLine();
        System.out.print("Price: ");
        float price = Float.parseFloat(scanner.nextLine());
        System.out.print("Discount (0-100): ");
        float discount = Float.parseFloat(scanner.nextLine());
        System.out.print("Min quantity for discount: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        AgreementItem ai = controller.createAgreementItem(itemId, catalog, price, discount, quantity, itemName);
        agreement.addItem(ai, price);
        System.out.println("✔️ product added.");
    }

    // Remove an item from an existing agreement
    private void removeItemFromAgreement(Agreement agreement) {
        Set<AgreementItem> items = agreement.getItems().keySet();
        if (items.isEmpty()) {
            System.out.println("No products to remove.");
            return;
        }
        List<AgreementItem> itemList = new ArrayList<>(items);
        for (int i = 0; i < itemList.size(); i++) {
            AgreementItem ai = itemList.get(i);
            System.out.printf("%d. %s (%s)\n", i + 1, ai.getName(), ai.getItemId());
        }

        int removeIndex = -1;
        while (true) {
            System.out.print("Select product to remove: ");
            try {
                removeIndex = Integer.parseInt(scanner.nextLine()) - 1;
                if (removeIndex >= 0 && removeIndex < itemList.size()) break;
                System.out.println("Invalid choice.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        AgreementItem toRemove = itemList.get(removeIndex);
        agreement.removeItem(toRemove);
        System.out.println("✔️ Item removed.");
    }

    // View all items in an agreement
    private void viewAgreementItems(Agreement agreement) {
        Map<AgreementItem, Double> items = agreement.getItems();
        if (items.isEmpty()) {
            System.out.println("No products in this agreement.");
        } else {
            System.out.println("products in agreement:");
            int count = 1;
            for (AgreementItem ai : items.keySet()) {
                System.out.printf("%d. product ID: %s | Name: %s | Catalog: %s | Price: %.2f | Discount: %.2f%% | Min Qty: %d\n",
                        count++, ai.getItemId(), ai.getName(), ai.getCatalogNumber(),
                        ai.getPrice(1), ai.getDiscount(), ai.getquantityForDiscount());
            }
        }
    }

    // Update the delivery days for an agreement
    private void updateDeliveryDays(Agreement agreement) {
        System.out.println("Update delivery days:");
        List<DeliveryWeekday> newDays = new ArrayList<>();
        for (DeliveryWeekday day : DeliveryWeekday.values()) {
            if (askYesNo("Delivery on " + day.name().toLowerCase() + "?")) {
                newDays.add(day);
            }
        }
        agreement.setDeliveryDays(newDays);
        System.out.println("✔️ Delivery days updated.");
    }

    // Edit specific details of an item in an agreement
    private void editAgreementItem(Agreement agreement) {
        Set<AgreementItem> items = agreement.getItems().keySet();
        if (items.isEmpty()) {
            System.out.println("No products to edit.");
            return;
        }

        List<AgreementItem> itemList = new ArrayList<>(items);
        System.out.println("products in agreement:");
        for (int i = 0; i < itemList.size(); i++) {
            AgreementItem ai = itemList.get(i);
            System.out.printf("%d. %s\n", i + 1, ai.getName());
        }

        int editIndex = -1;
        while (true) {
            System.out.print("Select product to edit: ");
            try {
                editIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (editIndex >= 0 && editIndex < itemList.size()) break;
                System.out.println("Invalid choice.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        AgreementItem itemToEdit = itemList.get(editIndex);
        double currentPrice = agreement.getItems().get(itemToEdit);
        float currentDiscount = itemToEdit.getDiscount();
        int currentMinQty = itemToEdit.getquantityForDiscount();

        while (true) {
            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Price (current: " + currentPrice + ")");
            System.out.println("2. Discount (current: " + currentDiscount + "%)");
            System.out.println("3. Min quantity for discount (current: " + currentMinQty + ")");
            System.out.println("0. Back");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter new price: ");
                    try {
                        float newPrice = Float.parseFloat(scanner.nextLine());
                        agreement.getItems().put(itemToEdit, (double) newPrice);
                        System.out.println("✔️ Price updated.");
                        currentPrice = newPrice;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "2" -> {
                    System.out.print("Enter new discount (0-100): ");
                    try {
                        float newDiscount = Float.parseFloat(scanner.nextLine());
                        itemToEdit.setDiscount(newDiscount);
                        System.out.println("✔️ Discount updated.");
                        currentDiscount = newDiscount;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "3" -> {
                    System.out.print("Enter new minimum quantity: ");
                    try {
                        int newQty = Integer.parseInt(scanner.nextLine());
                        itemToEdit.setQuantity(newQty);
                        System.out.println("✔️ Min quantity updated.");
                        currentMinQty = newQty;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                case "0" -> {
                    System.out.println("Returning to previous menu.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Ask the user a yes/no question and return true/false
    private boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }
}
