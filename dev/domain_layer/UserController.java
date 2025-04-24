package dev.domain_layer;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private List<User> users = new ArrayList<>();
    private User currentUser;
    
    public UserController() {
        // Initialize with some users
        users.add(new User( "Tal","123", User.Role.SystemManager));
    }
    public String addUser(String userName, String password, User.Role role) {
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                return "User already exists";
            }
        }
        User user = new User(userName, password, role);
        users.add(user);
        return "User added successfully";
    }

    public String login(String userName, String password) {
        if (currentUser != null) {
            return "User already logged in";
        }
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                currentUser = user;
                return "Login successful";
            }
        }

        
        return "Invalid username or password";
    }

    public String deleteUser(String userName) {
        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                users.remove(user);
                return "User deleted successfully";
            }
        }
        return "User not found";
    }
    public String logout() {
        if (currentUser != null) {
            currentUser = null;
            return "Logout successful";
        }
        return "No user is currently logged in";
    }

    public String getCurrentUserRole() {
        return currentUser.getRole().toString();
    }

    
    
}
