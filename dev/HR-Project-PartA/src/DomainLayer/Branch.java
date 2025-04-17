package DomainLayer;

import java.util.List;

public class Branch {
    private String Id;
    private String name;
    private String Address;
    private List<Employee> employees;

    // Constructor
    public Branch(String Id, String name, String Address, List<Employee> employees) {
        this.Id = Id;
        this.name = name;
        this.Address = Address;
        this.employees = employees;
    }

    // Getters
    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return Address;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    // Setters
    public void setId(String Id) {
        this.Id = Id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
