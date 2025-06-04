package DomainLayer.HR;

import java.time.LocalDate;

public class ShiftAssignment {
    private String Id;
    private Employee employee;
    private String shiftId;
    private Role role;
    private LocalDate ArchiveDate;
    private boolean IsArchived = false;

    public ShiftAssignment(Employee employee, String shiftId, Role role, LocalDate ArchiveDate) {
        this.Id =employee.getId()  + shiftId + role.getName();
        this.employee = employee;
        this.shiftId = shiftId;
        this.role = role;
        this.ArchiveDate = ArchiveDate;
    }

    public String getId() { //generates a unique id according to the shift assignment fields.
        return Id;
    }
    public Employee getEmployee() {
        return employee;
    }

    public String getShiftId() {
        return shiftId;
    }

    public Role getRole() {
        return role;
    }
    public LocalDate getArchiveDate() {
        return ArchiveDate;
    }

    public boolean getIsArchived (){return IsArchived;}

    public void setId(String NewId) {
        this.Id = Id;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setShift(String shiftId) {
        this.shiftId = shiftId;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setArchiveDate(LocalDate ArchiveDate) {
        this.ArchiveDate = ArchiveDate;
    }

    public void setArchived (boolean IsArchived){this.IsArchived = IsArchived;}

    public boolean isArchived (){return IsArchived; }


}