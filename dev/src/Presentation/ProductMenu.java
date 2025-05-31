package Presentation;

import Domain.*;

import java.util.Map;
import java.util.Scanner;

public class ProductMenu {
    private final Scanner scanner;
    private final SystemController controller;

    public ProductMenu(Scanner scanner, SystemController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    public void display() {
        int choice;
        do {
            System.out.println("\n=== Product Management Menu ===");
            System.out.println("1. List all products in stock");
            System.out.println("2. Add product");
            System.out.println("3. Remove product");
            System.out.println("4. Update Min Quantity");
            System.out.println("5. Show product price by ID");
            System.out.println("6. Update product price by ID");
            System.out.println("7. View suppliers for product");
            System.out.println("0. Back to Main Menu");
            choice = controller.promptInt(scanner);
            switch (choice) {
                case 1 -> controller.showProductsInStock();
                case 2 -> controller.addProduct();
                case 3 -> controller.removeProduct();
                case 4 -> controller.updateMinQuantity();
                case 5 -> controller.showProductPrice();
                case 6 -> controller.updateProductPrice();
                case 7 -> controller.viewSuppliersForProducts(scanner);

                case 0 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }
}