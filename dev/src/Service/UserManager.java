package Service;

import Domain.User;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, User> users = new HashMap<>();

    // Constructor - initialize the user manager with a default user
    public UserManager() {
        addUser(new User("superlee", "1234"));
    }

    // Check if a user exists by username
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    // Add a new user to the system
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    // Authenticate a user by username and password
    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null && user.checkPassword(password);
    }
}
