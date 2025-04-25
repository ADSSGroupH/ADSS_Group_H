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
        Role cashier = new Role("2", "Cashier");
        Role stocker = new Role("3", "Stocker");
        Role driver = new Role("4", "Driver");
        Role butcher = new Role("5", "Butcher");
        DAO.roles.addAll(List.of(cashier, stocker, driver, butcher));

// --- חוזים ---
        LocalDate today = LocalDate.now();



// --- עובדים ---
        managerService.addEmployee("111", "Hila", "050-0000001", branch, new HashSet<>(List.of(cashier)), "123", false, "pass1");
        managerService.addEmployee("112", "Yarden", "050-0000002", branch, new HashSet<>(List.of(cashier)), "456", false, "pass2");
        managerService.addEmployee("113", "Charlie", "050-0000003", branch, new HashSet<>(List.of(stocker)), "789", false, "pass3");
        managerService.addEmployee("114", "Dana", "050-0000004", branch, new HashSet<>(List.of(driver)), "101", false, "pass4");
        managerService.addEmployee("115", "Eli", "050-0000005", branch, new HashSet<>(List.of(stocker, driver)), "202", false, "pass5");
        managerService.addEmployee("116", "Snir", "050-0000006", branch, new HashSet<>(List.of(cashier)), "303", false, "pass6");

        EmployeeContract c1 = managerService.createContract("111", today, 12, 5, 160, "Basic", "Standard", 8000);
        EmployeeContract c2 = managerService.createContract("112", today, 12, 5, 160, "Basic", "Standard", 7800);
        EmployeeContract c3 = managerService.createContract("113", today, 10, 4, 160, "Basic", "None", 7500);
        EmployeeContract c4 = managerService.createContract("114", today, 10, 3, 160, "Basic", "None", 7200);
        EmployeeContract c5 = managerService.createContract("115", today, 10, 3, 160, "Standard", "Standard", 7900);
        EmployeeContract c6 = managerService.createContract("116", today, 12, 5, 160, "Basic", "Standard", 7600);

        Employee e1 = managerService.getEmployeeById("111");
        Employee e2 = managerService.getEmployeeById("112");
        Employee e3 = managerService.getEmployeeById("113");
        Employee e4 = managerService.getEmployeeById("114");
        Employee e5 = managerService.getEmployeeById("115");
        Employee e6 = managerService.getEmployeeById("116");

        e1.setContract(c1);
        e2.setContract(c2);
        e3.setContract(c3);
        e4.setContract(c4);
        e5.setContract(c5);
        e6.setContract(c6);

// --- משמרות ---
        LocalDate sunday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        for (int i = 0; i < 6; i++) {
            LocalDate date = sunday.plusDays(i);
            String dateStr = date.toString();

            Shift morningShift = shiftService.createShift("morn_" + i, date, "08:00", "14:00", "Morning", null, new ArrayList<>(List.of(cashier, stocker, butcher)), new ArrayList<>());
            Shift eveningShift = shiftService.createShift("eve_" + i, date, "15:00", "21:00", "Evening", null, new ArrayList<>(List.of(driver)), new ArrayList<>());

            // שיבוצים
            if (i % 2 == 0) {
                ShiftAssignment a1 = new ShiftAssignment(e1, morningShift, cashier, null);
                ShiftAssignment a2 = new ShiftAssignment(e3, morningShift, stocker, null);
                ShiftAssignment a3 = new ShiftAssignment(e4, eveningShift, driver, null);
                morningShift.setAssignments(List.of(a1,a2));
                eveningShift.setAssignments(List.of(a3));
                DAO.assignments.addAll(List.of(a1, a2, a3));
            } else {
                ShiftAssignment a4 = new ShiftAssignment(e2, morningShift, cashier, null);
                ShiftAssignment a5 = new ShiftAssignment(e5, morningShift, stocker, null);
                ShiftAssignment a6 = new ShiftAssignment(e6, eveningShift, driver, null);
                morningShift.setAssignments(List.of(a4,a5));
                eveningShift.setAssignments(List.of(a6));
                DAO.assignments.addAll(List.of(a4, a5, a6));
            }

            // נוספו כבר על ידי archiveShift במתודה createShift
        }

// בקשת החלפה
        Shift s1 = DAO.shifts.stream().filter(s -> s.getId().equals("morn_0")).findFirst().orElse(null);
        Shift s2 = DAO.shifts.stream().filter(s -> s.getId().equals("morn_1")).findFirst().orElse(null);
        if (s1 != null && s2 != null) {
            ShiftSwapRequest swap = new ShiftSwapRequest("swap1", e1, s1, s2, LocalDate.now());
            DAO.swapRequests.add(swap);
        }

        System.out.println("Sample data loaded with archived shifts from Sunday to Friday.");
    }

    public static void main(String[] args) {
        //creating a manager to operate the system
        Role role = new Role("0","manager");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        List <Employee> EmployeesInTheBranch = new ArrayList<>();
        Branch branch = new Branch("1", "example", "example", EmployeesInTheBranch);
        DAO.branches.add(branch);
        EmployeeContract contract = new EmployeeContract("123456789",LocalDate.parse("2025-04-17"),10,10,20,"example", "example",2000,"17.04.2025",true);
        HRManager manager = new HRManager("123456789","manager", "054-4332473", branch,roles,contract,"n",true, LocalDate.now(),"123");
        DAO.employees.add(manager);
        LoginForm loginForm = new LoginForm();
        //loginForm.loadSampleData(branch);
        loginForm.show();
    }
}
