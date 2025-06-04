package PresentationLayer;

import DomainLayer.HR.Employee;
import ServiceLayer.TransportationManagerService;

import java.util.Scanner;

public class TransportationManagerUI {
    private final Scanner scanner = new Scanner(System.in);
    private final TransportationManagerService service;
    private final Employee employee;

    public TransportationManagerUI(Employee employee) {
        this.employee = employee;
        this.service = new TransportationManagerService();
    }

    public void display() {
        while (true) {
            System.out.println("\nTransportation Manager Menu:");
            System.out.println("1. Add truck");
            System.out.println("2. Add site");
            System.out.println("3. Make transportation");
            System.out.println("4. Change shipment area name");
            System.out.println("5. Change transportation date");
            System.out.println("6. Change departure time");
            System.out.println("7. Change truck in transportation");
            System.out.println("8. Change driver in transportation");
            System.out.println("9. Change shipment areas");
            System.out.println("10. Change origin");
            System.out.println("11. Change success status");
            System.out.println("12. Add items");
            System.out.println("13. Remove items");
            System.out.println("14. Display transportation document");
            System.out.println("15. Display all transportations");
            System.out.println("16. Display all trucks");
            System.out.println("17. Display all drivers");
            System.out.println("18. Report success");
            System.out.println("19. Create shipment area");
            System.out.println("20. Remove truck");
            System.out.println("21. Exit");

            System.out.print("Choose: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> service.addTruckUI(scanner);
                case "2" -> service.addSiteUI(scanner);
                case "3" -> service.makeTransportationUI(scanner);
                case "4" -> service.changeShipmentAreaNameUI(scanner);
                case "5" -> service.changeTransportationDateUI(scanner);
                case "6" -> service.changeDepartureTimeUI(scanner);
                case "7" -> service.changeTruckUI(scanner);
                case "8" -> service.changeDriverUI(scanner);
                case "9" -> service.changeShipmentAreasUI(scanner);
                case "10" -> service.changeOriginUI(scanner);
                case "11" -> service.changeSuccessStatusUI(scanner);
                case "12" -> service.addItemsUI(scanner);
                case "13" -> service.removeItemsUI(scanner);
                case "14" -> service.displayTransportationDocumentUI(scanner);
                case "15" -> service.displayAllTransportations();
                case "16" -> service.displayAllTrucks();
                case "17" -> service.displayAllDrivers();
                case "18" -> service.reportSuccessUI(scanner);
                case "19" -> service.createShipmentAreaUI(scanner);
                case "20" -> service.removeTruckUI(scanner);
                case "21" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
