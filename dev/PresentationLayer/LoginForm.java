package PresentationLayer;
import DomainLayer.*;
import ServiceLayer.*;

import java.time.LocalDate;
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
        loginForm.show();
    }
}