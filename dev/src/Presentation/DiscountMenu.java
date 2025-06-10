package Presentation;

import java.util.Scanner;
import Domain.*;
public class DiscountMenu {
    private final Scanner scanner;
    private final SystemController controller;

    public DiscountMenu(Scanner scanner, SystemController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    public void display() {
        int choice;
        do {
            System.out.println("\n=== Discounts & Promotions Management Menu ===");
            System.out.println("1. Add Supplier Discount");
            System.out.println("2. Remove Supplier Discount");
            System.out.println("3. Add Promotion");
            System.out.println("4. Remove Promotion");
            System.out.println("0. Back to Main Menu");

            choice = controller.promptInt(scanner);
            switch (choice) {
                case 1 -> controller.addSupplierDiscount();
                case 2 -> controller.removeSupplierDiscount();
                case 3 -> controller.addPromotion();
                case 4 -> controller.removePromotion();
                case 0 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }
}
