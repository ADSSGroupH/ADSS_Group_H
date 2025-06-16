package DTO.HR;

public class BranchDTO {
    private String id;
    private String name;
    private String address;
    private String employeeIds;  // מחרוזת ארוכה של IDs מופרדים בפסיקים

    public BranchDTO() {}

    public BranchDTO(String id, String name, String address, String employeeIds) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.employeeIds = employeeIds;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmployeeIds() {
        return employeeIds;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmployeeIds(String employeeIds) {
        this.employeeIds = employeeIds;
    }
}
