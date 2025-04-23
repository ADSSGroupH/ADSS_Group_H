package dev.domain_layer;

public class User {
    private String userName;
    private String password;
    public enum Role {
        SystemManager,
        transportationManager,
        Driver
    }

    private Role role;

    public User( String userName,String password, Role role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }


    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    
}
