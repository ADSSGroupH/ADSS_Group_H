package Presentation;

import java.text.ParseException;

import dao.ItemDao;
import dao.SupplierDao;
import dao.ProductDao;
import java.util.*;
import Domain.*;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    private static final IRepositoryManager repositories = RepositoryManager.getInstance();
    private static final SystemController controller = SystemController.getInstance(repositories);

    public static void main(String[] args) throws ParseException {
        System.out.println("Welcome to the Supermarket Management System");

        // Data load option
        System.out.print("Initialize with sample data? (Y/N): ");
        String ans = scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("Y")) {
            loadDataFromDatabase(); // ← טען את הנתונים מה־DB
//            controller.initializeSampleData(); // ← אם יש פעולות חישוב/השלמה
            System.out.println("✔️ Data loaded successfully from database!");
        } else {
            System.out.println("Manual data entry selected.");
        }
        // User authentication
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            if (!repositories.getUserRepository().userExists(username)) {
                System.out.println("Username does not exist. Please try again.");
                continue;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (!repositories.getUserRepository().authenticate(username, password)) {
                System.out.println("Incorrect password. Please try again.");
                continue;
            }

            System.out.println("Login successful! Welcome, " + username);
            break;
        }

        // Create submenus
        SupplierMenu supplierMenu = new SupplierMenu(scanner, controller);
        AgreementMenu agreementMenu = new AgreementMenu(scanner, controller);
        ItemMenu itemMenu = new ItemMenu(scanner, controller);
        OrderMenu orderMenu = new OrderMenu(scanner, controller);
        ProductMenu productMenu = new ProductMenu(scanner, controller);
        DiscountMenu discountMenu = new DiscountMenu(scanner, controller);
        ReportMenu reportMenu = new ReportMenu(scanner, controller);

        while (true) {
            System.out.println("\n=== Supermarket Main Menu ===");
            System.out.println("1. Manage Suppliers");
            System.out.println("2. Manage Agreements");
            System.out.println("3. Manage Orders");
            System.out.println("4. Manage Product");
            System.out.println("5. Manage Item");
            System.out.println("6. Manage Discount");
            System.out.println("7. Manage Report");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> supplierMenu.display();
                case "2" -> agreementMenu.display();
                case "3" -> orderMenu.display();
                case "4" -> productMenu.display();
                case "5" -> itemMenu.display();
                case "6" -> discountMenu.display();
                case "7" -> reportMenu.display();
                case "0" -> {
                    System.out.println("Goodbye 👋 😎✌️");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public static void loadDataFromDatabase() {
        SupplierDao supplierDao = new SupplierDao();
        ProductDao productDao = new ProductDao();
        ItemDao itemDao = new ItemDao();

        List<Supplier> suppliers = supplierDao.getAllSuppliers();
        for (Supplier supplier : suppliers) {
            controller.addSupplier(
                    supplier.getSupplierId(),
                    supplier.getName(),
                    supplier.getDeliveryAddress(),
                    supplier.getBankAccount(),
                    supplier.getPaymentMethod(),
                    supplier.getContactPeople()
            );
        }

        List<Product> products = productDao.getAll();
        for (Product product : products) {
            if (repositories.getProductRepository().getProductByName(product.getName()) == null) {
                repositories.getProductRepository().addProduct(product);
            }
        }

        List<Item> items = itemDao.getAll();
        for (Item item : items) {
            repositories.getItemRepository().addItem(item);
        }

        System.out.println("✔️ Data loaded from DB successfully.");
    }

}

//
//    private static void seedTestData() {
//
//        List<ContactPerson> contacts1 = List.of(
//                controller.createContactPerson("David Cohen", "050-1234567", "david@example.com"));
//        controller.addSupplier("sup1", "Tnuva", "Tel Aviv", "123-456", PaymentMethod.CASH, contacts1);
//
//        List<ContactPerson> contacts2 = List.of(
//                controller.createContactPerson("Sarah Levi", "052-7654321", "sarah@example.com"));
//        controller.addSupplier("sup2", "Strauss", "Haifa", "654-321", PaymentMethod.CREDIT_CARD, contacts2);
//
//        Map<AgreementItem, Double> itemsMap1 = new HashMap<>();
//        itemsMap1.put(controller.createAgreementItem("P1", "cat100", 5.0f, 10.0f, 10, "milk"), 5.0);
//        itemsMap1.put(controller.createAgreementItem("P2", "cat101", 3.0f, 5.0f, 5, "bread"), 3.0);
//        List<DeliveryWeekday> days1 = List.of(DeliveryWeekday.SUNDAY, DeliveryWeekday.WEDNESDAY);
//        controller.addAgreementToSupplier("sup1", "agr1", true, days1, itemsMap1);
//
//        Map<AgreementItem, Double> itemsMap2 = new HashMap<>();
//        itemsMap2.put(controller.createAgreementItem("P3", "cat200", 8.0f, 12.0f, 7, "cheese"), 8.0);
//        itemsMap2.put(controller.createAgreementItem("P3", "cat201", 4.0f, 6.0f, 3, "butter"), 4.0);
//        itemsMap2.put(controller.createAgreementItem("P2", "cat202", 6.0f, 9.0f, 5, "juice"), 6.0);
//        List<DeliveryWeekday> days2 = List.of(DeliveryWeekday.MONDAY, DeliveryWeekday.THURSDAY);
//        controller.addAgreementToSupplier("sup2", "agr2", true, days2, itemsMap2);
//
//        Map<String, Integer> orderedItems1 = new HashMap<>();
//        orderedItems1.put("P1", 10);
//        orderedItems1.put("P2", 6);
//        Order order1 = new Order("ord1", controller.getSupplierById("sup1"), orderedItems1,
//                java.time.LocalDate.now().toString(), OrderStatus.PENDING);
//        controller.placeOrder(order1);
//
//        Map<String, Integer> orderedItems2 = new HashMap<>();
//        orderedItems2.put("P1", 5);
//        orderedItems2.put("P2", 3);
//        orderedItems2.put("P3", 4);
//        Order order2 = new Order("ord2", controller.getSupplierById("sup2"), orderedItems2,
//                java.time.LocalDate.now().toString(), OrderStatus.PENDING);
//        controller.placeOrder(order2);
//    }
//}