// Main.java
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // יצירת שכבות השירות
        AgreementService agreementService = new AgreementService();
        ItemService itemService = new ItemService();
        SupplierService supplierService = new SupplierService(scanner,itemService);
        OrderService orderService = new OrderService();

        // יצירת תפריטים (presentation layer)
        SupplierMenu supplierMenu = new SupplierMenu(scanner, supplierService);
        AgreementMenu agreementMenu = new AgreementMenu(scanner, supplierService, agreementService, itemService);
        ItemMenu itemMenu = new ItemMenu(scanner, itemService, supplierService);
        OrderMenu orderMenu = new OrderMenu(scanner, supplierService, orderService);

        // לולאת הפעלה
        while (true) {
            System.out.println("=== Supermarket Supply Management System ===");
            System.out.println("1. Manage Suppliers");
            System.out.println("2. Manage Agreements");
            System.out.println("3. Manage Orders");
            System.out.println("4. Manage Items");
            System.out.println("0. Exit");

            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> supplierMenu.display();
                case "2" -> agreementMenu.display();
                case "3" -> orderMenu.display();
                case "4" -> itemMenu.display();
                case "0" -> {
                    System.out.println("Goodbye");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}