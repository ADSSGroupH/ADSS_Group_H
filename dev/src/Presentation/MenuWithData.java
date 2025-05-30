//package Presentation;
//
//import Domain.*;
//
//import java.text.ParseException;
//import java.util.*;
//
//public class MenuWithData {
//    private static final Scanner scan = new Scanner(System.in);
//    IRepositoryManager repositories = RepositoryManager.getInstance();
//    SystemController Controller = SystemController.getInstance(repositories);
//
//    public static void main(String[] args) throws ParseException {
//        System.out.print("Initialize with sample data? (Y/N): ");
//        String ans = scan.nextLine().trim();
//        if (ans.equalsIgnoreCase("Y")) {
//            initializeSampleData();
//        }
//
//        int mainChoice;
//        do {
//            printMainMenu();
//            mainChoice = promptInt("Choose an option (1-5): ");
//            switch (mainChoice) {
//                case 1 -> handleProductsMenu();
//                case 2 -> handleItemsMenu();
//                case 3 -> handleDiscountsMenu();
//                case 4 -> handleReportsMenu();
//                case 5 -> System.out.println("Goodbye!");
//                default -> System.out.println("Invalid choice, please try again.");
//            }
//        } while (mainChoice != 5);
//
//        scan.close();
//    }
//
//    private static void printMainMenu() {
//        System.out.println("Main Menu:");
//        System.out.println("1. Manage Products");
//        System.out.println("2. Manage Items");
//        System.out.println("3. Manage Discounts & Promotions");
//        System.out.println("4. Reports");
//        System.out.println("5. Exit");
//    }
//
//    private static void handleProductsMenu() {
//        int choice;
//        do {
//            System.out.println("Manage Products:");
//            System.out.println("1. Add product");
//            System.out.println("2. Remove product");
//            System.out.println("3. Update Min Quantity");
//            System.out.println("4. Show product price by ID");
//            System.out.println("5. Update product price by ID");
//            System.out.println("6. Back to Main Menu");
//            choice = promptInt("Choose an option (1-6): ");
//            switch (choice) {
//                case 1 -> addProduct();
//                case 2 -> removeProduct();
//                case 3 -> updateMinQuantity();
//                case 4 -> showProductPrice();
//                case 5 -> updateProductPrice();
//                case 6 -> {}
//                default -> System.out.println("Invalid choice, please try again.");
//            }
//        } while (choice != 6);
//    }
//
//    private static void handleItemsMenu() throws ParseException {
//        int choice;
//        do {
//            System.out.println("Manage Items:");
//            System.out.println("1. Add item");
//            System.out.println("2. Remove item");
//            System.out.println("3. Show item details");
//            System.out.println("4. Mark item as defective");
//            System.out.println("5. Mark item as expired");
//            System.out.println("6. Back to Main Menu");
//            choice = promptInt("Choose an option (1-6): ");
//            switch (choice) {
//                case 1 -> addItem();
//                case 2 -> removeItem();
//                case 3 -> showItemDetails();
//                case 4 -> markDefective();
//                case 5 -> markExpired();
//                case 6 -> {}
//                default -> System.out.println("Invalid choice, please try again.");
//            }
//        } while (choice != 6);
//    }
//
//    private static void handleDiscountsMenu() {
//        int choice;
//        do {
//            System.out.println("Manage Discounts & Promotions:");
//            System.out.println("1. Add Supplier Discount");
//            System.out.println("2. Remove Supplier Discount");
//            System.out.println("3. Add Promotion");
//            System.out.println("4. Remove Promotion");
//            System.out.println("5. Back to Main Menu");
//            choice = promptInt("Choose an option (1-5): ");
//            switch (choice) {
//                case 1 -> addSupplierDiscount();
//                case 2 -> removeSupplierDiscount();
//                case 3 -> addPromotion();
//                case 4 -> removePromotion();
//                case 5 -> {}
//                default -> System.out.println("Invalid choice, please try again.");
//            }
//        } while (choice != 5);
//    }
//
//    private static void handleReportsMenu() {
//        int choice;
//        do {
//            System.out.println("Reports:");
//            System.out.println("1. List all products in stock");
//            System.out.println("2. Inventory report by category");
//            System.out.println("3. Defective items report");
//            System.out.println("4. Expired items report");
//            System.out.println("5. Back to Main Menu");
//            choice = promptInt("Choose an option (1-5): ");
//            switch (choice) {
//                case 1 -> showProductsInStock();
//                case 2 -> generateCategoryReport();
//                case 3 -> generateDefectiveReport();
//                case 4 -> generateExpiredReport();
//                case 5 -> {}
//                default -> System.out.println("Invalid choice, please try again.");
//            }
//        } while (choice != 5);
//    }
//
//    private static void initializeSampleData() {
//        Controller.initializeSampleData();
//    }
//
//    // --- Prompt Helpers ---
//
//    private static int promptInt(String prompt) {
//        while (true) {
//            System.out.print(prompt);
//            String line = scan.nextLine().trim();
//            try {
//                return Integer.parseInt(line);
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid number format, please try again.");
//            }
//        }
//    }
//
//    // --- Core Operations ---
//
//
//    private void addProduct() {
//        Controller.addProduct();
//    }
//
//    private void removeProduct() {
//        Controller.removeProduct();
//    }
//
//    private void addItem() throws ParseException {
//        Controller.addItem();
//    }
//
//    private void removeItem() {
//        Controller.removeItem();
//    }
//
//    private static void showProductsInStock() {
//        Controller.showProductsInStock();
//    }
//
//    private static void showItemDetails() {
//        Controller.showItemDetails();
//    }
//
//    private static void updateMinQuantity() {
//       Controller.updateMinQuantity();
//    }
//
//    private static void addSupplierDiscount() {
//        Controller.addSupplierDiscount();
//    }
//
//    private static void removeSupplierDiscount() {
//        Controller.removeSupplierDiscount();
//    }
//
//    private static void addPromotion() {
//        Controller.addPromotion();
//    }
//
//    private static void removePromotion() {
//        Controller.removePromotion();
//    }
//
//    private static void generateCategoryReport() {
//        Controller.generateCategoryReport();
//    }
//
//    private static void markDefective() {
//        Controller.markDefective();
//    }
//
//    private static void markExpired() {
//        Controller.markExpired();
//    }
//
//    private static void generateDefectiveReport() {
//        Controller.generateDefectiveReport();
//    }
//
//    private static void generateExpiredReport() {
//        Controller.generateExpiredReport();
//    }
//
//    private static void showProductPrice() {
//        Controller.showProductPrice();
//    }
//
//    private static void updateProductPrice() {
//        Controller.updateProductPrice();
//    }
//}
