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
            System.out.println("4. Update product fields");
            System.out.println("5. Delete product");
            System.out.println("0. Back to Main Menu");
            choice = controller.promptInt(scanner);
            switch (choice) {
                case 1 -> controller.showProductsInStock();
                case 2 -> {
                    // קוד אינטראקטיבי ל‑add:
                    System.out.print("Product ID: ");
                    String pid = scanner.nextLine().trim();
                    System.out.print("Name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Cost Price: ");
                    double cp = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Sale Price: ");
                    double sp = Double.parseDouble(scanner.nextLine().trim());
                    System.out.print("Manufacturer: ");
                    String manu = scanner.nextLine().trim();
                    System.out.print("Min Quantity: ");
                    int minQ = Integer.parseInt(scanner.nextLine().trim());
                    boolean ok = controller.addProduct(pid, name, cp, sp, manu, minQ);
                    System.out.println(ok ? "✅ Product added." : "❌ Failed to add product.");
                }
                case 3 -> {
                    System.out.print("Enter Product ID to delete: ");
                    String pid = scanner.nextLine().trim();
                    System.out.print("Confirm delete? (y/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                        boolean ok = controller.deleteProduct(pid);
                        System.out.println(ok ? "✅ Product deleted." : "❌ Failed to delete.");
                    } else {
                        System.out.println("Cancelled.");
                    }
                }
                case 4 -> updateProductMenu();
                case 5 -> deleteProductInteractive();
                case 0 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }

    // תפריט משנה לעדכון שדות
    private void updateProductMenu() {
        System.out.print("Enter Product ID to update: ");
        String pid = scanner.nextLine().trim();
        Product p = controller.getProductById(pid);
        if (p == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("1. Change name");
        System.out.println("2. Change cost price");
        System.out.println("3. Change sale price");
        System.out.println("4. Change min quantity");
        System.out.println("0. Back");
        System.out.print("Choice: ");
        String c = scanner.nextLine().trim();
        boolean ok = false;
        switch (c) {
            case "1" -> {
                System.out.print("New name: ");
                ok = controller.updateProductName(p, scanner.nextLine().trim());
            }
            case "2" -> {
                System.out.print("New cost price: ");
                ok = controller.updateProductCostPrice(p, Double.parseDouble(scanner.nextLine().trim()));
            }
            case "3" -> {
                System.out.print("New sale price: ");
                ok = controller.updateProductSalePrice(p, Double.parseDouble(scanner.nextLine().trim()));
            }
            case "4" -> {
                System.out.print("New min quantity: ");
                ok = controller.updateProductMinQuantity(p, Integer.parseInt(scanner.nextLine().trim()));
            }
            case "0" -> { return; }
            default -> System.out.println("Invalid choice.");
        }
        System.out.println(ok ? "✅ Updated." : "❌ Update failed.");
    }

    private void deleteProductInteractive() {
        System.out.print("Enter Product ID to delete (persist): ");
        String pid = scanner.nextLine().trim();
        System.out.print("Confirm delete? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("y")) {
            boolean ok = controller.deleteProduct(pid);
            System.out.println(ok ? "✅ Product deleted." : "❌ Failed to delete.");
        } else {
            System.out.println("Cancellation confirmed.");
        }
    }


}