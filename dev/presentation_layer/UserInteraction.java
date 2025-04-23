package dev.presentation_layer;

import dev.domain_layer.*;

import java.util.Scanner;

public class UserInteraction {
    private UserController userController = new UserController();              // אחראי על התחברות וניהול משתמשים
    private TransportationController transportationController = new TransportationController(); // אחראי על כל הפעולות של משלוחים
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Welcome to the Transportation Management System!");

        if (!login()) return;

        // נזהה את התפקיד של המשתמש המחובר כדי לנווט אותו לתפריט שלו
        String role = userController.getCurrentUserRole();

        if (role.equals("SystemManager")) {
            systemManagerMenu();
        } else if (role.equals("transportationManager")) {
            transportationManagerMenu();
        } else if (role.equals("Driver")) {
            driverMenu();
        } else {
            System.out.println("Unknown role.");
        }
    }


    private boolean login() {
        System.out.println("Please log in");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        String response = userController.login(username, password);
        System.out.println(response);

        return response.equals("Login successful");
    }


    private void systemManagerMenu() {
        while (true) {
            System.out.println("\n[System Manager Menu]");
            System.out.println("1. Add user");
            System.out.println("2. Delete user");
            System.out.println("3. Logout");
            System.out.println("0. Exit system");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addUser();
                case 2 -> deleteUser();
                case 3 -> {
                    System.out.println(userController.logout());
                    return;
                }
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // תפריט בסיסי למנהל תחבורה
    private void transportationManagerMenu() {
        while (true) {
            System.out.println("\n[Transportation Manager Menu]");
            System.out.println("1. Add driver");
            System.out.println("2. Add truck");
            System.out.println("3. Create shipment area");
            System.out.println("4. Change shipment area");
            System.out.println("5. Logout");
            System.out.println("0. Exit system");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addDriver();
                case 2 -> addTruck();
                case 3 -> createShipmentArea();
                case 4 -> changeShipmentArea();
                case 5 -> {
                    System.out.println(userController.logout());
                    return;
                }
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // תפריט בסיסי לנהג
    private void driverMenu() {
        System.out.println("\n[Driver Menu]");

    }

    private void addUser() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter role (SystemManager / transportationManager / Driver): ");
        String roleStr = scanner.nextLine();

        User.Role role = User.Role.valueOf(roleStr);
        String result = userController.addUser(username, password, role);
        System.out.println(result);
    }

    private void deleteUser() {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();
        String result = userController.deleteUser(username);
        System.out.println(result);
    }
    private void addDriver() {
        System.out.print("Enter driver ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter driver name: ");
        String name = scanner.nextLine();

        System.out.print("Enter license type (A/B/C/D/E): ");
        String license = scanner.nextLine();

        Driver.LicenseType licenseType = Driver.LicenseType.valueOf(license);
        String result = transportationController.addDriver(id, name, licenseType);
        System.out.println(result);
    }

    private void addTruck() {
        System.out.print("Enter plate number: ");
        String plate = scanner.nextLine();

        System.out.print("Enter model: ");
        String model = scanner.nextLine();

        System.out.print("Enter net weight: ");
        int net = scanner.nextInt();

        System.out.print("Enter max weight: ");
        int max = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter license type (A/B/C/D/E): ");
        String license = scanner.nextLine();

        Driver.LicenseType licenseType = Driver.LicenseType.valueOf(license);
        String result = transportationController.addTruck(plate, model, net, max, licenseType);
        System.out.println(result);
    }

    private void createShipmentArea() {
        System.out.print("Enter shipment area ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter shipment area name: ");
        String name = scanner.nextLine();

        String result = transportationController.makeShipmentArea(id, name);
        System.out.println(result);
    }

    private void changeShipmentArea() {
        System.out.print("Enter shipment area ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();

        String result = transportationController.changeShipmentArea(id, newName);
        System.out.println(result);
    }


    public static void main(String[] args) {
        UserInteraction ui = new UserInteraction(); // יוצרים מופע של הממשק
        ui.run();
    }
}

