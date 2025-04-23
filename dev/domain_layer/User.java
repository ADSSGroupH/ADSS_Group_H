package dev.domain_layer;

public class User {
    private int id;
    private String userName;
    private String password;
    public enum Role {
        SystemManager,
        transportationManager,
        Driver
    }

    private Role role;

    public User(int id, String userName,String password, Role role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    
}
