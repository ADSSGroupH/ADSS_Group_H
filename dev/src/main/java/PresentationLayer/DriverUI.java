package PresentationLayer;

import DomainLayer.HR.*;
import ServiceLayer.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class DriverUI {

    private final Scanner scanner = new Scanner(System.in);
    private final Employee employee;
    private final EmployeeService employeeService = new EmployeeService();
    private final ShiftService shiftService = new ShiftService();
    private final ShiftSwapRequestService swapService = new ShiftSwapRequestService();
    private final DriverTransportationService driverService = new DriverTransportationService();

    public DriverUI(Employee employee) {
        this.employee = employee;
    }

    public void display() throws SQLException {
        while (true) {
            System.out.println("\nDriver Menu:");
            System.out.println("1. Submit Shift Preferences");
            System.out.println("2. View Assignments (My Shifts)");
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
                    System.out.println("Logging out...");
                    new LoginForm().show();
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void submitShiftPreferences() {
        try {
            List<Shift> eligibleShifts = employeeService.getEligibleShiftsForNextWeek(employee.getId());
            if (eligibleShifts.isEmpty()) {
                System.out.println("No eligible shifts available for you this week.");
                return;
            }

            System.out.println("Available eligible shifts:");
            int index = 1;
            for (Shift shift : eligibleShifts) {
                System.out.println(index++ + ". Date: " + shift.getDate() + ", Type: " + shift.getType() + " (ID: " + shift.getId() + ")");
            }

            System.out.println("Enter the numbers of the shifts you want to select (separated by commas):");
            String[] shiftNumbers = scanner.nextLine().split(",");

            List<Shift> selectedShifts = new ArrayList<>();
            for (String numStr : shiftNumbers) {
                try {
                    int num = Integer.parseInt(numStr.trim());
                    if (num >= 1 && num <= eligibleShifts.size()) {
                        selectedShifts.add(eligibleShifts.get(num - 1));
                    } else {
                        System.out.println("Invalid number: " + num);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: " + numStr);
                }
            }

            if (!selectedShifts.isEmpty()) {
                employeeService.submitSelectedShifts(employee.getId(), selectedShifts, "Appending");
                System.out.println("Shift preferences submitted successfully!");
            } else {
                System.out.println("No valid shifts selected.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewMyAssignments() {
        List<ShiftAssignment> assignments = employeeService.getAssignmentsForEmployee(employee.getId());
        if (assignments == null) {
            System.out.println("Invalid date format in shifts.");
        } else if (assignments.isEmpty()) {
            System.out.println("No assignments found.");
        } else {
            System.out.println("Your shifts this week:");
            for (ShiftAssignment a : assignments) {
                Shift s = shiftService.getShiftById(a.getShiftId());
                System.out.println("- " + s.getDate() + " | " + s.getType() + " | Role: " + (a.getRole() != null ? a.getRole().getName() : "None"));
            }
        }
    }

    private void viewFullWeeklyReport() {
        try {
            List<String> report = shiftService.MakeWeeklyAssignmentReport();
            if (report == null || report.isEmpty()) {
                System.out.println("No assignments found for this week.");
            } else {
                report.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching report: " + e.getMessage());
        }
    }


    private void submitSwapRequest() {
        try {
            List<ShiftAssignment> myAssignments = shiftService.getAllAssignmentsForThisWeek().stream()
                    .filter(a -> a.getEmployee().getId().equals(employee.getId()))
                    .toList();

            if (myAssignments.isEmpty()) {
                System.out.println("You have no assigned shifts for this week.");
                return;
            }

            System.out.println("Your assigned shifts:");
            for (int i = 0; i < myAssignments.size(); i++) {
                Shift s = shiftService.getShiftById(myAssignments.get(i).getShiftId());
                Role r = myAssignments.get(i).getRole();
                System.out.printf("%d. %s | %s | Role: %s\n", i + 1, s.getDate(), s.getType(), r.getName());
            }

            System.out.print("Select shift number to swap FROM: ");
            int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (choice < 0 || choice >= myAssignments.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            Shift fromShift = shiftService.getShiftById(myAssignments.get(choice).getShiftId());
            Role myRole = myAssignments.get(choice).getRole();

            LocalDate weekStart = LocalDate.now().with(java.time.DayOfWeek.SUNDAY);
            LocalDate weekEnd = weekStart.plusDays(6);

            List<Shift> compatibleShifts = shiftService.getShiftsBetween(weekStart, weekEnd).stream()
                    .filter(s -> s.getRequiredRoles().stream().anyMatch(r -> r.getId().equals(myRole.getId())))
                    .filter(s -> !s.equals(fromShift))
                    .filter(s -> s.getAssignments().stream().noneMatch(a -> a.getEmployee().getId().equals(employee.getId())))
                    .toList();

            if (compatibleShifts.isEmpty()) {
                System.out.println("No compatible shifts found.");
                return;
            }

            System.out.println("Available shifts:");
            for (int i = 0; i < compatibleShifts.size(); i++) {
                Shift s = compatibleShifts.get(i);
                System.out.printf("%d. %s | %s\n", i + 1, s.getDate(), s.getType());
            }

            System.out.print("Select shift number to swap TO: ");
            int toChoice = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (toChoice < 0 || toChoice >= compatibleShifts.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            Shift toShift = compatibleShifts.get(toChoice);
            String requestId = String.valueOf(swapService.getAllRequests().size() + 1);
            swapService.createRequest(requestId, employee, fromShift, toShift, LocalDate.now());

            System.out.println("Swap request submitted.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewShiftDetailsAsManager() {
        System.out.print("Enter shift ID: ");
        String shiftId = scanner.nextLine();
        Shift shift = shiftService.getShiftById(shiftId);
        if (shift == null) {
            System.out.println("Shift not found.");
        } else if (!shift.getShiftManager().getId().equals(employee.getId())) {
            System.out.println("You are not the manager of this shift.");
        } else {
            System.out.println("--- Shift Details ---");
            System.out.println("ID: " + shift.getId());
            System.out.println("Date: " + shift.getDate());
            System.out.println("Type: " + shift.getType());
            System.out.println("Start: " + shift.getStartTime() + ", End: " + shift.getEndTime());

            Set<Role> assignedRoles = new HashSet<>();
            for (ShiftAssignment assignment : shift.getAssignments()) {
                System.out.println("- " + assignment.getRole().getName() + ": " + assignment.getEmployee().getName());
                assignedRoles.add(assignment.getRole());
            }

            for (Role role : shift.getRequiredRoles()) {
                if (!assignedRoles.contains(role)) {
                    System.out.println("- " + role.getName() + ": (unassigned)");
                }
            }
        }
    }

    private void printEmployeeDetails() {
        System.out.println("\n--- Personal Details ---");
        System.out.println("ID: " + employee.getId());
        System.out.println("Name: " + employee.getName());
        System.out.println("Phone: " + employee.getPhoneNumber());
        System.out.println("Branch ID: " + employee.getBranchId());
        System.out.println("Bank Details: " + employee.getBankDetails());

        Set<Role> roles = employee.getRoles();
        System.out.print("Roles: ");
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                System.out.print(role.getName() + " ");
            }
        } else {
            System.out.print("None");
        }
        System.out.println();

        if (employee.getContract() != null) {
            EmployeeContract c = employee.getContract();
            System.out.println("--- Contract ---");
            System.out.println("Start Date: " + c.getStartDate());
            System.out.println("Free Days: " + c.getFreeDays());
            System.out.println("Sickness Days: " + c.getSicknessDays());
            System.out.println("Monthly Hours: " + c.getMonthlyWorkHours());
            System.out.println("Salary: " + c.getSalary());
        } else {
            System.out.println("No active contract.");
        }
    }

    // ====== פונקציות ייחודיות לנהג ======

    private void viewTransportationDocument() {
        System.out.print("Enter transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println(driverService.getTransportationDocument(id));
    }

    private void viewItemsInTransportation() {
        System.out.print("Enter transportation ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println(driverService.getItemsForTransportation(id));
    }

    private void reportAccident() {
        System.out.print("Enter transportation ID to report accident: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("enter the accident");
        String accident = scanner.nextLine();
        driverService.reportAccident(id, accident);
    }
}
