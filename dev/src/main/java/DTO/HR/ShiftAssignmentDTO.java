package DTO.HR;

import java.time.LocalDate;

public class ShiftAssignmentDTO {
    private String id;
    private String employeeId;
    private String shiftId;
    private String roleId;
    private boolean isArchived;
    private String archiveDate; // במקום LocalDate

    public ShiftAssignmentDTO() {}

    public ShiftAssignmentDTO(String id, String employeeId, String shiftId, String roleId, String archiveDate, boolean isArchived) {
        this.id = id;
        this.employeeId = employeeId;
        this.shiftId = shiftId;
        this.roleId = roleId;
        this.archiveDate = archiveDate;
        this.isArchived = isArchived;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getShiftId() { return shiftId; }
    public void setShiftId(String shiftId) { this.shiftId = shiftId; }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public String getArchiveDate() { return archiveDate; }
    public void setArchiveDate(String archiveDate) { this.archiveDate = archiveDate; }

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { isArchived = archived; }
}
