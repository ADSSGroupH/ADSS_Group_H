package PresentationLayer;
<<<<<<< HEAD
/*package PresentationLayer;
=======
>>>>>>> 52972888becfac149da20316b226d9cf1f21354e

import DomainLayer.HR.HRManager;
import ServiceLayer.TransportationManagerService;
import DomainLayer.transportationDomain.ItemsDocument;
import DomainLayer.transportationDomain.Site;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransportationManagerUI extends EmployeeUI {
    private final Scanner scanner = new Scanner(System.in);
    private final TransportationManagerService service = new TransportationManagerService();

    public TransportationManagerUI(HRManager employee) {
        super(employee);
    }

    @Override
    public void display() {
        while (true) {
            System.out.println("\n--- Transportation Manager Menu ---");
            System.out.println("1. Add truck");
            System.out.println("2. Create shipment area");
            System.out.println("3. Change shipment area");
            System.out.println("4. Make transportation");
            System.out.println("5. Change transportation date");
            System.out.println("6. Change departure time");
            System.out.println("7. Change truck in transportation");
            System.out.println("8. Change driver in transportation");
            System.out.println("9. Change shipment areas");
            System.out.println("10. Change origin");
            System.out.println("11. Change succeeded status");
            System.out.println("12. Add items");
            System.out.println("13. Remove items");
            System.out.println("14. Display transportation document");
            System.out.println("15. Display all transportations");
            System.out.println("16. Display all trucks");
            System.out.println("17. Display all drivers");
            System.out.println("18. Report transportation success");
            System.out.println("19. Add site");
            System.out.println("20. Remove truck");
            System.out.println("21. Remove driver");
            System.out.println("22. Employee Options");
            System.out.println("23. Exit");

            System.out.print("Choose option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addTruck();
                case "2" -> createShipmentArea();
                case "3" -> changeShipmentArea();
                case "4" -> makeTransportation();
                case "5" -> changeTransportationDate();
                case "6" -> changeDepartureTime();
                case "7" -> changeTruck();
                case "8" -> changeDriver();
                case "9" -> changeShipmentAreas();
                case "10" -> changeOrigin();
                case "11" -> changeSucceeded();
                case "12" -> addItems();
                case "13" -> removeItems();
                case "14" -> displayTransportationDocument();
                case "15" -> displayAllTransportations();
                case "16" -> displayAllTrucks();
                case "17" -> displayAllDrivers();
                case "18" -> reportSuccess();
                case "19" -> addSite();
                case "20" -> removeTruck();
                case "21" -> removeDriver();
                case "22" -> super.display(); // Call EmployeeUI menu
                case "23" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addTruck() {
        System.out.println(service.addTruck("123", "Volvo", 3000, 5000, DTO.LicenseType.C));
    }

    private void createShipmentArea() {
        System.out.println(service.makeShipmentArea(1, "South Area"));
    }

    private void changeShipmentArea() {
        System.out.println(service.changeShipmentArea(1, "Updated Name"));
    }

    private void makeTransportation() {
        System.out.println(service.makeTransportation(1, LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(12, 0), "123", "driver1", new ArrayList<>(), List.of(1), new Site("Beer Sheva", "address", "050", "contact", 1)));
    }

    private void changeTransportationDate() {
        System.out.println(service.changeDate(1, LocalDate.of(2025, 6, 15)));
    }

    private void changeDepartureTime() {
        System.out.println(service.changeDepartureTime(1, LocalTime.of(9, 0)));
    }

    private void changeTruck() {
        System.out.println(service.changeTruckPlateNumber(1, "456"));
    }

    private void changeDriver() {
        System.out.println(service.changeDriverName(1, "driver2"));
    }

    private void changeShipmentAreas() {
        System.out.println(service.changeShipmentAreasID(1, List.of(1, 2)));
    }

    private void changeOrigin() {
        System.out.println(service.changeOrigin(1, new Site("Tel Aviv", "main", "052", "avi", 1)));
    }

    private void changeSucceeded() {
        System.out.println(service.changeSucceeded(1, true));
    }

    private void addItems() {
        System.out.println(service.addItems(1, new ItemsDocument(1, new ArrayList<>())));
    }

    private void removeItems() {
        System.out.println(service.removeItems(1, 1));
    }

    private void displayTransportationDocument() {
        System.out.println(service.displayTransportationDocument(1));
    }

    private void displayAllTransportations() {
        System.out.println(service.displayAllTransportations());
    }

    private void displayAllTrucks() {
        System.out.println(service.displayTrucks());
    }

    private void displayAllDrivers() {
        System.out.println(service.displayDrivers());
    }

    private void reportSuccess() {
        System.out.println(service.reportTransportationSuccess(1));
    }

    private void addSite() {
        System.out.println(service.addSite("Site1", "Address", "054", "Ron", 1));
    }

    private void removeTruck() {
        System.out.println(service.removeTruck("123"));
    }

    private void removeDriver() {
        System.out.println(service.removeDriver("driver1"));
    }
}
