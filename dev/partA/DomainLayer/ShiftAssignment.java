import java.time.LocalDate;
import java.util.Date;

public class ShiftAssignment {
    private Employee employee;
    private Shift shift;
    private Role role;
    private LocalDate ArchiveDate;

    public ShiftAssignment(Employee employee, Shift shift, Role role, LocalDate ArchiveDate) {
        this.employee = employee;
        this.shift = shift;
        this.role = role;
        this.ArchiveDate = ArchiveDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Shift getShift() {
        return shift;
    }

    public Role getRole() {
        return role;
    }
    public LocalDate getArchiveDate() {
        return ArchiveDate;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public void setArchiveDate(LocalDate ArchiveDate) {
        this.ArchiveDate = ArchiveDate;
    }
}
