import java.util.Set;

public class Employee {
    private String id;
    private String name;
    private String phoneNumber;
    private Branch branch;
    private Set<Role> roles;
    private EmployeeContract contract;
    private String bankDetails;
    private boolean isArchived;
    private String archivedAt;

    // --- Constructor ---
    public Employee(String id, String name, String phoneNumber, Branch branch, Set<Role> roles,
                    EmployeeContract contract, String bankDetails, boolean isArchived, String archivedAt) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.branch = branch;
        this.roles = roles;
        this.contract = contract;
        this.bankDetails = bankDetails;
        this.isArchived = isArchived;
        this.archivedAt = archivedAt;
    }
}
