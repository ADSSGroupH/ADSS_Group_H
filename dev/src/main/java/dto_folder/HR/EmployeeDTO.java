package DTO.HR;

public class EmployeeDTO {
    private String id;
    private String name;
    private String phoneNumber;
    private String branchId;
    private String roleIds; // e.g. "1,3,7"
    private int salary;
    private String contractId;
    private String bankDetails;
    private boolean isArchived;
    private String archivedAt;
    private boolean isManager;

    // Default constructor
    public EmployeeDTO() {
    }

    // Full constructor
    public EmployeeDTO(String id, String name, String phoneNumber, String branchId,
                       String roleIds, int salary, String contractId, String bankDetails,
                       boolean isArchived, String archivedAt, boolean isManager) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.branchId = branchId;
        this.roleIds = roleIds;
        this.salary = salary;
        this.contractId = contractId;
        this.bankDetails = bankDetails;
        this.isArchived = isArchived;
        this.archivedAt = archivedAt;
        this.isManager = isManager;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public int getSalary() {
        return salary;
    }

    public String getContractId() {
        return contractId;
    }


    public String getBankDetails() {
        return bankDetails;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public String getArchivedAt() {
        return archivedAt;
    }

    public boolean isManager() {
        return isManager;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }


    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public void setArchivedAt(String archivedAt) {
        this.archivedAt = archivedAt;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
