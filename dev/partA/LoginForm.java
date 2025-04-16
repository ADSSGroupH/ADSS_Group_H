import java.util.Scanner;

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

            switch (loggedInUser.IsManager()) {
                case false:
                    new EmployeeUI().display(loggedInUser);
                    break;
                case true:
                    new ManagerUI().display(loggedInUser);
                    break;
            }
        } else {
            System.out.println("Invalid ID or password.");
        }
    }
}
