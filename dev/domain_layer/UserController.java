package dev.domain_layer;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    List<User> users = new ArrayList<>();

    public UserController() {
        // Initialize with some users
        users.add(new User(1, "Tal","123", User.Role.SystemManager));
    }
    public String addUser(String userName, String password, User.Role role) {
        int id = users.size() + 1;
        User user = new User(id, userName, password, role);
        users.add(user);
        return "User added successfully";
    }

    public String login(String userName, String password) {
        for (User user : users) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                return "Login successful";
            }
        }
        return "Invalid username or password";
    }
    
}
