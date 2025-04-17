import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class LoginForm {
    private final Scanner scanner = new Scanner(System.in);

    public void show() {
        System.out.println("=== Login ===");

        System.out.print("Enter your ID: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine().trim();

        Employee loggedInUser = null;

        for (Employee user : DataStore.employees) {
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
                new ManagerUI().display();
            }
        } else {
            System.out.println("Invalid ID or password.");
        }
    }

    public static void main(String[] args) {
        //creating a manager to operate the system
        Role role = new Role("0","manager");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        Branch branch = new Branch("1","example","example", null);
        EmployeeContract contract = new EmployeeContract("123456789","17.04.2025",10,10,20,"example", "example",2000,"17.04.2025",true);
        HRManager manager = new HRManager("123456789","manager", "054-4332473", branch,roles,contract,"n",true,"17.04.2025","123");
        DataStore.employees.add(manager);
        LoginForm loginForm = new LoginForm();
        loginForm.show();
    }
}
