package Presentation;

import java.util.Scanner;
import Domain.*;

public class ReportMenu {
    private final Scanner scanner;
    private final SystemController controller;

    public ReportMenu(Scanner scanner, SystemController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    public void display() {
        int choice;
        do {
            System.out.println("\n=== Reports Management Menu ===");
            System.out.println("1. Inventory report by category");
            System.out.println("2. Defective items report");
            System.out.println("3. Expired items report");
            System.out.println("0. Back to Main Menu");

            choice = controller.promptInt(scanner);
            switch (choice) {
                case 1 -> controller.generateCategoryReport();
                case 2 -> controller.generateDefectiveReport();
                case 3 -> controller.generateExpiredReport();
                case 0 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }

}