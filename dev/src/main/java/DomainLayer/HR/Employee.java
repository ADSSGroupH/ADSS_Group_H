package DomainLayer.HR;

import java.time.LocalDate;
import java.util.Set;

public class Employee {
    private String id;
    private String name;
    private String phoneNumber;
    private String branchId;
    private Set<Role> roles;
    private EmployeeContract contract;
    private String bankDetails;
    private boolean isArchived;
    private LocalDate archivedAt;
    private boolean isManager;
    private String password;



    // --- Constructor ---
    public Employee(String id, String name, String phoneNumber, String branchId, Set<Role> roles, EmployeeContract contract, String bankDetails, boolean isArchived, LocalDate archivedAt, boolean isManager , String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.branchId = branchId;
        this.roles = roles;
        this.contract = contract;
        this.bankDetails = bankDetails;
        this.isArchived = isArchived;
        this.archivedAt = archivedAt;
        this.isManager = isManager;
        this.password = password;
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

    public String getBranchId() {
        return branchId;
    }
    public void setBranchId(String branchId) {
        this.branchId = branchId;
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

    public LocalDate getArchivedAt() {
        return archivedAt;
    }
    public void setArchivedAt(LocalDate archivedAt) {
        this.archivedAt = archivedAt;
    }
    public boolean IsManager (){
        return isManager;
    }
    public void setManager(boolean manager){
        isManager = manager;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}