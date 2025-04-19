// SupplierMenu.java
import java.util.*;

public class SupplierMenu {
    private Scanner scanner;
    private SupplierService supplierService;

    public SupplierMenu(Scanner scanner, SupplierService supplierService) {
        this.scanner = scanner;
        this.supplierService = supplierService;
    }

    public void display() {
        while (true) {
            System.out.println("=== Supplier Management Menu ===");
            System.out.println("1. Show all suppliers");
            System.out.println("2. Add new supplier");
            System.out.println("3. Select supplier to view/edit");
            System.out.println("4. Add agreement");
            System.out.println("0. Back to main menu");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> printAllSuppliers();
                case "2" -> addSupplier();
                case "3" -> selectSupplier();
                case "4" -> supplierService.addAgreementToSupplier();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void printAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
            return;
        }
        int i=1;
        for (Supplier s : suppliers) {
            System.out.println("Supplier " + i + " :" );
            System.out.println(s);
        }
    }

    private void addSupplier() {
        System.out.print("Supplier ID: ");
        String id = scanner.nextLine();
        if (supplierService.supplierExists(id)) {
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
            System.out.print("Would you like to add contact person " + count + "? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("no")) break;
            if (!answer.equals("yes")) continue;

            System.out.print("Name: ");
            String contactName = scanner.nextLine();
            System.out.print("Phone: ");
            String phone = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();

            contacts.add(new ContactPerson(contactName, phone, email));
            count++;
        }

        Supplier s = supplierService.createSupplier(id, name, address, bank, selectedPaymentMethod, contacts);
        supplierService.addSupplier(s);
        System.out.println("Supplier added successfully.");
    }

    private void selectSupplier() {
        System.out.print("Enter Supplier ID: ");
        String id = scanner.nextLine();
        Supplier s = supplierService.getSupplierById(id);
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
            System.out.println("5. Manage contact persons");
            System.out.println("0. Back");

            System.out.print("Your choice: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    s.setName(scanner.nextLine());
                }
                case "2" -> {
                    System.out.print("Enter new delivery address: ");
                    s.setDeliveryAddress(scanner.nextLine());
                }
                case "3" -> {
                    System.out.print("Enter new bank account: ");
                    s.setBankAccount(scanner.nextLine());
                }
                case "4" -> {
                    System.out.println("Choose payment method:");
                    PaymentMethod[] methods = PaymentMethod.values();
                    for (int i = 0; i < methods.length; i++) {
                        System.out.println((i + 1) + ". " + methods[i]);
                    }
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= methods.length) {
                        s.setPaymentMethod(methods[choice - 1]);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
                case "5" -> manageContacts(s);
                case "0" -> {
                    System.out.println("Returning to supplier menu.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }

            System.out.println("✔️ Supplier details updated.");
        }
    }

    public void manageContacts(Supplier supplier) {
        while (true) {
            System.out.println("--- Manage Contact Persons for Supplier: " + supplier.getSupplierId() + " ---");
            var contacts = supplier.getContactPeople();

            if (contacts.isEmpty()) {
                System.out.println("No contact persons.");
            } else {
                for (int i = 0; i < contacts.size(); i++) {
                    System.out.println(contacts.get(i));
                }
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
                    supplier.addContactPerson(new ContactPerson(name, phone, email));
                    System.out.println("Contact added.");
                }
                case "2" -> {
                    System.out.print("Enter number of contact to edit: ");
                    int index = Integer.parseInt(scanner.nextLine()) - 1;
                    if (index >= 0 && index < contacts.size()) {
                        ContactPerson c = contacts.get(index);
                        System.out.print("New name (" + c.getName() + "): ");
                        c.setName(scanner.nextLine());
                        System.out.print("New phone (" + c.getPhoneNumber() + "): ");
                        c.setPhoneNumber(scanner.nextLine());
                        System.out.print("New email (" + c.getEmail() + "): ");
                        c.setEmail(scanner.nextLine());
                        System.out.println("Contact updated.");
                    } else {
                        System.out.println("Invalid contact number.");
                    }
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
}

