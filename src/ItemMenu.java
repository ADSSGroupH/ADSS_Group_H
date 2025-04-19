import java.util.*;

public class ItemMenu {
    private Scanner scanner;
    private final ItemService itemService;
    private final SupplierService supplierService;

    public ItemMenu(Scanner scanner, ItemService itemService, SupplierService supplierService) {
        this.scanner = scanner;
        this.itemService = itemService;
        this.supplierService = supplierService;

    }

    public void display() {
        while (true) {
            System.out.println("\n=== Item Management Menu ===");
            System.out.println("1. Show all items");
            System.out.println("2. View suppliers for a specific item");
            System.out.println("0. Back to main menu");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> itemService.printAllItems();
                case "2" -> viewItemSuppliers();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewItemSuppliers() {
        System.out.print("Enter item ID: ");
        String itemId = scanner.nextLine();

        Item item = itemService.getItemById(itemId);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.println("Item: " + item.getName() + " (" + item.getItemId() + ")");
        boolean found = false;

        Map<Supplier, String> itemSuppliers = itemService.getSuppliersForItem(itemId, supplierService.getAllSuppliers());

        for (Map.Entry<Supplier, String> entry : itemSuppliers.entrySet()) {
            Supplier supplier = entry.getKey();
            String catalogNumber = entry.getValue();

            for (Agreement agreement : supplier.getAgreements()) {
                for (AgreementItem agreementItem : agreement.getItems().keySet()) {
                    if (agreementItem.getCatalogNumber().equals(itemId)) {
                        found = true;
                        System.out.println("Supplier: " + supplier.getName() + " (" + supplier.getSupplierId() + ")");
                        System.out.println("  Catalog #: " + agreementItem.getCatalogNumber());
                        System.out.println("  Price: " + agreementItem.getPrice(1));
                        System.out.println("  Discount: " + (agreementItem.getDiscount() * 100) + "%");
                        System.out.println("  Min Quantity for Discount: " + agreementItem.getquantityForDiscount());
                        System.out.println("------------------------------------");
                    }
                }
            }
        }

        if (!found) {
            System.out.println("No suppliers found for this item.");
        }
    }
}