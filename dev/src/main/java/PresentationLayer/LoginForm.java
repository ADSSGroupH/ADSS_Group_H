package PresentationLayer;
import DomainLayer.HR.*;
import ServiceLayer.*;
import database.Database;

import java.sql.SQLException;
import java.util.*;

public class LoginForm {
    private final Scanner scanner = new Scanner(System.in);
    HRManagerService managerService = new HRManagerService();
    ShiftService shiftService = new ShiftService();
    ShiftSwapRequestService shiftSwapRequest = new ShiftSwapRequestService();
    public void show() throws SQLException {
        while (true) {
            System.out.println("=== Login ===");

            System.out.print("Enter your ID: ");
            String userId = scanner.nextLine().trim();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine().trim();

            Employee loggedInUser = null;

            List<Employee> AllEmployees = managerService.getAllEmployees();
            for (Employee user : AllEmployees) {
                if (user.getId().equals(userId)) {
                    loggedInUser = user;
                    break;
                }
            }

            if (loggedInUser != null) {
                System.out.println("Login successful! Welcome, " + loggedInUser.getName());

                boolean isManager = loggedInUser.IsManager();
                if (!isManager) {
                    boolean isDriver = loggedInUser.getRoles().stream()
                            .anyMatch(role -> role.getName().equalsIgnoreCase("Driver"));

                    if (isDriver) {
                        new DriverUI(loggedInUser).display();
                    } else {
                        new EmployeeUI(loggedInUser).display();
                    }
                } else {
                    ManagerUI HRDash = new ManagerUI();
                    HRManager manager = new HRManager(
                            loggedInUser.getId(), loggedInUser.getName(), loggedInUser.getPhoneNumber(),
                            loggedInUser.getBranchId(), loggedInUser.getRoles(), loggedInUser.getContract(),
                            loggedInUser.getBankDetails(), loggedInUser.isArchived(),
                            loggedInUser.getArchivedAt(), loggedInUser.getPassword()
                    );
                    TransportationManagerUI TransportationDash = new TransportationManagerUI(manager);
                    System.out.println("Hi Manager! What would you like to do? ");
                    System.out.println("1. Manage HR ");
                    System.out.println("2. Manage Transportations ");

                    String choice = scanner.nextLine().trim().toLowerCase();
                    if (choice.equals("1")) {
                        HRDash.LoggedInManager = manager;
                        HRDash.display();
                    }
                    if (choice.equals("2")) {
                        TransportationDash.display();
                    }
                }
            } else {
                System.out.println("Invalid ID or password.");
            }
        }

    }

    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        LoginForm loginForm = new LoginForm();

        System.out.println("Welcome to the System!");

        String answer = "";
        while (true) {
            System.out.println("Would you like to clear the data in the system? (yes/no) ");
            answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("yes")) {
                Database.ClearData();
                System.out.println("Starting with empty system. You can now add data manually.");
                break;
            } else if (answer.equals("no")) {
                System.out.println("The system is loaded with employees, shifts, and shifts assignments from sunday to friday.");
                break;
            } else {
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        }

        loginForm.show();
    }


}
