package Presentation;

import Domain.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ItemMenu {
    private final Scanner scanner;
    private final SystemController controller;

    public ItemMenu(Scanner scanner, SystemController controller) {
        this.scanner = scanner;
        this.controller = controller;
    }

    public void display() throws ParseException {
        int choice;
        do {
            System.out.println("\n=== Item Management Menu ===");
            System.out.println("1. Add item");
            System.out.println("2. Remove item");
            System.out.println("3. Show item details");
            System.out.println("4. Mark item as defective");
            System.out.println("5. Mark item as expired");
            System.out.println("6. Update item");
            System.out.println("0. Back to Main Menu");
            choice = controller.promptInt(scanner);
            switch (choice) {
                case 1 -> controller.addItem();
                case 2 -> controller.removeItem();
                case 3 -> controller.showItemDetails();
                case 4 -> controller.markDefective();
                case 5 -> controller.markExpired();
                case 6 -> updateItemMenu();
                case 0 -> {}
                default -> System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 0);
    }

    private void updateItemMenu() {
        System.out.print("Enter Item ID to update: ");
        String iid = scanner.nextLine().trim();

        // כאן כבר מחזירים Item ולא Product
        Item item = controller.getItemByIid(iid);
        if (item == null) {
            System.out.println("❌ Item not found.");
            return;
        }
        int sub;
        do {
            System.out.println("\n--- Update Item " + item.getName() + " (ID: " + iid + ") ---");
            System.out.println("1. Change name");
            System.out.println("2. Change location");
            System.out.println("3. Change expiration date");
            System.out.println("4. Change classification");
            System.out.println("5. Change defect status");
            System.out.println("0. Back");
            sub = controller.promptInt(scanner);

            switch (sub) {
                case 1 -> {
                    System.out.print("New name: ");
                    String newName = scanner.nextLine().trim();
                    if (!newName.isEmpty() && controller.updateItemName(item, newName)) {
                        System.out.println("✔️ Name updated to " + newName);
                    } else {
                        System.out.println("❌ Update failed.");
                    }
                }
                case 2 -> {
                    System.out.print("New location (0=WareHouse,1=Store): ");
                    int locChoice = controller.parseIntSafe(scanner.nextLine());
                    Location newLoc = (locChoice == 0) ? Location.WareHouse : Location.Store;
                    if (controller.updateItemLocation(item, newLoc)) {
                        System.out.println("✔️ Location updated to " + newLoc);
                    } else {
                        System.out.println("❌ Update failed.");
                    }
                }
                case 3 -> {
                    System.out.print("New Expiration Date (yyyy-MM-dd): ");
                    try {
                        Date newDate = new SimpleDateFormat("yyyy-MM-dd")
                                .parse(scanner.nextLine().trim());
                        if (controller.updateItemExpirationDate(item, newDate)) {
                            System.out.println("✔️ Expiration date updated.");
                        } else {
                            System.out.println("❌ Update failed.");
                        }
                    } catch (Exception e) {
                        System.out.println("❌ Invalid date format.");
                    }
                }
                case 4 -> {
                    System.out.print("New Classification ID: ");
                    String cid = scanner.nextLine().trim();
                    System.out.print("Category: ");
                    String cat = scanner.nextLine().trim();
                    System.out.print("Subcategory: ");
                    String subcat = scanner.nextLine().trim();
                    System.out.print("Size: ");
                    double size = Double.parseDouble(scanner.nextLine().trim());
                    Classification cls = new Classification(cid, cat, subcat, size);
                    if (controller.updateItemClassification(item, cls)) {
                        System.out.println("✔️ Classification updated.");
                    } else {
                        System.out.println("❌ Update failed.");
                    }
                }
                case 5 -> {
                    System.out.print("Defect status (y/n): ");
                    boolean isDef = scanner.nextLine().trim().equalsIgnoreCase("y");
                    if (controller.updateItemDefectStatus(item, isDef)) {
                        System.out.println("✔️ Defect status set to " + isDef);
                    } else {
                        System.out.println("❌ Update failed.");
                    }
                }
                case 0 -> { /* back */ }
                default -> System.out.println("Invalid choice.");
            }
        } while (sub != 0);
    }
}
