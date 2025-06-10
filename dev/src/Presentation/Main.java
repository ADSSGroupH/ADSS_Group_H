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
            controller.initializeSampleData(); // ← אם יש פעולות חישוב/השלמה
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

        // 1. Load suppliers only into in‑memory repo
        List<Supplier> suppliers = supplierDao.getAllSuppliers();
        for (Supplier s : suppliers) {
            repositories.getSupplierRepository().add(s);
        }

        // 2. Load products
        for (Product p : productDao.getAllProducts()) {
            if (repositories.getProductRepository().getProductByName(p.getName()) == null) {
                repositories.getProductRepository().addProduct(p);
            }
        }

        // 3. Load items
        for (Item it : itemDao.getAll()) {
            repositories.getItemRepository().addItem(it);
        }

        System.out.println("✔️ Data loaded from DB successfully.");
    }
}

