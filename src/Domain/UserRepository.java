package Domain;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private static UserRepository instance = null;
    private final Map<String, String> users = new HashMap<>();

    private UserRepository() {
        // אפשר להכניס משתמשי ברירת מחדל
        users.put("superlee", "1234");
        users.put("manager", "password");
        users.put("1", "1");

    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    public boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public void addUser(String username, String password) {
        users.put(username, password);
    }
}
