package PresentationLayer;
<<<<<<< HEAD
/*package presentation_layer;
=======
>>>>>>> 52972888becfac149da20316b226d9cf1f21354e
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import DomainLayer.User;
import DomainLayer.UserController;
import DomainLayer.transportationDomain.Driver;
import DomainLayer.transportationDomain.Item;
import DomainLayer.transportationDomain.ItemsDocument;
import DomainLayer.transportationDomain.Site;
import DomainLayer.transportationDomain.Transportation;
import DomainLayer.transportationDomain.TransportationController;

public class UserInteraction {
    private UserController userController = new UserController();              
    private TransportationController transportationController = new TransportationController(); 
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Welcome to the Transportation Management System!");

        while (true) {
            if (!login()) return;

            String role = userController.getCurrentUserRole();

            switch (role) {
                case "SystemManager":
                    systemManagerMenu();
                    break;
                case "transportationManager":
                    transportationManagerMenu();
                    break;
                case "Driver":
                    driverMenu();
                    break;
                default:
                    System.out.println("Unknown role.");
                    break;
            }
        }
    }


    private boolean login() {
        System.out.println("Please log in");

        while (true) {
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            String response = userController.login(username, password);
            System.out.println(response);

            if (response.equals("Login successful")) {
                return true;
            } else {
                System.out.println("Please try again.");
            }
        }
    }



    private void systemManagerMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n[System Manager Menu]");
            System.out.println("1. Add user");
            System.out.println("2. Delete user");
            System.out.println("3. Logout");
            System.out.println("0. Exit system");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: {
                    addUser();
                    break;
                }
                case 2: {
                    deleteUser();
                    break;
                }
                case 3: {
                    System.out.println(userController.logout());
                    loggedIn = false;
                    break;
                }
                case 0:{
                    System.exit(0);
                    break;
                } 
                default: System.out.println("Invalid choice.");
            }
        }
    }


    private void transportationManagerMenu() {
        boolean loggedIn = true;
        while (loggedIn) {
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
            System.out.println("15. Display transportation document");
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
                case 1: {
                    addDriver();
                    break;
                }
                case 2: {
                    addTruck();
                    break;
                }
                case 3: {
                    createShipmentArea();
                    break;
                }
                case 4: 
                    changeShipmentArea();
                    break;
                case 5: {
                    makeTransportation();
                    break;
                } 
                case 6: {
                    changeDate();
                    break;
                }
                case 7: {
                    changeDepartureTime();
                    break;
                }
                case 8: {
                    changeTruck();
                    break;
                } 
                case 9: {
                    changeDriver();
                    break;
                }
                case 10: {
                    changeShipmentAreasID();
                    break;
                }
                case 11: {
                    changeOrigin();
                    break;
                }

                case 12:{
                     changeSucceeded();
                    break;
                }
                case 13:{ 
                    addItems();
                    break;
                }
                case 14:{
                    removeItems();
                    break;
                }
                case 15:{
                    displayTransportationDocument();
                    break;
                }
                case 16:{
                    displayAllTransportations();
                    break;
                }
                case 17: {
                    displayTrucks();
                    break;
                }
                case 18: {
                    displayDrivers();
                    break;
                }
                case 19: {
                    reportTransportationSuccess();
                    break;
                }
                case 20: {
                    addSite();
                    break;
                }
                case 21: {
                    removeTruck();
                    break;
                }
                case 22: {
                    removeDriver();
                    break;
                }
                case 23:{
                    System.out.println(userController.logout());
                    loggedIn = false;
                    break;
                }
                case 0: {
                    System.exit(0);
                    break;
                }
                default: {
                    System.out.println("Invalid choice.");
                    break;
                }
            }
        }
    }





    private void driverMenu() {
        boolean loggedIn = true;
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
                case 1:
                    reportAccident();
                    break;
                case 2:
                    displayTransportationDocument();
                    break;
                case 3:
                    displayItemsList();
                    break;
                case 4:
                    System.out.println(userController.logout());
                    loggedIn = false;
                    return;
                case 0:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addUser() {
        while (true) {
            System.out.print("Enter new username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter role (SystemManager / transportationManager / Driver): ");
            String roleStr = scanner.nextLine();

            if (!(roleStr.equals("SystemManager") || roleStr.equals("transportationManager") || roleStr.equals("Driver"))) {
                System.out.println("Invalid role. Please enter exactly: SystemManager, transportationManager, or Driver. Try again.\n");
                continue;
            }

            User.Role role = User.Role.valueOf(roleStr);
            String result = userController.addUser(username, password, role);
            System.out.println(result);
            if (role.equals(User.Role.Driver)) {
                Driver.LicenseType licenseType = Driver.LicenseType.valueOf(scanner.nextLine());
                transportationController.addDriver(username, licenseType);
            }
            break;
        }
    }

    private void deleteUser() {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();
        String result = userController.deleteUser(username);
        System.out.println(result);
    }
    private void addDriver() {
        System.out.print("Enter driver name: ");
        String name = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();


        System.out.print("Enter license type (A/B/C/D/E): ");
        String license = scanner.nextLine();

        Driver.LicenseType licenseType = Driver.LicenseType.valueOf(license);
        String result = transportationController.addDriver(name, licenseType);

        userController.addUser(name, password, User.Role.Driver);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("Enter date (e.g., 2025-05-01): ");
        LocalDate date = null;
        String input = scanner.nextLine();
        try {
            date = LocalDate.parse(input, formatter);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid format. Please use yyyy-MM-dd (e.g. 2025-05-23).");
        }
        if(date == null) {
            return;
        }

        System.out.print("Enter departure time (e.g., 08:00): ");
        String input2 = scanner.nextLine();
        LocalTime departureTime = LocalTime.parse(input2);

        System.out.print("Enter arrival time (e.g., 08:00): ");
        String input3 = scanner.nextLine();
        LocalTime arrivalTime = LocalTime.parse(input3);

        System.out.print("Enter truck plate number: ");
        String truckPlate = scanner.nextLine();

        System.out.print("Enter driver Name: ");
        String driverName = scanner.nextLine();

        System.out.print("Enter origin site name: ");
        String siteName = scanner.nextLine();

        System.out.print("Enter origin site shipment area ID: ");
        int areaId = scanner.nextInt();

        Site origin = transportationController.findShipmentAreaById(areaId).getSiteByName(siteName);
        if (origin == null) {
            System.out.println("Site not found.");
            return;
        } 
        // empty lists to allow basic creation without item logic yet
        List<ItemsDocument> itemsDocs = new ArrayList<>();
        List<Integer> shipmentAreas = new ArrayList<>();
        shipmentAreas.add(areaId);

        String result = transportationController.makeTransportation(id, date, departureTime, arrivalTime, truckPlate, driverName, itemsDocs, shipmentAreas, origin);
        System.out.println(result);
    }

    private void changeDate() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.print("Enter new date: ");
        LocalDate date = null;
        String input = scanner.nextLine();
        try {
            date = LocalDate.parse(input, formatter);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid format. Please use yyyy-MM-dd (e.g. 2025-05-23).");
        }
        if(date == null) {
            return;
        }
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

        System.out.print("Enter new driver name: ");
        String driverName = scanner.nextLine();
        scanner.nextLine();

        System.out.println(transportationController.changeDriverName(id, driverName));
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

        System.out.print("Enter ItemsDocument ID: ");
        int itemsDocumentId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter destination site name: ");
        String siteName = scanner.nextLine();


        System.out.print("Enter destination site shipment area ID: ");
        int areaId = scanner.nextInt();

        Site destination = transportationController.findShipmentAreaById(areaId).getSiteByName(siteName);
        if (destination == null) {
            System.out.println("Site not found.");
            return;
        } 
        boolean done = false;
        List<Item> items = new ArrayList<>(); // Placeholder for actual item list
        while (!done) {
            System.out.print("Enter item id: ");
            int itemId = scanner.nextInt();
            scanner.nextLine(); 
            System.out.print("Enter item name: ");
            String name = scanner.nextLine();

            System.out.print("Enter item quantity: ");
            int quantity = scanner.nextInt();

            System.out.print("Enter item weight: ");
            int weight = scanner.nextInt();
            Item item = new Item(itemId, name, weight, quantity); // ID is set to 0 for simplicity
            items.add(item);
            System.out.print("Done? (yes/no): ");
            scanner.nextLine();
            String doneInput = scanner.nextLine();

            if (doneInput.equals("yes")) {
                done = true;
            }
        }
        ItemsDocument itemsDocument = new ItemsDocument(itemsDocumentId, destination, items); // Placeholder for actual ItemsDocument creation
        // Add logic to create and add items to the document        

        String result = transportationController.addItems(id, itemsDocument);
        System.out.println(result);
    
    }

    private void removeItems() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter ItemsDocument ID: ");
        int itemsDocumentId = scanner.nextInt();
        String result = transportationController.removeItems(id, itemsDocumentId);
        System.out.println(result);
    }

    private void changeOrigin() {
        System.out.print("Enter transportation ID: ");
        int id = scanner.nextInt();

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

        Transportation t = transportationController.findTransportationById(id); 
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
        String input = scanner.nextLine();
        LocalTime time = LocalTime.parse(input);

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
        System.out.print("Enter driver name to remove: ");
        String name = scanner.nextLine();
        System.out.println(transportationController.removeDriver(name));
    }
    public static void main(String[] args) {
        UserInteraction ui = new UserInteraction(); 
        ui.run();
    }
}

