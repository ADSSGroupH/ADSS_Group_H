package DTO.HR;


public class RoleDTO {
    private String id;
    private String name;

    // Default constructor
    public RoleDTO() {
    }

    // Constructor with all fields
    public RoleDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

}
