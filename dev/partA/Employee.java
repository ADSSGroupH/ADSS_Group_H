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

    // --- Getters and Setters ---
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Branch getBranch() {
        return branch;
    }
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public EmployeeContract getContract() {
        return contract;
    }
    public void setContract(EmployeeContract contract) {
        this.contract = contract;
    }

    public String getBankDetails() {
        return bankDetails;
    }
    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public boolean isArchived() {
        return isArchived;
    }
    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public String getArchivedAt() {
        return archivedAt;
    }
    public void setArchivedAt(String archivedAt) {
        this.archivedAt = archivedAt;
    }
}
