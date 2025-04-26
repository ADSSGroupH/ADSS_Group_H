package Presentation;

import java.util.*;
import Domain.*;
import controller.SystemController;
import Service.UserManager;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create user manager
        UserManager userManager = new UserManager();

        System.out.println("Welcome to the Supermarket Supply Management System");

        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            if (!userManager.userExists(username)) {
                System.out.println("Username does not exist. Please try again.");
                continue;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (!userManager.authenticate(username, password)) {
                System.out.println("Incorrect password. Please try again.");
                continue; // return to ask for username
            }

            System.out.println("Login successful! Welcome, " + username);
            break; // login succeed, break loop
        }

        // Create central controller
        SystemController controller = new SystemController();
        seedTestData(controller);

        // Create menus
        SupplierMenu supplierMenu = new SupplierMenu(scanner, controller);
        AgreementMenu agreementMenu = new AgreementMenu(scanner, controller);
        ItemMenu itemMenu = new ItemMenu(scanner, controller);
        OrderMenu orderMenu = new OrderMenu(scanner, controller);

        while (true) {
            System.out.println("\n=== Supermarket Supply Management System ===");
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
                    System.out.println("Goodbye 👋 😎✌️");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void seedTestData(SystemController controller) {
        // Items
        controller.addItem("item1", "Milk");
        controller.addItem("item2", "Bread");
        controller.addItem("item3", "Eggs");
        controller.addItem("item4", "Cheese");
        controller.addItem("item5", "Butter");
        controller.addItem("item6", "Juice");

        // Suppliers
        List<ContactPerson> contacts1 = List.of(
                controller.createContactPerson("David Cohen", "050-1234567", "david@example.com"));
        controller.addSupplier("sup1", "Tnuva", "Tel Aviv", "123-456", PaymentMethod.CASH, contacts1);

        List<ContactPerson> contacts2 = List.of(
                controller.createContactPerson("Sarah Levi", "052-7654321", "sarah@example.com"));
        controller.addSupplier("sup2", "Strauss", "Haifa", "654-321", PaymentMethod.CREDIT_CARD, contacts2);

        // Agreements
        Map<AgreementItem, Double> itemsMap1 = new HashMap<>();
        itemsMap1.put(controller.createAgreementItem("item1", "cat100", 5.0f, 10.0f, 10, "milk"), 5.0);
        itemsMap1.put(controller.createAgreementItem("item2", "cat101", 3.0f, 5.0f, 5, "bread"), 3.0);
        List<DeliveryWeekday> days1 = List.of(DeliveryWeekday.SUNDAY, DeliveryWeekday.WEDNESDAY);
        controller.addAgreementToSupplier("sup1", "agr1", true, days1, itemsMap1);

        Map<AgreementItem, Double> itemsMap2 = new HashMap<>();
        itemsMap2.put(controller.createAgreementItem("item4", "cat200", 8.0f, 12.0f, 7, "cheese"), 8.0);
        itemsMap2.put(controller.createAgreementItem("item5", "cat201", 4.0f, 6.0f, 3, "butter"), 4.0);
        itemsMap2.put(controller.createAgreementItem("item6", "cat202", 6.0f, 9.0f, 5, "juice"), 6.0);
        List<DeliveryWeekday> days2 = List.of(DeliveryWeekday.MONDAY, DeliveryWeekday.THURSDAY);
        controller.addAgreementToSupplier("sup2", "agr2", true, days2, itemsMap2);

        // Orders
        Map<String, Integer> orderedItems1 = new HashMap<>();
        orderedItems1.put("item1", 10);
        orderedItems1.put("item2", 6);
        Order order1 = new Order("ord1", controller.getSupplierById("sup1"), orderedItems1,
                java.time.LocalDate.now().toString(), 69, OrderStatus.PENDING);
        controller.placeOrder(order1);

        Map<String, Integer> orderedItems2 = new HashMap<>();
        orderedItems2.put("item4", 5);
        orderedItems2.put("item5", 3);
        orderedItems2.put("item6", 4);
        Order order2 = new Order("ord2", controller.getSupplierById("sup2"), orderedItems2,
                java.time.LocalDate.now().toString(), 85, OrderStatus.PENDING);
        controller.placeOrder(order2);

    }

}
