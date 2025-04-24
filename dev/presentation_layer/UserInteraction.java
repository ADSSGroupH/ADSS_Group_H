package dev.presentation_layer;
import java.util.ArrayList;
import java.util.List;

import dev.domain_layer.*;

import java.util.Scanner;

public class UserInteraction {
    private UserController userController = new UserController();              // אחראי על התחברות וניהול משתמשים
    private TransportationController transportationController = new TransportationController(); // אחראי על כל הפעולות של משלוחים
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Welcome to the Transportation Management System!");

        while (true) {
            if (!login()) return;

            String role = userController.getCurrentUserRole();

            switch (role) {
                case "SystemManager" -> systemManagerMenu();
                case "transportationManager" -> transportationManagerMenu();
                case "Driver" -> driverMenu();
                default -> System.out.println("Unknown role.");
            }
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
            System.out.println("5. Make transportation");
            System.out.println("6. Change transportation date");
            System.out.println("7. Change departure time");
            System.out.println("8. Change truck in transportation");
            System.out.println("9. Change driver in transportation");
            System.out.println("10. Change shipment areas");
            System.out.println("11. Change origin");
            System.out.println("12. Change succeeded status");
            System.out.println("13. Add items");
            System.out.println("14. Remove items");
            System.out.println("15. Change items document");
            System.out.println("16. Display all transportations");
            System.out.println("17. Display all trucks");
            System.out.println("18. Display all drivers");
            System.out.println("19. Report transportation success");
            System.out.println("20. Add site");
            System.out.println("21. Remove truck");
            System.out.println("22. Remove driver");
            System.out.println("23. Logout");
            System.out.println("0. Exit system");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addDriver();
                case 2 -> addTruck();
                case 3 -> createShipmentArea();
                case 4 -> changeShipmentArea();
                case 5 -> makeTransportation();
                case 6 -> changeDate();
                case 7 -> changeDepartureTime();
                case 8 -> changeTruck();
                case 9 -> changeDriver();
                case 10 -> changeShipmentAreasID();
                case 11 -> changeOrigin();
                case 12 -> changeSucceeded();
                case 13 -> addItems();
                case 14 -> removeItems();
                case 15 -> changeItemsDocument();
                case 16 -> displayAllTransportations();
                case 17 -> displayTrucks();
                case 18 -> displayDrivers();
                case 19 -> reportTransportationSuccess();
                case 20 -> addSite();
                case 21 -> removeTruck();
                case 22 -> removeDriver();
                case 23 -> {
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
        while (true) {
            System.out.println("\n[Driver Menu]");
            System.out.println("1. Report accident");
            System.out.println("2. View transportation document");
            System.out.println("3. View items list");
            System.out.println("4. Logout");
            System.out.println("0. Exit system");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> reportAccident();
                case 2 -> displayTransportationDocument();
                case 3 -> displayItemsList();
                case 4 -> {
                    System.out.println(userController.logout());
                    return;
                }
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
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


    private void reportAccident() {
        System.out.print("Enter transportation ID: ");
        int transportationId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Describe the accident: ");
        String accident = scanner.nextLine();

        String result = transportationController.reportAccident(transportationId, accident);
        System.out.println(result);
    }

    private void makeTransportation() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter date (e.g., 2025-05-01): ");
        String date = scanner.nextLine();

        System.out.print("Enter departure time (e.g., 08:00): ");
        String departureTime = scanner.nextLine();

        System.out.print("Enter truck plate number: ");
        String truckPlate = scanner.nextLine();

        System.out.print("Enter driver ID: ");
        int driverId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter origin site name: ");
        String siteName = scanner.nextLine();

        System.out.print("Enter origin site address: ");
        String address = scanner.nextLine();

        System.out.print("Enter origin site phone: ");
        String phone = scanner.nextLine();

        System.out.print("Enter origin site contact name: ");
        String contact = scanner.nextLine();

        System.out.print("Enter origin site shipment area ID: ");
        int areaId = scanner.nextInt();
        scanner.nextLine();

        Site origin = new Site(siteName, address, phone, contact, areaId);

        // empty lists to allow basic creation without item logic yet
        List<ItemsDocument> itemsDocs = new ArrayList<>();
        List<Integer> shipmentAreas = new ArrayList<>();
        shipmentAreas.add(areaId);

        String result = transportationController.makeTransportation(id, date, departureTime, truckPlate, driverId, itemsDocs, shipmentAreas, origin);
        System.out.println(result);
    }

    private void changeDate() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new date: ");
        String date = scanner.nextLine();

        System.out.println(transportationController.changeDate(id, date));
    }

    private void changeTruck() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new truck plate: ");
        String plate = scanner.nextLine();

        System.out.println(transportationController.changeTruckPlateNumber(id, plate));
    }

    private void changeDriver() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new driver ID: ");
        int driverId = scanner.nextInt();
        scanner.nextLine();

        System.out.println(transportationController.changeDriverID(id, driverId));
    }

    private void displayAllTransportations() {
        System.out.println(transportationController.displayAllTransportations());
    }

    private void displayTrucks() {
        System.out.println(transportationController.displayTrucks());
    }

    private void displayDrivers() {
        System.out.println(transportationController.displayDrivers());
    }

    private void addItems() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        // אין קלט למסמכים כי לא בנינו ממשק לפריטים עדיין
        List<ItemsDocument> itemsToAdd = new ArrayList<>();
        System.out.println("[Simulation] Empty ItemsDocument list added.");

        String result = transportationController.addItems(id, itemsToAdd);
        System.out.println(result);
    }

    private void removeItems() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        List<ItemsDocument> itemsToRemove = new ArrayList<>();
        System.out.println("[Simulation] Empty ItemsDocument list removed.");

        String result = transportationController.removeItems(id, itemsToRemove);
        System.out.println(result);
    }

    private void changeItemsDocument() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        List<ItemsDocument> newDocs = new ArrayList<>();
        System.out.println("[Simulation] Replaced with empty ItemsDocument list.");

        String result = transportationController.changeItemsDocument(id, newDocs);
        System.out.println(result);
    }

    private void changeOrigin() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new origin site name: ");
        String name = scanner.nextLine();

        System.out.print("Enter site address: ");
        String address = scanner.nextLine();

        System.out.print("Enter site phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter contact person name: ");
        String contact = scanner.nextLine();

        System.out.print("Enter shipment area ID: ");
        int shipmentAreaId = scanner.nextInt();
        scanner.nextLine();

        Site newOrigin = new Site(name, address, phone, contact, shipmentAreaId);
        String result = transportationController.changeOrigin(id, newOrigin);
        System.out.println(result);
    }

    private void reportTransportationSuccess() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String result = transportationController.reportTransportationSuccess(id);
        System.out.println(result);
    }

    private void displayTransportationDocument() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String result = transportationController.displayTransportationDocument(id);
        System.out.println(result);
    }

    private void displayItemsList() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Transportation t = transportationController.findTransportationById(id); // נדרשת חשיפה של הפונקציה
        if (t != null) {
            String result = transportationController.displayItemsList(t);
            System.out.println(result);
        } else {
            System.out.println("Transportation with ID " + id + " not found.");
        }
    }

    private void changeDepartureTime() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new departure time: ");
        String time = scanner.nextLine();

        System.out.println(transportationController.changeDepartureTime(id, time));
    }

    private void changeSucceeded() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Was it successful? (true/false): ");
        boolean success = scanner.nextBoolean();
        scanner.nextLine();

        System.out.println(transportationController.changeSucceeded(id, success));
    }

    private void changeShipmentAreasID() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new shipment area IDs separated by commas: ");
        String input = scanner.nextLine();
        String[] tokens = input.split(",");
        List<Integer> areaIDs = new ArrayList<>();
        for (String token : tokens) {
            areaIDs.add(Integer.parseInt(token.trim()));
        }

        System.out.println(transportationController.changeShipmentAreasID(id, areaIDs));
    }

    private void addSite() {
        System.out.print("Enter site name: ");
        String name = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        System.out.print("Enter contact person name: ");
        String contact = scanner.nextLine();

        System.out.print("Enter shipment area ID: ");
        int shipmentAreaId = scanner.nextInt();
        scanner.nextLine();

        String result = transportationController.addSite(name, address, phone, contact, shipmentAreaId);
        System.out.println(result);
    }

    private void removeTruck() {
        System.out.print("Enter truck plate number to remove: ");
        String plate = scanner.nextLine();
        System.out.println(transportationController.removeTruck(plate));
    }

    private void removeDriver() {
        System.out.print("Enter driver ID to remove: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println(transportationController.removeDriver(id));
    }
    public static void main(String[] args) {
        UserInteraction ui = new UserInteraction(); // יוצרים מופע של הממשק
        ui.run();
    }
}

