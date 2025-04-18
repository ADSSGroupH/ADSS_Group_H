import java.util.*;

public class ManagerUI {
    private final Scanner scanner = new Scanner(System.in);
    private final HRManagerService managerService = new HRManagerService();
    private final ShiftService shiftService = new ShiftService();
    private final ShiftSwapRequestService swapService = new ShiftSwapRequestService();

    public void display() {
        while (true) {
            System.out.println("\nManager Menu:");
            System.out.println("1. View All Employees");
            System.out.println("2. View Weekly Assignment Report");
            System.out.println("3. View Shift Swap Requests");
            System.out.println("4. Approve or Reject a Swap Request");
            System.out.println("5. Add New Employee");
            System.out.println("6. Delete Employee");
            System.out.println("7. Create New Shift");
            System.out.println("8. Update Shift Field");
            System.out.println("9. Delete Shift");
            System.out.println("10. Update Employee Info");
            System.out.println("11. Find Shift by ID");
            System.out.println("12. View All Shifts for This Week");
            System.out.println("13. Add New Role"); //might be unnecessary - check!
            System.out.println("14. get all employees qualified for a specific role");
            System.out.println("15. create shift assignment");
            System.out.println("16. Exit");
            System.out.print("Choose: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> {
                    List<Employee> employees = managerService.getAllEmployees();
                    for (Employee e : employees) {
                        System.out.println("- ID: " + e.getId() + ", Name: " + e.getName());
                    }
                }

                case "2" -> {
                    List<String> report = shiftService.MakeWeeklyAssignmentReport();
                    if (report == null || report.isEmpty()) {
                        System.out.println("No assignments found.");
                    } else {
                        for (String line : report) {
                            System.out.println(line);
                        }
                    }
                }

                case "3" -> {
                    List<ShiftSwapRequest> requests = swapService.getAllRequests();
                    for (ShiftSwapRequest r : requests) {
                        System.out.println("Request ID: " + r.getId() + ", From: " + r.getFromShift().getId() + ", To: " + r.getToShift().getId() + ", Status: " + r.getStatus());
                    }
                }

                case "4" -> {
                    System.out.print("Enter request ID to update: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter new status (approved/rejected): ");
                    String statusStr = scanner.nextLine();
                    ShiftSwapRequest.Status status = ShiftSwapRequest.Status.valueOf(statusStr);
                    boolean updated = swapService.updateRequestStatus(id, status);
                    System.out.println(updated ? "Status updated." : "Request not found.");

                    if (updated && status == ShiftSwapRequest.Status.approved) {
                        ShiftSwapRequest request = swapService.getRequestById(id);
                        if (request != null) {
                            swapService.applyApprovedRequest(request);
                        }
                    }
                }

                case "5" -> {
                    System.out.print("Employee ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Phone: ");
                    String phone = scanner.nextLine();

                    // קלט לסניף
                    Branch branch = null;
                    System.out.print("Branch ID: ");
                    String branchId = scanner.nextLine();
                    for (Branch b: DataStore.branches) {
                        if (b.getId().equals(branchId)) {
                            branch = b;
                            break;
                        }
                    }
                    if (branch == null) {
                        System.out.println("Branch with ID '" + branchId + "' was not found. Employee creation aborted.");
                        break; // יוצא מ-case 5
                    }

                    Set<Role> roles = new HashSet<>();
                    System.out.print("Add a role? (yes/no): ");
                    String addRole = scanner.nextLine();
                    while (addRole.equalsIgnoreCase("yes")) {

                        Role role = null;
                        System.out.print("Role name: ");
                        String roleName = scanner.nextLine();
                        for (Role r: DataStore.roles) {
                            if (r.getName().equals(roleName)) {
                                role = r;
                            }
                        }
                        if ( role == null){
                            System.out.print("This role doesn't exist. would you like to create it? (yes/no): ");
                            String answer = scanner.nextLine();
                            if (answer.equals("yes")) {
                                System.out.print("Role ID: ");
                                String roleId = scanner.nextLine();
                                Role newRole = new Role(roleName, roleId);
                                roles.add(newRole);
                                DataStore.roles.add(newRole); // מוסיף גם למחסן התפקידים
                            }
                        }

                        System.out.print("Add another role? (yes/no): ");
                        addRole = scanner.nextLine();
                    }

                    System.out.print("Bank details: ");
                    String bank = scanner.nextLine();

                    System.out.print("password: ");
                    String password = scanner.nextLine();

                    managerService.addEmployee(id, name, phone, branch, roles, bank, false,password);

                    // יצירת חוזה לעובד החדש
                    System.out.println("Enter contract details for the new employee:");
                    System.out.print("Start Date (yyyy-MM-dd): ");
                    String startDate = scanner.nextLine();
                    System.out.print("Free Days: ");
                    int freeDays = Integer.parseInt(scanner.nextLine());
                    System.out.print("Sickness Days: ");
                    int sicknessDays = Integer.parseInt(scanner.nextLine());
                    System.out.print("Monthly Work Hours: ");
                    int hours = Integer.parseInt(scanner.nextLine());
                    System.out.print("Social Contributions: ");
                    String social = scanner.nextLine();
                    System.out.print("Advanced Study Fund: ");
                    String fund = scanner.nextLine();
                    System.out.print("Salary: ");
                    int salary = Integer.parseInt(scanner.nextLine());

                    managerService.createContract(id, startDate, freeDays, sicknessDays, hours, social, fund, salary);
                    System.out.println("A new employee was successfully added along with a new active contract.");
                }

                case "6" -> {
                    System.out.print("Enter ID of employee to delete: ");
                    String id = scanner.nextLine();
                    managerService.deleteEmployee(id);
                }

                case "7" -> {
                    System.out.print("Shift ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Date (yyyy-MM-dd): ");
                    String date = scanner.nextLine();
                    System.out.print("Start Time: ");
                    String start = scanner.nextLine();
                    System.out.print("End Time: ");
                    String end = scanner.nextLine();
                    System.out.print("Type: ");
                    String type = scanner.nextLine();
                    Employee manager = null; // will bw assigned in the shift assignment
                    List<Role> roles = new ArrayList<>();
                    System.out.print("Which roles do you need in this shift? : (separate the names by comma)");
                    String neededRoles = scanner.nextLine();
                    // Split the roles
                    String[] SeperatedNeededRoles = neededRoles.split(",");
                    for (String roleName : SeperatedNeededRoles){
                        for (Role existedRole : DataStore.roles){
                            if (!existedRole.getName().equals(roleName)){
                                System.out.printf("%s doesn't exist in the system. Would you like to add it? (yes/no)%n", roleName);
                                String answer = scanner.nextLine();
                                if (answer.equals("yes")){
                                    System.out.printf("What is the role Id?");
                                    String RoleId = scanner.nextLine();
                                    Role newRole = new Role (RoleId, roleName);
                                    roles.add(newRole);
                                    DataStore.roles.add(newRole);
                                }

                            }else{
                                roles.add(existedRole);
                            }
                        }
                        System.out.printf("%s doesn't exist in the system. Would you like to add it? (yes/no)%n", roleName);
                        String answer = scanner.nextLine();
                        if (answer.equals("yes")){
                            System.out.printf("What is the role Id?");
                            String RoleId = scanner.nextLine();
                            Role newRole = new Role (RoleId, roleName);
                            roles.add(newRole);
                            DataStore.roles.add(newRole);
                        }

                    }


                    List<ShiftAssignment> assignments = new ArrayList<>();
                    shiftService.createShift(id, date, start, end, type, manager, roles, assignments);
                }

                case "8" -> {
                    System.out.print("Enter shift ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter field name to update: ");
                    String field = scanner.nextLine();
                    System.out.print("Enter new value (manual object input required): ");
                    String value = scanner.nextLine(); // Here you'd inject a proper object
                    shiftService.updateShiftField(id, field, value);
                }

                case "9" -> {
                    System.out.print("Enter shift ID to delete: ");
                    String id = scanner.nextLine();
                    shiftService.deleteShift(id);
                }

                case "10" -> {
                    System.out.print("Enter employee ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Field to update (name/phoneNumber/bankDetails): ");
                    String field = scanner.nextLine();
                    System.out.print("New value: ");
                    String value = scanner.nextLine();
                    managerService.updateEmployeeField(id, field, value);
                }

                case "11" -> {
                    System.out.print("Enter shift ID: ");
                    String id = scanner.nextLine();
                    Shift shift = shiftService.getShiftById(id);
                    System.out.println(shift != null ? "Shift found: " + shift.getType() + ", Date: " + shift.getDate() : "Shift not found.");
                }

                case "12" -> {
                    List<Shift> shifts = shiftService.getAllShiftsForThisWeek();
                    for (Shift s : shifts) {
                        System.out.println("- ID: " + s.getId() + ", Date: " + s.getDate() + ", Type: " + s.getType());
                    }
                }

                case "13" -> {
                    System.out.print("Enter new role ID: ");
                    String roleId = scanner.nextLine();
                    System.out.print("Enter new role name: ");
                    String roleName = scanner.nextLine();
                    Role role = new Role(roleId, roleName);
                    DataStore.roles.add(role);
                    System.out.println("New role added successfully.");
                }
                case "14" -> {
                    System.out.println("Enter the role name");
                    String RoleName = scanner.nextLine();
                    HRManagerService ManagerService = new HRManagerService();
                    System.out.println(ManagerService.getAllEmployeesByRole(RoleName)); //check the printing is working
                }
                case "15" -> {
                    System.out.println("Enter the shift ID: ");
                    String ShiftId = scanner.nextLine();
                    ShiftService shiftService1 = new ShiftService();
                    shiftService1.CreateShiftAssignment(ShiftId);
                }

                case "16" -> {
                    System.out.println("Logging out...");
                    new LoginForm().show();
                }

                default -> System.out.println("Invalid choice.");
            }

        }

    }
}
