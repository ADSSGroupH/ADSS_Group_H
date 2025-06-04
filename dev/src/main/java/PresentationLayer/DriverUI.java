package PresentationLayer;

import ServiceLayer.DriverTransportationService;
import DomainLayer.HR.Employee;
import java.util.Scanner;

public class DriverUI extends EmployeeUI {

    private final Scanner scanner = new Scanner(System.in);
    private final DriverTransportationService driverService = new DriverTransportationService();

    public DriverUI(Employee employee) {
        super(employee); // עובר למחלקת EmployeeUI
    }

    @Override
    public void display() {
        while (true) {
            System.out.println("\nDriver Menu:");
            System.out.println("1. Submit Shift Preferences");
            System.out.println("2. View Weekly Assignments (My Shifts)");
            System.out.println("3. View Full Weekly Assignment Report");
            System.out.println("4. Submit Shift Swap Request");
            System.out.println("5. View My Personal Details");
            System.out.println("6. View a specific shift details (for the shift manager only!)");
            System.out.println("7. View transportation document");
            System.out.println("8. View items in transportation");
            System.out.println("9. Report accident");
            System.out.println("10. Exit");

            System.out.print("Choose: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> submitShiftPreferences();
                case "2" -> viewMyAssignments();
                case "3" -> viewFullWeeklyReport();
                case "4" -> submitSwapRequest();
                case "5" -> printEmployeeDetails();
                case "6" -> viewShiftDetailsAsManager();
                case "7" -> viewTransportationDocument();
                case "8" -> viewItemsInTransportation();
                case "9" -> reportAccident();
                case "10" -> {
                    System.out.println("Logging out.");
                    new LoginForm().show();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewTransportationDocument() {
        System.out.print("Enter transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        String document = driverService.getTransportationDocument(id);
        System.out.println(document);
    }

    private void viewItemsInTransportation() {
        System.out.print("Enter transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        String items = driverService.getItemsForTransportation(id);
        System.out.println(items);
    }

    private void reportAccident() {
        System.out.print("Enter transportation ID to report accident: ");
        int id = Integer.parseInt(scanner.nextLine());
        driverService.reportAccident(id);
    }
}
