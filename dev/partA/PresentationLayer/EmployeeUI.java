import java.util.Scanner;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.time.LocalDate;

public class EmployeeUI {
    private final Scanner scanner = new Scanner(System.in);
    private final Employee employee;
    private final EmployeeService employeeService = new EmployeeService();
    private final ShiftService shiftService = new ShiftService();
    private final ShiftSwapRequestService swapService = new ShiftSwapRequestService();

    public EmployeeUI(Employee employee) {
        this.employee = employee;
    }

    public void display() {
        while (true) {
            System.out.println("\nEmployee Menu:");
            System.out.println("1. Submit Shift Preferences");
            System.out.println("2. View Weekly Assignments (My Shifts)");
            System.out.println("3. View Full Weekly Assignment Report");
            System.out.println("4. Submit Shift Swap Request");
            System.out.println("5. View My Personal Details");
            System.out.println("6. Exit");
            System.out.print("Choose: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> employeeService.submitWeeklyShiftPreferences(employee.getId());

                case "2" -> {
                    List<ShiftAssignment> assignments = shiftService.getWeeklyAssignmentsForEmployee(employee.getId());
                    if (assignments == null || assignments.isEmpty()) {
                        System.out.println("No assignments for this week.");
                    } else {
                        System.out.println("Weekly Assignments:");
                        for (ShiftAssignment a : assignments) {
                            System.out.println("- " + a.getShift().getDate() + " | " + a.getShift().getType() + " | Role: " + (a.getRole() != null ? a.getRole().getName() : "None"));
                        }
                    }
                }

                case "3" -> {
                    List<String> report = shiftService.MakeWeeklyAssignmentReport();
                    if (report == null || report.isEmpty()) {
                        System.out.println("No assignments found for this week.");
                    } else {
                        for (String line : report) {
                            System.out.println(line);
                        }
                    }
                }

                case "4" -> {
                    System.out.print("Enter ID of shift to swap FROM: ");
                    String fromId = scanner.nextLine();
                    System.out.print("Enter ID of shift to swap TO: ");
                    String toId = scanner.nextLine();

                    Shift fromShift = shiftService.getShiftById(fromId);
                    Shift toShift = shiftService.getShiftById(toId);

                    if (fromShift == null || toShift == null) {
                        System.out.println("One or both shifts not found.");
                        break;
                    }

                    String requestId = UUID.randomUUID().toString();
                    String date = LocalDate.now().toString();
                    ShiftSwapRequest request = swapService.createRequest(requestId, employee, fromShift, toShift, date);
                    System.out.println("Swap request submitted with ID: " + request.getId());
                }

                case "5" -> printEmployeeDetails();

                case "6" -> {
                    System.out.println("Logging out...");
                    new LoginForm().show();;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void printEmployeeDetails() {
        System.out.println("\n--- Personal Details ---");
        System.out.println("ID: " + employee.getId());
        System.out.println("Name: " + employee.getName());
        System.out.println("Phone: " + employee.getPhoneNumber());
        System.out.println("Branch: " + (employee.getBranch() != null ? employee.getBranch().getName() : "None"));

        System.out.print("Roles: ");
        Set<Role> roles = employee.getRoles();
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                System.out.print(role.getName() + " ");
            }
        } else {
            System.out.print("None");
        }
        System.out.println();

        System.out.println("Bank Details: " + employee.getBankDetails());

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
}
