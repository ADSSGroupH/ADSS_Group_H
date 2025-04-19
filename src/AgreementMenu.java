// AgreementMenu.java
import java.util.*;

public class AgreementMenu {
    private final Scanner scanner;
    private final SupplierService supplierService;
    private final AgreementService agreementService;
    private final ItemService itemService;

    public AgreementMenu(Scanner scanner, SupplierService supplierService, AgreementService agreementService, ItemService itemService) {
        this.scanner = scanner;
        this.supplierService = supplierService;
        this.agreementService = agreementService;
        this.itemService = itemService;
    }

    public void display() {
        while (true) {
            System.out.println("=== Agreement Management Menu ===");
            System.out.println("1. Show all agreements");
            System.out.println("2. Add new agreement");
            System.out.println("0. Back to main menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> showAllAgreements();
                case "2" -> addAgreement();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void showAllAgreements() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
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

    public void addAgreement() {
        System.out.print("Supplier ID: ");
        String supplierId = scanner.nextLine();
        Supplier supplier = supplierService.getSupplierById(supplierId);
        if (supplier == null) {
            System.out.println("Supplier not found.");
            return;
        }

        System.out.print("Agreement ID: ");
        String agreementId = scanner.nextLine();
        System.out.print("Start date: ");
        String start = scanner.nextLine();
        System.out.print("End date: ");
        String end = scanner.nextLine();
        System.out.print("Supports delivery? (true/false): ");
        boolean supportsDelivery = Boolean.parseBoolean(scanner.nextLine());

        List<DeliveryWeekday> days = new ArrayList<>();
        if (supportsDelivery) {
            System.out.println("Enter delivery days (names, comma separated, e.g., sunday, tuesday): ");
            String[] tokens = scanner.nextLine().split(",");
            for (String t : tokens) {
                try {
                    days.add(DeliveryWeekday.fromString(t.trim()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid day name: " + t.trim());
                }
            }
        }

        Map<AgreementItem, Double> items = new HashMap<>();
        int itemCount = 1;
        while (true) {
            System.out.print("Would you like to add item " + itemCount + "? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("no")) break;
            if (!answer.equals("yes")) continue;

            System.out.print("Supermarket Item ID: ");
            String itemId = scanner.nextLine();

            System.out.print("Item name (for new item if needed): ");
            String itemName = scanner.nextLine();
            itemService.ensureItemExists(itemId, itemName);

            System.out.print("Catalog Number (supplier-specific): ");
            String catalog = scanner.nextLine();
            System.out.print("Price: ");
            float price = Float.parseFloat(scanner.nextLine());
            System.out.print("Discount (0-1): ");
            float discount = Float.parseFloat(scanner.nextLine());
            System.out.print("Min quantity for discount: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            AgreementItem ai = agreementService.createAgreementItem(itemId, catalog, price, discount, quantity);
            items.put(ai, (double) price);
            itemCount++;
        }

        Agreement agreement = agreementService.createAgreement(agreementId, supplierId, days, supportsDelivery, start, end, items);
        System.out.println("Agreement added.");
    }

}
