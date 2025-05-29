package PresentationLayer;

import java.util.*;
import java.time.LocalDate;
import DomainLayer.*;
import ServiceLayer.*;

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
            System.out.println("6. View a specific shift details (for the shift manager only!)");
            System.out.println("7. Exit");
            System.out.print("Choose: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> {
                    try {
                        List<Shift> eligibleShifts = employeeService.getEligibleShiftsForNextWeek(employee.getId());

                        if (eligibleShifts.isEmpty()) {
                            System.out.println("No eligible shifts available for you this week.");
                            return;
                        }

                        System.out.println("Available eligible shifts:");
                        int index = 1;
                        for (Shift shift : eligibleShifts) {
                            System.out.println(index + ". Date: " + shift.getDate() + ", Type: " + shift.getType() + " (ID: " + shift.getId() + ")");
                            index++;
                        }

                        System.out.println("Enter the numbers of the shifts you want to select (separated by commas):");
                        Scanner scanner = new Scanner(System.in);
                        String input1 = scanner.nextLine();
                        String[] shiftNumbers = input1.split(",");

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

                        if (selectedShifts.isEmpty()) {
                            System.out.println("No valid shifts selected.");
                        } else {
                            employeeService.submitSelectedShifts(employee.getId(), selectedShifts);
                            System.out.println("Shift preferences submitted successfully!");
                        }

                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                case "2" -> {
                    List<ShiftAssignment> assignments = shiftService.getWeeklyAssignmentsForEmployee(employee.getId());
                    if (assignments == null) {
                        System.out.println("Invalid date format in one of the shifts this week");
                    } else if (assignments.isEmpty()) {
                        System.out.println("No assignments found for employee ID: " + employee.getId());
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
                    try {
                        List<ShiftAssignment> myAssignments = DAO.assignments.stream().filter(a -> a.getEmployee().getId().equals(employee.getId())).filter(a -> {
                                    LocalDate date = a.getShift().getDate();
                                    LocalDate start = LocalDate.now().with(java.time.DayOfWeek.SUNDAY);
                                    LocalDate end = start.plusDays(6);
                                    return !date.isBefore(start) && !date.isAfter(end);}).toList();

                        if (myAssignments.isEmpty()) {
                            System.out.println("You have no assigned shifts for this week.");
                            break;
                        }

                        System.out.println("Your assigned shifts this week:");
                        for (int i = 0; i < myAssignments.size(); i++) {
                            Shift s = myAssignments.get(i).getShift();
                            Role r = myAssignments.get(i).getRole();
                            System.out.printf("%d. %s | %s | Role: %s\n", i + 1, s.getDate(), s.getType(), r.getName());
                        }

                        System.out.print("Select shift number to swap FROM: ");
                        int choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (choice < 0 || choice >= myAssignments.size()) {
                            System.out.println("Invalid choice.");
                            break;
                        }

                        Shift fromShift = myAssignments.get(choice).getShift();
                        Role myRole = myAssignments.get(choice).getRole();

                        // מצא את כל המשמרות השבועיות שדורשות את אותו תפקיד
                        LocalDate weekStart = LocalDate.now().with(java.time.DayOfWeek.SUNDAY);
                        LocalDate weekEnd = weekStart.plusDays(6);

                        List<Shift> compatibleShifts = DAO.shifts.stream()
                                .filter(s -> s.getDate().compareTo(weekStart) >= 0 && s.getDate().compareTo(weekEnd) <= 0)
                                .filter(s -> s.getRequiredRoles().stream()
                                        .anyMatch(r -> r.getId().equals(myRole.getId())))
                                .filter(s -> !s.equals(fromShift))  // לא ניתן להחליף לאותה משמרת
                                .filter(s -> s.getAssignments().stream()
                                        .noneMatch(a -> a.getEmployee().getId().equals(employee.getId()))) // לא משובץ שם כבר
                                .toList();

                        if (compatibleShifts.isEmpty()) {
                            System.out.println("No available shifts this week with the same role.");
                            break;
                        }

                        System.out.println("Available shifts with role '" + myRole.getName() + "':");
                        for (int i = 0; i < compatibleShifts.size(); i++) {
                            Shift s = compatibleShifts.get(i);
                            System.out.printf("%d. %s | %s\n", i + 1, s.getDate(), s.getType());
                        }

                        System.out.print("Select shift number to swap TO: ");
                        int toChoice = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (toChoice < 0 || toChoice >= compatibleShifts.size()) {
                            System.out.println("Invalid choice.");
                            break;
                        }

                        Shift toShift = compatibleShifts.get(toChoice);

                        String requestId = String.valueOf(DAO.swapRequests.size() + 1);
                        ShiftSwapRequest request = new ShiftSwapRequestController().createRequest(
                                requestId,
                                employee,
                                fromShift,
                                toShift,
                                LocalDate.now()
                        );

                        System.out.println("Swap request submitted.");

                    } catch (Exception e) {
                        System.out.println("Something went wrong: " + e.getMessage());
                    }
                }

                case "5" -> printEmployeeDetails();

                case "6" -> {
                    System.out.print("Enter shift ID: ");
                    String shiftId = scanner.nextLine();
                    Shift shift = shiftService.getShiftById(shiftId);
                    if (shift == null) {
                        System.out.println("Shift not found.");
                        break;
                    }
                    if (!shift.getShiftManager().getId().equals(this.employee.getId())){
                        System.out.printf("you are not the manager of shift number : %s! Therefore you can't watch this shift's details.",shift.getId());
                    }else {
                        System.out.println("\n--- Shift Details ---");
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
                                System.out.println("- " + role.getName() + ": (unassigned, employee needed)");
                            }
                        }
                    }
                }

                case "7" -> {
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