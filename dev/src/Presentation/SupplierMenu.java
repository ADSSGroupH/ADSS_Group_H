package Presentation;

import Domain.*;

import java.util.*;

public class SupplierMenu {
    private Scanner scanner;
    private final SystemController controller;

    // Constructor - setting up the supplier menu with scanner and controller
    public SupplierMenu(Scanner scanner, SystemController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    // Display the supplier management main menu
    public void display() {
        while (true) {
            System.out.println("\n=== Supplier Management Menu ===");
            System.out.println("1. Show all suppliers");
            System.out.println("2. Add new supplier");
            System.out.println("3. Select supplier to view/edit");
            System.out.println("4. Add agreement");
            System.out.println("5. View products in supplier agreements");
            System.out.println("0. Back to main menu");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> printAllSuppliers();
                case "2" -> addSupplier();
                case "3" -> selectSupplier();
                case "4" -> addAgreementToSupplier();
                case "5" -> viewItemsInSupplierAgreements();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Add a new supplier to the system
    private void addSupplier() {
        System.out.print("Supplier ID: ");
        String id = scanner.nextLine();

        if (controller.supplierExist(id)) {
            System.out.println("Supplier already exists.");
            return;
        }

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Delivery Address: ");
        String address = scanner.nextLine();
        System.out.print("Bank Account: ");
        String bank = scanner.nextLine();

        System.out.println("Select Payment Method:");
        PaymentMethod[] methods = PaymentMethod.values();
        for (int i = 0; i < methods.length; i++) {
            System.out.println((i + 1) + ". " + methods[i]);
        }

        int methodChoice = -1;
        while (methodChoice < 1 || methodChoice > methods.length) {
            System.out.print("Enter choice (1-" + methods.length + "): ");
            try {
                methodChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        PaymentMethod selectedPaymentMethod = methods[methodChoice - 1];

        List<ContactPerson> contacts = new ArrayList<>();
        int count = 1;
        while (true) {
            System.out.print("Would you like to add contact person " + count + "? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("n")) break;
            if (!answer.equals("y")) continue;

            System.out.print("Name: ");
            String contactName = scanner.nextLine();
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();

            contacts.add(controller.createContactPerson(contactName, phone, email));
            count++;
        }

        boolean success = controller.addSupplier(id, name, address, bank, selectedPaymentMethod, contacts);
        if (success) {
            System.out.println("Supplier added successfully.");
        } else {
            System.out.println("Supplier already exists.");
        }
    }

    // Show all suppliers registered in the system
    private void printAllSuppliers() {
        List<Supplier> suppliers = controller.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
            return;
        }
        int i = 1;
        for (Supplier s : suppliers) {
            System.out.println("Supplier " + i + " :");
            System.out.println(s);
            i++;
        }
    }

    // Select a supplier to view or edit its details
    private void selectSupplier() {
        System.out.print("Enter Supplier ID: ");
        String id = scanner.nextLine();

        Supplier s = controller.getSupplierById(id);
        if (s == null) {
            System.out.println("Supplier not found.");
            return;
        }

        while (true) {
            System.out.println("\n=== Edit Supplier: " + s.getSupplierId() + " ===");
            System.out.println("1. Change name");
            System.out.println("2. Change delivery address");
            System.out.println("3. Change bank account");
            System.out.println("4. Change payment method");
            System.out.println("5. Manage supplier contacts");
            System.out.println("0. Back");

            System.out.print("Your choice: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    controller.updateSupplierName(s, newName);
                }
                case "2" -> {
                    System.out.print("Enter new delivery address: ");
                    String newAddress = scanner.nextLine();
                    controller.updateSupplierAddress(s, newAddress);
                }
                case "3" -> {
                    System.out.print("Enter new bank account: ");
                    String newBank = scanner.nextLine();
                    controller.updateSupplierBankAccount(s, newBank);
                }
                case "4" -> {
                    System.out.println("Choose new payment method:");
                    PaymentMethod[] methods = PaymentMethod.values();
                    for (int i = 0; i < methods.length; i++) {
                        System.out.println((i + 1) + ". " + methods[i]);
                    }
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= methods.length) {
                        controller.updateSupplierPaymentMethod(s, methods[choice - 1]);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
                case "5" -> manageContacts(s);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }

            System.out.println("Supplier details updated.");
        }
    }

    // Add a new agreement for a specific supplier
    private void addAgreementToSupplier() {
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

                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty()) {
                    System.out.println("Product name cannot be empty.");
                    continue;
                }

                System.out.print("Catalog Number: ");
                String catalog = scanner.nextLine().trim();
                if (catalog.isEmpty()) {
                    System.out.println("Catalog number cannot be empty.");
                    continue;
                }

                System.out.print("Price: ");
                float price = Float.parseFloat(scanner.nextLine().trim());
                if (price < 0) {
                    System.out.println("Price cannot be negative.");
                    continue;
                }

                System.out.print("Discount (0–100%): ");
                float discount = Float.parseFloat(scanner.nextLine().trim());
                if (discount < 0 || discount > 100) {
                    System.out.println("Discount must be between 0 and 100.");
                    continue;
                }

                System.out.print("Min Quantity for Discount: ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity < 0) {
                    System.out.println("Minimum quantity cannot be negative.");
                    continue;
                }

                AgreementItem ai = controller.createAgreementItem(itemId, catalog, price, discount, quantity, name);
                items.put(ai, (double) price);

                itemCount++;

            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number. Please try again.");
            } catch (Exception e) {
                System.out.println("❌ Unexpected error: " + e.getMessage());
            }
        }

        boolean success = controller.addAgreementToSupplier(supplierId, agreementId, supportsDelivery, days, items);
        if (success) {
            System.out.println("✅ Agreement added successfully.");
        } else {
            System.out.println("❌ Failed to add agreement. Please check the supplier and try again.");
        }
    }
    // View all items included in a supplier's agreements
    private void viewItemsInSupplierAgreements() {
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

        System.out.println("\nProducts supplied by " + supplier.getName() + ":");

        int count = 1;
        for (Agreement agreement : agreements) {
            System.out.println("Agreement ID: " + agreement.getAgreementId());

            if (agreement.getItems().isEmpty()) {
                System.out.println("  No products in this agreement.");
                continue;
            }

            for (AgreementItem item : agreement.getItems().keySet()) {
                System.out.printf("  %d. product ID: %s | Name: %s | Catalog: %s | Price: %.2f | Discount: %.2f%% | Min Qty for Discount: %d\n",
                        count++,
                        item.getItemId(),
                        item.getName(),
                        item.getCatalogNumber(),
                        item.getPrice(1),
                        item.getDiscount(),
                        item.getquantityForDiscount());
            }
            System.out.println();
        }
    }

    // Manage (add, edit, remove) supplier's contact persons
    public void manageContacts(Supplier supplier) {
        while (true) {
            System.out.println("\n--- Manage Contact Persons for Supplier: " + supplier.getSupplierId() + " ---");
            var contacts = supplier.getContactPeople();

            if (contacts.isEmpty()) {
                System.out.println("No contact persons.");
            } else {
                showSupplierContacts(supplier);
                System.out.println("\n--- Menu Options ---");
            }

            System.out.println("1. Add contact person");
            System.out.println("2. Edit contact person");
            System.out.println("3. Remove contact person");
            System.out.println("0. Back");
            System.out.print("Your choice: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Phone: ");
                    String phone = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    supplier.addContactPerson(controller.createContactPerson(name, phone, email));
                    System.out.println("Contact added.");
                }
                case "2" -> {
                    if (contacts.isEmpty()) {
                        System.out.println("No contacts to edit.");
                        break;
                    }
                    showSupplierContacts(supplier);
                    int index = -1;
                    while (true) {
                        System.out.print("Enter number of contact to edit: ");
                        try {
                            index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                            if (index >= 0 && index < contacts.size()) {
                                break;
                            } else {
                                System.out.println("Invalid contact number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }

                    ContactPerson c = contacts.get(index);
                    System.out.print("New name (" + c.getName() + "): ");
                    c.setName(scanner.nextLine());
                    System.out.print("New phone (" + c.getPhoneNumber() + "): ");
                    c.setPhoneNumber(scanner.nextLine());
                    System.out.print("New email (" + c.getEmail() + "): ");
                    c.setEmail(scanner.nextLine());
                    System.out.println("Contact updated.");
                }
                case "3" -> {
                    System.out.print("Enter number of contact to remove: ");
                    int index = Integer.parseInt(scanner.nextLine()) - 1;
                    if (index >= 0 && index < contacts.size()) {
                        supplier.removeContactPerson(contacts.get(index));
                        System.out.println("Contact removed.");
                    } else {
                        System.out.println("Invalid contact number.");
                    }
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Ask the user a yes/no question
    private boolean askYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    // Show all contact persons of a supplier
    private void showSupplierContacts(Supplier supplier) {
        List<ContactPerson> contacts = supplier.getContactPeople();
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\nContact list:");
        for (int i = 0; i < contacts.size(); i++) {
            ContactPerson c = contacts.get(i);
            System.out.printf("%d. Name: %s | Phone: %s | Email: %s\n",
                    i + 1, c.getName(), c.getPhoneNumber(), c.getEmail());
        }
    }
}
