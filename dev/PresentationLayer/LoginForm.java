package PresentationLayer;
import DomainLayer.*;
import ServiceLayer.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class LoginForm {
    private final Scanner scanner = new Scanner(System.in);

    public void show() {
        while (true) {
            System.out.println("=== Login ===");

            System.out.print("Enter your ID: ");
            String userId = scanner.nextLine().trim();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine().trim();

            Employee loggedInUser = null;

            for (Employee user : DAO.employees) {
                if (user.getId().equals(userId) && user.getPassword().equals(password)) {
                    loggedInUser = user;
                    break;
                }
            }

            if (loggedInUser != null) {
                System.out.println("Login successful! Welcome, " + loggedInUser.getName());

                boolean isManager = loggedInUser.IsManager();
                if (!isManager) {
                    new EmployeeUI(loggedInUser).display();
                } else {
                    ManagerUI ManagerDash = new ManagerUI(); //now we know who is the manager that got into the system.
                    ManagerDash.LoggedInManager = (HRManager) loggedInUser;
                    ManagerDash.display();
                }
            } else {
                System.out.println("Invalid ID or password.");
            }
        }
    }
    public void loadSampleData(Branch branch) {
        ShiftService shiftService = new ShiftService();
        HRManagerService managerService = new HRManagerService();

// --- תפקידים ---
        Role cashier = new Role("2", "cashier");
        Role stocker = new Role("3", "stocker");
        Role driver = new Role("4", "driver");
        Role butcher = new Role("5", "butcher");
        Role shift_manager = new Role("1", "shift manager");
        DAO.roles.addAll(List.of(cashier, stocker, driver, butcher,shift_manager));

// --- חוזים ---
        LocalDate today = LocalDate.now();



// --- עובדים ---
        managerService.addEmployee("111", "Hila", "050-0000001", branch, new HashSet<>(List.of(cashier)), "123", false, "pass1");
        managerService.addEmployee("112", "Yarden", "050-0000002", branch, new HashSet<>(List.of(cashier)), "456", false, "pass2");
        managerService.addEmployee("113", "Charlie", "050-0000003", branch, new HashSet<>(List.of(stocker)), "789", false, "pass3");
        managerService.addEmployee("114", "Dana", "050-0000004", branch, new HashSet<>(List.of(driver)), "101", false, "pass4");
        managerService.addEmployee("115", "Eli", "050-0000005", branch, new HashSet<>(List.of(stocker, driver)), "202", false, "pass5");
        managerService.addEmployee("116", "Snir", "050-0000006", branch, new HashSet<>(List.of(cashier,driver)), "303", false, "pass6");
        managerService.addEmployee("117", "David", "050-0000007", branch, new HashSet<>(List.of(shift_manager)), "303", false, "pass7");
        managerService.addEmployee("118", "Ben", "050-0000008", branch, new HashSet<>(List.of(butcher)), "505", false, "pass8");

        EmployeeContract c1 = managerService.createContract("111", today, 12, 5, 160, "Basic", "Standard", 8000);
        EmployeeContract c2 = managerService.createContract("112", today, 12, 5, 160, "Basic", "Standard", 7800);
        EmployeeContract c3 = managerService.createContract("113", today, 10, 4, 160, "Basic", "None", 7500);
        EmployeeContract c4 = managerService.createContract("114", today, 10, 3, 160, "Basic", "None", 7200);
        EmployeeContract c5 = managerService.createContract("115", today, 10, 3, 160, "Standard", "Standard", 7900);
        EmployeeContract c6 = managerService.createContract("116", today, 12, 5, 160, "Basic", "Standard", 7600);
        EmployeeContract c7 = managerService.createContract("117", today, 12, 5, 160, "Basic", "Standard", 9000);
        EmployeeContract c8 = managerService.createContract("118", today, 10, 3, 160, "Standard", "Standard", 7700);

        Employee e1 = managerService.getEmployeeById("111");
        Employee e2 = managerService.getEmployeeById("112");
        Employee e3 = managerService.getEmployeeById("113");
        Employee e4 = managerService.getEmployeeById("114");
        Employee e5 = managerService.getEmployeeById("115");
        Employee e6 = managerService.getEmployeeById("116");
        Employee e7 = managerService.getEmployeeById("117");
        Employee e8 = managerService.getEmployeeById("118");

        e1.setContract(c1);
        e2.setContract(c2);
        e3.setContract(c3);
        e4.setContract(c4);
        e5.setContract(c5);
        e6.setContract(c6);
        e7.setContract(c7);
        e8.setContract(c8);

// --- משמרות ---
        LocalDate sunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        for (int i = 0; i < 6; i++) {
            LocalDate date = sunday.plusDays(i);
            String dateStr = date.toString();

            Shift morningShift = shiftService.createShift("morn_" + i, date, "08:00", "14:00", "Morning", null, new ArrayList<>(List.of(cashier, stocker, butcher)), new ArrayList<>());
            Shift eveningShift = shiftService.createShift("eve_" + i, date, "15:00", "21:00", "Evening", null, new ArrayList<>(List.of(driver)), new ArrayList<>());

            // --- יצירת שיבוצים ---
            List<ShiftAssignment> morningAssignments = new ArrayList<>();
            List<ShiftAssignment> eveningAssignments = new ArrayList<>();

            if (i % 2 == 0) {
                morningAssignments.add(new ShiftAssignment(e1, morningShift, cashier, null)); // Hila
                morningAssignments.add(new ShiftAssignment(e3, morningShift, stocker, null)); // Charlie
                morningAssignments.add(new ShiftAssignment(e8, morningShift, butcher, null)); // Ben
                eveningAssignments.add(new ShiftAssignment(e4, eveningShift, driver, null));  // Dana
            } else {
                morningAssignments.add(new ShiftAssignment(e2, morningShift, cashier, null)); // Yarden
                morningAssignments.add(new ShiftAssignment(e5, morningShift, stocker, null)); // Eli
                morningAssignments.add(new ShiftAssignment(e8, morningShift, butcher, null)); // Ben
                eveningAssignments.add(new ShiftAssignment(e6, eveningShift, driver, null));  // Snir
            }

            // --- דוד (David) משובץ קבוע בכל משמרת בתור מנהל משמרת ---
            morningAssignments.add(new ShiftAssignment(e7, morningShift, shift_manager, null));
            eveningAssignments.add(new ShiftAssignment(e7, eveningShift, shift_manager, null));

            // קובע את דוד כ-Shift Manager בפועל במשמרת
            morningShift.setShiftManager(e7);
            eveningShift.setShiftManager(e7);

            // --- הוספת השיבוצים למשמרת ---
            morningShift.setAssignments(morningAssignments);
            eveningShift.setAssignments(eveningAssignments);

            // --- שמירה ב-DAO ---
            DAO.assignments.addAll(morningAssignments);
            DAO.assignments.addAll(eveningAssignments);
        }

        System.out.println("Sample data loaded with archived shifts from Sunday to Friday.");
    }

    public static void main(String[] args) {
        // יצירת סניף ומנהל
        Role role = new Role("0", "manager");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        List<Employee> EmployeesInTheBranch = new ArrayList<>();
        Branch branch = new Branch("1", "example", "example", EmployeesInTheBranch);
        DAO.branches.add(branch);

        EmployeeContract contract = new EmployeeContract(
                "123456789",
                LocalDate.parse("2025-04-17"),
                10, 10, 20, "example", "example", 2000, "17.04.2025", true
        );
        HRManager manager = new HRManager(
                "123456789", "manager", "054-4332473",
                branch, roles, contract, "n", true, LocalDate.now(), "123"
        );
        DAO.employees.add(manager);

        // --- הוספת לולאת בדיקה ---
        Scanner scanner = new Scanner(System.in);
        LoginForm loginForm = new LoginForm();

        System.out.println("Welcome to the System!");

        String answer = "";
        while (true) {
            System.out.println("Would you like to load sample data? (yes/no) ");
            answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("yes")) {
                loginForm.loadSampleData(branch);
                break;
            } else if (answer.equals("no")) {
                System.out.println("Starting with empty system. You can add data manually.");
                break;
            } else {
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        }

        loginForm.show();
    }

}
